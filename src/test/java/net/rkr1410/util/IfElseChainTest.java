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

    @Mock Supplier<String> stringSupplier;

/*
               ╦  ╦┌─┐┬─┐┬┌─┐┬ ┬  ┌─┐┬ ┬┌─┐┌─┐┬  ┬┌─┐┬─┐  ┌─┐┌─┐┬  ┬  ┌─┐
               ╚╗╔╝├┤ ├┬┘│├┤ └┬┘  └─┐│ │├─┘├─┘│  │├┤ ├┬┘  │  ├─┤│  │  └─┐
                ╚╝ └─┘┴└─┴└   ┴   └─┘└─┘┴  ┴  ┴─┘┴└─┘┴└─  └─┘┴ ┴┴─┘┴─┘└─┘
 */

    /* No conditions, just a default getter */
    @Test
    void noIfThenSuppliers_elseBranchSupplierEvaluated() {
        new IfElseChain<String>().elseDefault(stringSupplier);

        verify(stringSupplier).get();
    }

    /* Single false condition */
    @Test
    void singleFalseSupplier_elseBranchSupplierEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .elseDefault(stringSupplier);

        verify(stringSupplier).get();
    }

    @Test
    void singleFalseSupplier_falseBranchSupplierNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> false, stringSupplier)
                .elseDefault(() -> "");

        verify(stringSupplier, never()).get();
    }

    /* Single true condition */
    @Test
    void singleTrueSupplier_elseBranchSupplierNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .elseDefault(stringSupplier);

        verify(stringSupplier, never()).get();
    }

    @Test
    void singleTrueSupplier_trueBranchSupplierEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .elseDefault(() -> "");

        verify(stringSupplier).get();
    }

    /* Two conditions: first one is false, second one true */
    @Test
    void falseTrueSuppliers_trueBranchSupplierEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .ifThen(() -> true, stringSupplier)
                .elseDefault(() -> "");

        verify(stringSupplier).get();
    }

    @Test
    void falseTrueSuppliers_falseBranchSupplierNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> false, stringSupplier)
                .ifThen(() -> true, () -> "")
                .elseDefault(() -> "");

        verify(stringSupplier, never()).get();
    }

    @Test
    void falseTrueSuppliers_elseBranchSupplierNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .ifThen(() -> true, () -> "")
                .elseDefault(stringSupplier);

        verify(stringSupplier, never()).get();
    }

    /* Two conditions: first one is true, second one false */
    @Test
    void trueFalseSuppliers_trueBranchSupplierEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .ifThen(() -> false, () -> "")
                .elseDefault(() -> "");

        verify(stringSupplier).get();
    }

    @Test
    void trueFalseSuppliers_falseBranchSupplierNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> false, stringSupplier)
                .elseDefault(() -> "");

        verify(stringSupplier, never()).get();
    }

    @Test
    void trueFalseSuppliers_elseBranchSupplierNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> false, () -> "")
                .elseDefault(stringSupplier);

        verify(stringSupplier, never()).get();
    }

    /* Two conditions, both true */
    @Test
    void trueTrueSuppliers_firstTrueBranchSupplierEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .ifThen(() -> true, () -> "")
                .elseDefault(() -> "");

        verify(stringSupplier).get();
    }

    @Test
    void trueTrueSuppliers_secondTrueBranchSupplierNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> true, stringSupplier)
                .elseDefault(() -> "");

        verify(stringSupplier, never()).get();
    }

    @Test
    void trueTrueSuppliers_elseBranchSupplierNotEvaluated() {
        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> true, () -> "")
                .elseDefault(stringSupplier);

        verify(stringSupplier, never()).get();
    }

/*
               ╔═╗┌─┐┌─┐┌─┐┬─┐┌┬┐  ┬─┐┌─┐┌─┐┬ ┬┬ ┌┬┐  ┬  ┬┌─┐┬  ┬ ┬┌─┐┌─┐
               ╠═╣└─┐└─┐├┤ ├┬┘ │   ├┬┘├┤ └─┐│ ││  │   └┐┌┘├─┤│  │ │├┤ └─┐
               ╩ ╩└─┘└─┘└─┘┴└─ ┴   ┴└─└─┘└─┘└─┘┴─┘┴    └┘ ┴ ┴┴─┘└─┘└─┘└─┘
*/

    @Test
    void noIfThenSuppliers_elseSupplierResultReturned() {
        doReturn(ELSE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>().elseDefault(stringSupplier);

        assertThat(chainResult).isEqualTo(ELSE_VALUE);
    }

    @Test
    void singleFalseSupplier_elseSupplierResultReturned() {
        doReturn(ELSE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .elseDefault(stringSupplier);

        assertThat(chainResult).isEqualTo(ELSE_VALUE);
    }


    @Test
    void singleTrueSupplier_trueSupplierResultReturned() {
        doReturn(TRUE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }

    @Test
    void trueFalseSuppliers_trueSupplierResultReturned() {
        doReturn(TRUE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .ifThen(() -> false, () -> "")
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }

    @Test
    void falseTrueSuppliers_firstTrueSupplierResultReturned() {
        doReturn(TRUE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .ifThen(() -> true, stringSupplier)
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }

    @Test
    void trueTrueSuppliers_firstTrueSupplierResultReturned() {
        doReturn(TRUE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .ifThen(() -> true, () -> "")
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }
}