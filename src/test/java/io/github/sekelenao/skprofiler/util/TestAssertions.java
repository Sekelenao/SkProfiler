package io.github.sekelenao.skprofiler.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class TestAssertions {

    @Test
    void checkPortAssertion() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> Assertions.checkPort(-1)),
                () -> assertThrows(IllegalArgumentException.class, () -> Assertions.checkPort(65536)),
                () -> assertDoesNotThrow(() -> Assertions.checkPort(8080)),
                () -> assertDoesNotThrow(() -> Assertions.checkPort(0)),
                () -> assertDoesNotThrow(() -> Assertions.checkPort(65535))
        );
    }

}
