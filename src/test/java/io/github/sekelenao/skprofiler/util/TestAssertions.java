package io.github.sekelenao.skprofiler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class TestAssertions {

    @Test
    @DisplayName("Check port assertion is working")
    void checkPortAssertion() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> Assertions.checkPort(-1)),
                () -> assertThrows(IllegalArgumentException.class, () -> Assertions.checkPort(65536)),
                () -> assertDoesNotThrow(() -> Assertions.checkPort(8080)),
                () -> assertDoesNotThrow(() -> Assertions.checkPort(0)),
                () -> assertDoesNotThrow(() -> Assertions.checkPort(65535))
        );
    }

    @Test
    @DisplayName("Require non nulls assertion is working")
    void requireNonNullsAssertion() {
        var emptyArray = new Object[]{};
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> Assertions.requireNonNulls((Object[]) null)),
                () -> assertThrows(NullPointerException.class, () -> Assertions.requireNonNulls("test", null, 5)),
                () -> assertDoesNotThrow(() -> Assertions.requireNonNulls("test", "value", 5)),
                () -> assertDoesNotThrow(() -> Assertions.requireNonNulls(emptyArray))
        );
    }

    @Test
    @DisplayName("Is positive assertion is working")
    void isPositiveAssertion() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> Assertions.isPositive(-5.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> Assertions.isPositive(-0.1)),
                () -> assertDoesNotThrow(() -> Assertions.isPositive(0.0)),
                () -> assertDoesNotThrow(() -> Assertions.isPositive(5.0))
        );
    }

    @Test
    @DisplayName("Are positives assertion is working")
    void arePositivesAssertion() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> Assertions.arePositives(-5.0, -1.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> Assertions.arePositives(-0.1, 0.0)),
                () -> assertDoesNotThrow(() -> Assertions.arePositives(0.0, 5.0)),
                () -> assertDoesNotThrow(() -> Assertions.arePositives(5.0, 1.0))
        );
    }

    @Test
    @DisplayName("Greater than is working")
    void greaterThanIsWorking(){
        assertAll(
            () -> assertDoesNotThrow(() -> Assertions.isGreaterThan(-5.64, -5.65)),
            () -> assertThrows(IllegalArgumentException.class, () -> Assertions.isGreaterThan(-5.65, -5.64)),
            () -> assertThrows(IllegalArgumentException.class, () -> Assertions.isGreaterThan(-5.65, -5.65)),
            () -> assertThrows(IllegalArgumentException.class, () -> Assertions.isGreaterThan(-5.65, 0.0)),
            () -> assertDoesNotThrow(() -> Assertions.isGreaterThan(0, -5.65)),
            () -> assertThrows(IllegalArgumentException.class, () -> Assertions.isGreaterThan(0, 0)),
            () -> assertDoesNotThrow(() -> Assertions.isGreaterThan(5.65, 0))
        );
    }

}
