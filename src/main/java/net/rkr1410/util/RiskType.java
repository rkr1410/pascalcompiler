package net.rkr1410.util;

public enum RiskType {
    // No analysis done
    UNDECIDED,
    // Application too narrow to decide usability,
    // might be worth considering after made more general
    TOO_SPECIFIC,
    // Might be taking functional features too far for Java.
    // Trying to introduce concepts not 'in tune' with the language.
    // Example would be trying to unify Optional, List and Future as Functors,
    // stuff like implementing forEach instead of ifPresent for Optional.
    LAMBDA_ABUSE,
    // Looks like it could have potential application in real-world code
    POTENTIAL_VALUE
}
