package net.rkr1410.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IfElseChainTest {

    private static final String ELSE_VALUE = "value returned on else";
    private static final String TRUE_VALUE = "value returned when true branch evaluated";

    @Mock Supplier<String> resultEvaluator;
    @Mock Supplier<Boolean> firstConditionFalse;
    @Mock Supplier<Boolean> secondConditionTrue;
    @Mock Supplier<Boolean> thirdConditionFalse;
    @Mock Supplier<Boolean> fourthConditionTrue;

/*
                   ╦  ╦┌─┐┬─┐┬┌─┐┬ ┬  ┌─┐┬ ┬┌─┐┌─┐┬  ┬┌─┐┬─┐  ┌─┐┌─┐┬  ┬  ┌─┐
                   ╚╗╔╝├┤ ├┬┘│├┤ └┬┘  └─┐│ │├─┘├─┘│  │├┤ ├┬┘  │  ├─┤│  │  └─┐
                    ╚╝ └─┘┴└─┴└   ┴   └─┘└─┘┴  ┴  ┴─┘┴└─┘┴└─  └─┘┴ ┴┴─┘┴─┘└─┘

          A set of tests verifying if only the expected suppliers get evaluated
 */

    /* No conditions, just a default getter */
    @Test
    void noConditions_elseBranchResultEvaluated() {
        new IfElseChain<String>().elseDefault(resultEvaluator);

        verify(resultEvaluator).get();
    }

    /* Single false condition */
    @Test
    void singleFalseCondition_elseBranchResultEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .elseDefault(resultEvaluator);

        verify(resultEvaluator).get();
    }

    @Test
    void singleFalseCondition_falseBranchResultNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> false, resultEvaluator)
                .elseDefault(() -> "");

        verify(resultEvaluator, never()).get();
    }

    /* Single true condition */
    @Test
    void singleTrueCondition_elseBranchResultNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .elseDefault(resultEvaluator);

        verify(resultEvaluator, never()).get();
    }

    @Test
    void singleTrueCondition_trueBranchResultEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, resultEvaluator)
                .elseDefault(() -> "");

        verify(resultEvaluator).get();
    }

    /* Two conditions: first one is false, second one true */
    @Test
    void falseTrueConditions_trueBranchResultEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .ifThen(() -> true, resultEvaluator)
                .elseDefault(() -> "");

        verify(resultEvaluator).get();
    }

    @Test
    void falseTrueConditions_falseBranchResultNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> false, resultEvaluator)
                .ifThen(() -> true, () -> "")
                .elseDefault(() -> "");

        verify(resultEvaluator, never()).get();
    }

    @Test
    void falseTrueConditions_elseBranchResultNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .ifThen(() -> true, () -> "")
                .elseDefault(resultEvaluator);

        verify(resultEvaluator, never()).get();
    }

    /* Two conditions: first one is true, second one false */
    @Test
    void trueFalseConditions_trueBranchResultEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, resultEvaluator)
                .ifThen(() -> false, () -> "")
                .elseDefault(() -> "");

        verify(resultEvaluator).get();
    }

    @Test
    void trueFalseConditions_falseBranchResultNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> false, resultEvaluator)
                .elseDefault(() -> "");

        verify(resultEvaluator, never()).get();
    }

    @Test
    void trueFalseConditions_elseBranchResultNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> false, () -> "")
                .elseDefault(resultEvaluator);

        verify(resultEvaluator, never()).get();
    }

    /* Two conditions, both true */
    @Test
    void trueTrueConditions_firstTrueBranchResultEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, resultEvaluator)
                .ifThen(() -> true, () -> "")
                .elseDefault(() -> "");

        verify(resultEvaluator).get();
    }

    @Test
    void trueTrueConditions_secondTrueBranchResultNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> true, resultEvaluator)
                .elseDefault(() -> "");

        verify(resultEvaluator, never()).get();
    }

    @Test
    void trueTrueConditions_elseBranchResultNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> true, () -> "")
                .elseDefault(resultEvaluator);

        verify(resultEvaluator, never()).get();
    }

    @Test
    void afterFirstTrueCondition_noneOfFollowingConditionsIsEvaluated() {
        doReturn(false).when(firstConditionFalse).get();
        doReturn(true).when(secondConditionTrue).get();

        new IfElseChain<String>()
                .ifThen(firstConditionFalse, () -> "")
                .ifThen(secondConditionTrue, () -> "")
                .ifThen(thirdConditionFalse, () -> "")
                .ifThen(fourthConditionTrue, () -> "")
                .elseDefault(resultEvaluator);

        verify(firstConditionFalse).get();
        verify(secondConditionTrue).get();
        verify(thirdConditionFalse, never()).get();
        verify(fourthConditionTrue, never()).get();
    }

/*
                   ╔═╗┌─┐┌─┐┌─┐┬─┐┌┬┐  ┬─┐┌─┐┌─┐┬ ┬┬ ┌┬┐  ┬  ┬┌─┐┬  ┬ ┬┌─┐┌─┐
                   ╠═╣└─┐└─┐├┤ ├┬┘ │   ├┬┘├┤ └─┐│ ││  │   └┐┌┘├─┤│  │ │├┤ └─┐
                   ╩ ╩└─┘└─┘└─┘┴└─ ┴   ┴└─└─┘└─┘└─┘┴─┘┴    └┘ ┴ ┴┴─┘└─┘└─┘└─┘

          Set of tests asserting that the value returned by calling the chain is exactly
          the value returned by evaluating the supplier for which paired condition supplier returned true as first.
*/

    @Test
    void noIfThenSuppliers_elseSupplierResultReturned() {
        doReturn(ELSE_VALUE).when(resultEvaluator).get();

        String chainResult = new IfElseChain<String>().elseDefault(resultEvaluator);

        assertThat(chainResult).isEqualTo(ELSE_VALUE);
    }

    @Test
    void singleFalseSupplier_elseSupplierResultReturned() {
        doReturn(ELSE_VALUE).when(resultEvaluator).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .elseDefault(resultEvaluator);

        assertThat(chainResult).isEqualTo(ELSE_VALUE);
    }


    @Test
    void singleTrueSupplier_trueSupplierResultReturned() {
        doReturn(TRUE_VALUE).when(resultEvaluator).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> true, resultEvaluator)
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }

    @Test
    void trueFalseSuppliers_trueSupplierResultReturned() {
        doReturn(TRUE_VALUE).when(resultEvaluator).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> true, resultEvaluator)
                .ifThen(() -> false, () -> "")
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }

    @Test
    void falseTrueSuppliers_firstTrueSupplierResultReturned() {
        doReturn(TRUE_VALUE).when(resultEvaluator).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .ifThen(() -> true, resultEvaluator)
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }

    @Test
    void trueTrueSuppliers_firstTrueSupplierResultReturned() {
        doReturn(TRUE_VALUE).when(resultEvaluator).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> true, resultEvaluator)
                .ifThen(() -> true, () -> "")
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }
}