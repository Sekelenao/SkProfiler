package io.github.sekelenao.skprofiler.util;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;

public final class Optionals {

    private static final String MISSING_INFORMATION = "Unknown";

    private Optionals() {
        throw new AssertionError("You cannot instantiate this class");
    }

    /**
     * Returns the string representation of the value contained in the provided Optional,
     * or the missing descriptor string if the Optional is empty.
     *
     * @param optional an Optional containing a value or empty if no value is present
     * @return the string representation of the value if present, or the missing descriptor string if the Optional
     * is empty
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static String asStringOrMissingDescriptor(Optional<?> optional) {
        Objects.requireNonNull(optional);
        return optional.map(String::valueOf).orElse(MISSING_INFORMATION);
    }

    /**
     * Converts the value contained in the provided {@code OptionalLong} to its string representation,
     * or returns a predefined missing descriptor string if the {@code OptionalLong} is empty.
     *
     * @param optional the {@code OptionalLong} containing a value or empty if no value is present
     * @return the string representation of the value if present, or a missing descriptor string if empty
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static String asStringOrMissingDescriptor(OptionalLong optional) {
        Objects.requireNonNull(optional);
        if(optional.isEmpty()) {
            return MISSING_INFORMATION;
        }
        return Long.toString(optional.getAsLong());
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
