package io.github.sekelenao.skprofiler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class TestOptionals {

    @Test
    @DisplayName("Empty if negative is working")
    void emptyIfNegativeIsWorking(){
        assertAll(
            () -> assertEquals(OptionalLong.empty(), Optionals.emptyIfNegative(-1L)),
            () -> assertEquals(OptionalLong.of(1L), Optionals.emptyIfNegative(1L))
        );
    }

    @Test
    @DisplayName("Empty string if null is working")
    void emptyStringIfNullIsWorking() {
        var value = "Non-null value";
        assertAll(
            () -> assertEquals("", Optionals.emptyStringIfNull(null)),
            () -> assertEquals(value, Optionals.emptyStringIfNull(value))
        );
    }

}
