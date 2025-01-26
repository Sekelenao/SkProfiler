package io.github.sekelenao.skprofiler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.*;

final class TestOptionals {

    private static final String MISSING_DESCRIPTOR = "Unknown";

    @Test
    @DisplayName("Value as String or missing descriptor is working")
    void asStringOrMissingDescriptorIsWorking(){
        OptionalLong emptyOptionalLong = OptionalLong.empty();
        Optional<Object> emptyOptional = Optional.empty();
        assertAll(
            () -> assertThrows(
                NullPointerException.class,
                () -> Optionals.asStringOrMissingDescriptor(null)
            ),
            () -> assertThrows(
                NullPointerException.class,
                () -> Optionals.asStringOrMissingDescriptor(null, Long::toString)
            ),
            () -> assertEquals(MISSING_DESCRIPTOR, Optionals.asStringOrMissingDescriptor(emptyOptionalLong, Long::toString)),
            () -> assertEquals(MISSING_DESCRIPTOR, Optionals.asStringOrMissingDescriptor(emptyOptional)),
            () -> assertEquals(
                "96",
                Optionals.asStringOrMissingDescriptor(OptionalLong.of(96L), Long::toString)
            ),
            () -> assertEquals(
                "96ok",
                Optionals.asStringOrMissingDescriptor(OptionalLong.of(96L), l -> l + "ok")
            ),
            () -> assertEquals("test", Optionals.asStringOrMissingDescriptor(Optional.of("test")))
        );
    }

    @Test
    @DisplayName("Empty if negative is working")
    void emptyIfNegativeIsWorking(){
        assertAll(
            () -> assertEquals(OptionalLong.empty(), Optionals.emptyIfNegative(-1L)),
            () -> assertEquals(OptionalLong.of(1L), Optionals.emptyIfNegative(1L))
        );
    }

}
