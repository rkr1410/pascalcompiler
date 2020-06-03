package net.rkr1410.util;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

import static net.rkr1410.util.RiskType.LAMBDA_ABUSE;

/**
 * <h1>An if-else chain</h1>
 *
 * <p>Implementation of series of if-else statements returning a generic type.
 * Similar to Java 12 switch expression, but allows for dynamic conditions and forces non-fall through behaviour.
 *
 * <h2>Example:</h2>
 * <pre>
 * {@code
 * return new IfElseChain<String>()
 *     .ifThen(this::someBooleanMethod(), () -> "a value")
 *     .ifThen(() -> anotherBooleanMethod(capturedField), () -> someObject.toString())
 *     .elseDefault(() -> "a default value");
 * }
 * </pre>
 * is equivalent to:
 * <pre>
 * {@code
 * String returnValue;
 * if (someBooleanMethod()) {
 *     returnValue = "a value";
 * } else if (anotherBooleanMethod(capturedField)) {
 *     returnValue = someObject.toString();
 * } else {
 *     returnValue = "a default value";
 * }
 * return returnValue;
 * }
 * </pre>
 * @param <T> expression type
 */
@LearningExperience(type = LAMBDA_ABUSE)
public class IfElseChain<T> {
    private LinkedHashMap<Supplier<Boolean>, Supplier<T>> conditionalValues;

    /**
     * Constructor
     */
    public IfElseChain() {
        conditionalValues = new LinkedHashMap<>();
    }

    /**
     * Add a representation of a single branch of if-else series.
     *
     * @param conditionEvaluator used to evaluate the condition
     * @param returnValueEvaluator used to evaluate resulting value, if condition is true
     * @return <code>this</code>, for method chaining
     */
    public IfElseChain<T> ifThen(Supplier<Boolean> conditionEvaluator, Supplier<T> returnValueEvaluator) {
        conditionalValues.put(conditionEvaluator, returnValueEvaluator);
        return this;
    }

    /**
     * Terminating method, which evaluates the whole expression.
     *
     * This method will evaluate conditions added by {@link #ifThen(Supplier, Supplier)} according to order
     * in which they were added. For the first condition which evaluates to true, it's paired supplier will
     * be used to calculate return value. If none evaluate to true, supplier passed to this method will be
     * used instead.
     *
     * @param defaultSupplier Supplier to use if all <code>ifThen</code> conditions evaluate to <code>false</code>
     * @return a result of interpreting this instance as series of if-else statements
     */
    public T elseDefault(Supplier<T> defaultSupplier) {
        return conditionalValues
                .entrySet()
                .stream()
                .filter(e -> e.getKey().get())
                .findFirst()
                .map(e -> e.getValue().get())
                .orElseGet(defaultSupplier);
    }
}
