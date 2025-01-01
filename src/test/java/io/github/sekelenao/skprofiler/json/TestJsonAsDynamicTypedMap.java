package io.github.sekelenao.skprofiler.json;

import io.github.sekelenao.skprofiler.exception.FormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

final class TestJsonAsDynamicTypedMap {

    @Test
    @DisplayName("Put assertions")
    void putAssertions() {
        var map = new JsonAsDynamicTypedMap();
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> map.put(null, "value")),
                () -> assertThrows(NullPointerException.class, () -> map.put("key", null))
        );
    }

    @Test
    @DisplayName("Dynamic typed map size and put are working")
    void size() {
        var map = new JsonAsDynamicTypedMap();
        assertAll(
                () -> assertEquals(0, map.size()),
                () -> {
                    map.put("key", "value");
                    assertEquals(1, map.size());
                },
                () -> {
                    IntStream.range(0, 100)
                            .forEach(i -> map.put("key" + i, "value"));
                    assertEquals(101, map.size());
                }
        );
    }

    static Stream<Arguments> provideClassAndCorrespondingExceptions() {
        return Stream.of(
                Arguments.of(boolean.class, FormatException.class),
                Arguments.of(Boolean.class, FormatException.class),
                Arguments.of(byte.class, NumberFormatException.class),
                Arguments.of(Byte.class, NumberFormatException.class),
                Arguments.of(short.class, NumberFormatException.class),
                Arguments.of(Short.class, NumberFormatException.class),
                Arguments.of(int.class, NumberFormatException.class),
                Arguments.of(Integer.class, NumberFormatException.class),
                Arguments.of(long.class, NumberFormatException.class),
                Arguments.of(Long.class, NumberFormatException.class),
                Arguments.of(float.class, NumberFormatException.class),
                Arguments.of(Float.class, NumberFormatException.class),
                Arguments.of(double.class, NumberFormatException.class),
                Arguments.of(Double.class, NumberFormatException.class),
                Arguments.of(char.class, FormatException.class),
                Arguments.of(Character.class, FormatException.class),
                Arguments.of(ArrayList.class, IllegalArgumentException.class)
        );
    }

    @ParameterizedTest
    @MethodSource("provideClassAndCorrespondingExceptions")
    @DisplayName("Get assertions")
    void getAssertions(Class<?> type, Class<? extends Throwable> throwable) {
        var map = new JsonAsDynamicTypedMap();
        map.put("key", "INVALID VALUE");
        assertThrows(throwable, () -> map.get("key", type));
    }

    @Test
    @DisplayName("Get an unknown key throws")
    void getUnknownKeyThrows() {
        var map = new JsonAsDynamicTypedMap();
        assertThrows(NoSuchElementException.class, () -> map.get("key", String.class));
    }

    static Stream<Arguments> provideClassAndCorrespondingValue() {
        return Stream.of(
                Arguments.of(boolean.class, "false", false),
                Arguments.of(Boolean.class, "true", true),
                Arguments.of(byte.class, "127", Byte.MAX_VALUE),
                Arguments.of(Byte.class, "-128", Byte.MIN_VALUE),
                Arguments.of(short.class, "-32768", Short.MIN_VALUE),
                Arguments.of(Short.class, "32767", Short.MAX_VALUE),
                Arguments.of(int.class, "2147483647", Integer.MAX_VALUE),
                Arguments.of(Integer.class, "-2147483648", Integer.MIN_VALUE),
                Arguments.of(long.class, "9223372036854775807", Long.MAX_VALUE),
                Arguments.of(Long.class, "-9223372036854775808", Long.MIN_VALUE),
                Arguments.of(float.class, "3.4028235E38", Float.MAX_VALUE),
                Arguments.of(Float.class, "1.4E-45", Float.MIN_VALUE),
                Arguments.of(double.class, "1.7976931348623157E308", Double.MAX_VALUE),
                Arguments.of(Double.class, "4.9E-324", Double.MIN_VALUE),
                Arguments.of(char.class, "c", 'c'),
                Arguments.of(Character.class, "C", 'C'),
                Arguments.of(String.class, "Hello World", "Hello World")
        );
    }

    @ParameterizedTest
    @MethodSource("provideClassAndCorrespondingValue")
    @DisplayName("Get is working for all types")
    void getIsWorking(Class<?> type, String value, Object expected) {
        var map = new JsonAsDynamicTypedMap();
        map.put("key", value);
        assertEquals(expected, map.get("key", type));
    }

}
