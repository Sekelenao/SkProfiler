package io.github.sekelenao.skprofiler.util;

import java.util.Objects;

/**
 * A utility class that provides assertion methods to validate inputs and ensure
 * invariants in code.
 * This class is designed to be non-instantiable and only
 * provides static methods.
 */
public final class Assertions {

    private Assertions() {
        throw new AssertionError("You cannot instantiate this class");
    }

    /**
     * Validates that the specified port number is within the valid range of 0 to 65,535.
     * If the port is outside this range, an {@code IllegalArgumentException} is thrown.
     *
     * @param port the port number to validate
     * @throws IllegalArgumentException if the port is less than 0 or greater than 65,535
     */
    public static void checkPort(int port){
        if(port < 0 || port > 65535) {
            throw new IllegalArgumentException("Wrong port was provided: " + port);
        }
    }

    /**
     * Ensures that the provided array of objects and each of its elements are not null.
     * If any element is null, a {@link NullPointerException} will be thrown.
     *
     * @param objects the array of objects to validate for non-nullity
     * @throws NullPointerException if the array itself or any of its elements is null
     */
    public static void requireNonNulls(Object... objects){
        Objects.requireNonNull(objects);
        for(Object object : objects){
            Objects.requireNonNull(object);
        }
    }

    /**
     * Ensures the provided value is positive.
     * Throws an {@code IllegalArgumentException}
     * if the value is less than zero.
     *
     * @param value the numeric value to validate as positive
     * @throws IllegalArgumentException if the value is less than zero
     */
    public static void isPositive(double value){
        if(value < 0) {
            throw new IllegalArgumentException("Value should be positive: " + value);
        }
    }

    /**
     * Ensures all provided values are positive.
     * Each value in the provided array is validated using the {@code isPositive} method.
     * Throws an {@code IllegalArgumentException} if any value is less than zero.
     *
     * @param values the varargs array of numeric values to validate as positive
     * @throws IllegalArgumentException if any of the values is less than zero
     */
    public static void arePositives(double... values){
        for(double value : values){
            isPositive(value);
        }
    }

}
