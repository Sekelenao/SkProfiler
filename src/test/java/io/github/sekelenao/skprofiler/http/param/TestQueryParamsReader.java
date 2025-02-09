package io.github.sekelenao.skprofiler.http.param;

import io.github.sekelenao.skprofiler.exception.DynamicTypingException;
import io.github.sekelenao.skprofiler.exception.InvalidQueryParamException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class TestQueryParamsReader {

    @Test
    @DisplayName("Returns default value when key is not present")
    void returnsDefaultValueWhenKeyIsNotPresent() throws InvalidQueryParamException {
        QueryParamsReader queryParamsReader = new QueryParamsReader("");
        int result = queryParamsReader.presentOrDefault("missingKey", 42);
        assertEquals(42, result);
    }

    @Test
    @DisplayName("Returns parameter value when key is present")
    void returnsParameterValueWhenKeyIsPresent() throws InvalidQueryParamException {
        QueryParamsReader queryParamsReader = new QueryParamsReader("test=testing&presentKey=15");
        int result = queryParamsReader.presentOrDefault("presentKey", 42);
        assertEquals(15, result);
    }

    @Test
    @DisplayName("Throws InvalidQueryParamException when dynamic typing exception occurs")
    void throwsInvalidQueryParamExceptionWhenDynamicTypingExceptionOccurs() {
        QueryParamsReader queryParamsReader = new QueryParamsReader("test=testing&invalidKey=notAnInt");
        InvalidQueryParamException exception = assertThrows(
            InvalidQueryParamException.class,
            () -> queryParamsReader.presentOrDefault("invalidKey", 42)
        );
        assertNotNull(exception.getCause());
        assertInstanceOf(DynamicTypingException.class, exception.getCause());
    }

    @Test
    @DisplayName("Throws NullPointerException when key is null")
    void throwsNullPointerExceptionWhenKeyIsNull() {
        QueryParamsReader queryParamsReader = new QueryParamsReader("query");
        assertThrows(NullPointerException.class, () -> queryParamsReader.presentOrDefault(null, 42));
    }

}