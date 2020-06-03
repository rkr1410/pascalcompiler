package net.rkr1410.util;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

import static net.rkr1410.util.RiskType.LAMBDA_ABUSE;

/**
 * <h1>If - else chain</h1>
 *
 * Implementation of an if-else chain returning a generic type.
 * Similar to Java 12 switch expressions, but with dynamic conditions
 * and non-fall through behaviour
 *
 * @param <T> returned type
 */
@LearningExperience(type = LAMBDA_ABUSE)
public class IfElseChain<T> {
    private LinkedHashMap<Supplier<Boolean>, Supplier<T>> conditionalValues;

    /**
     * Constructor
     *
     * Initializes backing structures
     */
    public IfElseChain() {
        conditionalValues = new LinkedHashMap<>();
    }


    public IfElseChain<T> ifThen(Supplier<Boolean> voidPredicate, Supplier<T> returnValue) {
        conditionalValues.put(voidPredicate, returnValue);
        return this;
    }

    public T elseDefault(Supplier<T> returnValue) {
        return conditionalValues
                .entrySet()
                .stream()
                .filter(e -> e.getKey().get())
                .findFirst()
                .map(e -> e.getValue().get())
                .orElseGet(returnValue);
    }
}
