package net.rkr1410.lang.frontend;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IfElseChainTest {

    private static final String ELSE_VALUE = "value returned on else";
    private static final String TRUE_VALUE = "value returned when true branch evaluated";

    @Mock Supplier<String> stringSupplier;

    /**
     * Verify supplier evaluations
     */

    // No conditions, just a default getter
    @Test
    void noIfThenSuppliers_elseBranchSupplierEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>().elseDefault(stringSupplier);

        verify(stringSupplier).get();
    }

    // Single false condition
    @Test
    void singleFalseSupplier_elseBranchSupplierEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .elseDefault(stringSupplier);

        verify(stringSupplier).get();
    }

    @Test
    void singleFalseSupplier_falseBranchSupplierNotEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> false, stringSupplier)
                .elseDefault(() -> "");

        verify(stringSupplier, never()).get();
    }

    // Single true condition
    @Test
    void singleTrueSupplier_elseBranchSupplierNotEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .elseDefault(stringSupplier);

        verify(stringSupplier, never()).get();
    }

    @Test
    void singleTrueSupplier_trueBranchSupplierEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .elseDefault(() -> "");

        verify(stringSupplier).get();
    }

    // Two conditions: first one is false, second one true
    @Test
    void falseTrueSuppliers_trueBranchSupplierEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .ifThen(() -> true, stringSupplier)
                .elseDefault(() -> "");

        verify(stringSupplier).get();
    }

    @Test
    void falseTrueSuppliers_falseBranchSupplierNotEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> false, stringSupplier)
                .ifThen(() -> true, () -> "")
                .elseDefault(() -> "");

        verify(stringSupplier, never()).get();
    }

    @Test
    void falseTrueSuppliers_elseBranchSupplierNotEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .ifThen(() -> true, () -> "")
                .elseDefault(stringSupplier);

        verify(stringSupplier, never()).get();
    }

    // Two conditions: first one is true, second one false
    @Test
    void trueFalseSuppliers_trueBranchSupplierEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .ifThen(() -> false, () -> "")
                .elseDefault(() -> "");

        verify(stringSupplier).get();
    }

    @Test
    void trueFalseSuppliers_falseBranchSupplierNotEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> false, stringSupplier)
                .elseDefault(() -> "");

        verify(stringSupplier, never()).get();
    }

    @Test
    void trueFalseSuppliers_elseBranchSupplierNotEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> false, () -> "")
                .elseDefault(stringSupplier);

        verify(stringSupplier, never()).get();
    }

    // Two conditions, both true
    @Test
    void trueTrueSuppliers_firstTrueBranchSupplierEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .ifThen(() -> true, () -> "")
                .elseDefault(() -> "");

        verify(stringSupplier).get();
    }

    @Test
    void trueTrueSuppliers_secondTrueBranchSupplierNotEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> true, stringSupplier)
                .elseDefault(() -> "");

        verify(stringSupplier, never()).get();
    }

    @Test
    void trueTrueSuppliers_elseBranchSupplierNotEvaluated() {
        MockitoAnnotations.initMocks(this);

        new IfElseChain<String>()
                .ifThen(() -> true, () -> "")
                .ifThen(() -> true, () -> "")
                .elseDefault(stringSupplier);

        verify(stringSupplier, never()).get();
    }

    /**
     * Assert returned results
     */

    @Test
    void noIfThenSuppliers_elseSupplierResultReturned() {
        MockitoAnnotations.initMocks(this);
        doReturn(ELSE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>().elseDefault(stringSupplier);

        assertThat(chainResult).isEqualTo(ELSE_VALUE);
    }


    @Test
    void singleFalseSupplier_elseSupplierResultReturned() {
        MockitoAnnotations.initMocks(this);
        doReturn(ELSE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .elseDefault(stringSupplier);

        assertThat(chainResult).isEqualTo(ELSE_VALUE);
    }


    @Test
    void singleTrueSupplier_trueSupplierResultReturned() {
        MockitoAnnotations.initMocks(this);
        doReturn(TRUE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }

    @Test
    void trueFalseSuppliers_trueSupplierResultReturned() {
        MockitoAnnotations.initMocks(this);
        doReturn(TRUE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .ifThen(() -> false, () -> "")
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }

    @Test
    void falseTrueSuppliers_firstTrueSupplierResultReturned() {
        MockitoAnnotations.initMocks(this);
        doReturn(TRUE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> false, () -> "")
                .ifThen(() -> true, stringSupplier)
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }

    @Test
    void trueTrueSuppliers_firstTrueSupplierResultReturned() {
        MockitoAnnotations.initMocks(this);
        doReturn(TRUE_VALUE).when(stringSupplier).get();

        String chainResult = new IfElseChain<String>()
                .ifThen(() -> true, stringSupplier)
                .ifThen(() -> true, () -> "")
                .elseDefault(() -> "");

        assertThat(chainResult).isEqualTo(TRUE_VALUE);
    }
}