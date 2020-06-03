package net.rkr1410.util;

public enum RiskType {
    /* No analysis done */
    UNDECIDED,
    /* Application too narrow to decide usability, might be worth considering
       after made more general. Or, conversely, written for learning purposes
       very abstractly but unfitting the use case - e.g. very general implementation
       being a private method serving single case, which normally should be rewritten
       as more specific. */
    WRONG_ABSTRACTION_LEVEL,
    /* Might be taking functional features too far for Java.
       Trying to introduce concepts not 'in tune' with the language.
       Example would be trying to unify Optional, List and Future as Functors,
       or using Optional.ofNullable(a).orElse(const) instead of
       return a != null ? a : const */
    LAMBDA_ABUSE,
    /* Looks like it could have potential application in real-world code */
    POTENTIAL_VALUE
}
