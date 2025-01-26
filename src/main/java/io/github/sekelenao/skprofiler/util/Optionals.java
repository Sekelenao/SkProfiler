package io.github.sekelenao.skprofiler.util;

import java.util.OptionalLong;

public final class Optionals {

    private static final String MISSING_INFORMATION = "Unknown";

    private Optionals() {
        throw new AssertionError("You cannot instantiate this class");
    }

    /**
     * Returns a default descriptor string representing missing or unknown information.
     *
     * @return a string indicating missing or unknown information
     */
    public static String missingValueDescriptor() {
        return MISSING_INFORMATION;
    }

    /**
     * Returns an {@code OptionalLong} containing the provided value if it is non-negative,
     * or an empty {@code OptionalLong} if the value is negative.
     *
     * @param value the long value to evaluate
     * @return an {@code OptionalLong} containing the value if it is non-negative,
     *         or an empty {@code OptionalLong} if the value is negative
     */
    public static OptionalLong emptyIfNegative(long value) {
        return value < 0 ? OptionalLong.empty() : OptionalLong.of(value);
    }

}
