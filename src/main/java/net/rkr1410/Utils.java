package net.rkr1410;

import static net.rkr1410.RiskType.POTENTIAL_VALUE;

public class Utils {
    /**
     * Abuse Java's generic inference to throw a checked exception
     * as unchecked. For use with functional types which don't declare
     * any checked exceptions like Runnable or Supplier.
     *
     * Warning: make sure to handle the exceptions as if these were checked.
     * TODO: can I make this stand out? Some safety filp only activated when this is tested in calling method?
     *
     * @param t Exception to throw
     * @param <T> type of the exception, inferred as Throwable
     * @throws T supplied exception
     */
    @LearningExperience(type = POTENTIAL_VALUE) // Google says this has seen some wider adoption.
    public static <T extends Throwable> void sneakThrow(Throwable t) throws T {
        throw (T) t;
    }
}
