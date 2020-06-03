package net.rkr1410;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a marker interface type annotation.
 *
 * Used to denote a class is written as a learning experience.
 *
 * Intended tp be used for marking a class which is either a re-implementation
 * of a standard library class, or uses constructs that abuse the language or
 * are otherwise non-defensible from clean-code or common sense perspective.
 *
 * Might also be used to denote types bordering on such behaviour, which
 * after careful consideration could be put into real-life code.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LearningExperience {

    RiskType type() default RiskType.UNDECIDED;
}
