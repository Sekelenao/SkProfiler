package io.github.sekelenao.skprofiler.http;

import io.github.sekelenao.skprofiler.exception.DynamicTypingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

final class TestQueryParamsAsDynamicTypedMap {

    @Test
    @DisplayName("Should throw NullPointerException for null query")
    void shouldThrowExceptionForNullQuery() {
        assertThrows(NullPointerException.class, () -> QueryParamsAsDynamicTypedMap.of(null));
    }

    @Test
    @DisplayName("Should retrieve integer value from query")
    void shouldRetrieveIntegerValueFromQuery() {
        String query = "key1=42&key2=100";
        QueryParamsAsDynamicTypedMap map = QueryParamsAsDynamicTypedMap.of(query);
        assertAll(
            () -> assertEquals(OptionalInt.of(42), map.getAsInt("key1")),
            () -> assertEquals(OptionalInt.of(100), map.getAsInt("key2"))
        );
    }

    @Test
    @DisplayName("Should return empty OptionalInt if key is not found")
    void shouldReturnEmptyOptionalIntForMissingKey() throws DynamicTypingException {
        String query = "key1=42";
        QueryParamsAsDynamicTypedMap map = QueryParamsAsDynamicTypedMap.of(query);
        assertEquals(OptionalInt.empty(), map.getAsInt("key2"));
    }

    @Test
    @DisplayName("Should throw if key has no value")
    void shouldThrowForKeyWithNoValue() {
        String query = "key1=";
        QueryParamsAsDynamicTypedMap map = QueryParamsAsDynamicTypedMap.of(query);
        assertThrows(DynamicTypingException.class, () -> map.getAsInt("key1"));
    }

    @Test
    @DisplayName("Should throw DynamicTypingException for invalid integer value")
    void shouldThrowExceptionForInvalidIntegerValue() {
        String query = "key1=notAnInt";
        QueryParamsAsDynamicTypedMap map = QueryParamsAsDynamicTypedMap.of(query);
        assertThrows(DynamicTypingException.class, () -> map.getAsInt("key1"));
    }

    @Test
    @DisplayName("Should throw NullPointerException for null key in getAsInt")
    void shouldThrowExceptionForNullKeyInGetAsInt() {
        String query = "key1=42";
        QueryParamsAsDynamicTypedMap map = QueryParamsAsDynamicTypedMap.of(query);
        assertThrows(NullPointerException.class, () -> map.getAsInt(null));
    }

}