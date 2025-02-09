package io.github.sekelenao.skprofiler.http.response;

import com.sun.net.httpserver.Headers;
import io.github.sekelenao.skprofiler.http.dto.send.MessageDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class TestCustomHttpResponse {

    @Test
    @DisplayName("Should return internal server error when processor throws an exception")
    void shouldReturnInternalServerErrorWhenProcessorThrowsException() throws IOException {
        RequestProcessor processor = mock(RequestProcessor.class);
        String route = "/test-route";
        when(processor.response()).thenThrow(new IOException("Mocked IO Exception"));
        CustomHttpResponse response = CustomHttpResponse.safeProcess(processor, route);
        assertAll(
            () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status()),
            () -> assertTrue(response.body().isPresent()),
            () -> assertInstanceOf(MessageDTO.class, response.body().orElseThrow())
        );
    }

    @Test
    @DisplayName("Should correctly modify headers to include JSON content type and allowed methods")
    void shouldCorrectlyModifyHeaders() {
        Headers headers = new Headers();
        CustomHttpResponse.modifyHeaders(headers);
        assertAll(
            () -> assertEquals("application/json", headers.getFirst("Content-Type")),
            () -> assertEquals("GET, POST, DELETE", headers.getFirst("Access-Control-Allow-Methods"))
        );
    }

    @Test
    @DisplayName("Should create a successful response with a valid body")
    void shouldCreateSuccessfulResponseWithValidBody() {
        MessageDTO body = new MessageDTO("Test success message");
        CustomHttpResponse response = CustomHttpResponse.success(body);
        assertAll(
            () -> assertEquals(HttpStatus.SUCCESS, response.status()),
            () -> assertTrue(response.body().isPresent()),
            () -> assertEquals(body, response.body().orElseThrow())
        );
    }

    @Test
    @DisplayName("Should create a bad request response with default message")
    void shouldCreateBadRequestResponseWithDefaultMessage() {
        CustomHttpResponse response = CustomHttpResponse.badRequest();
        assertAll(
            () -> assertEquals(HttpStatus.BAD_REQUEST, response.status()),
            () -> assertTrue(response.body().isPresent())
        );
    }

    @Test
    @DisplayName("Should create a bad request response with custom message")
    void shouldCreateBadRequestResponseWithCustomMessage() {
        CustomHttpResponse response = CustomHttpResponse.badRequest("Test message");
        assertAll(
            () -> assertEquals(HttpStatus.BAD_REQUEST, response.status()),
            () -> assertTrue(response.body().isPresent())
        );
    }

    @Test
    @DisplayName("Should create a response with method not allowed status")
    void shouldCreateResponseWithMethodNotAllowedStatus() {
        CustomHttpResponse response = CustomHttpResponse.methodNotAllowed();
        assertAll(
            () -> assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.status()),
            () -> assertTrue(response.body().isPresent())
        );
    }

    @Test
    @DisplayName("Should create a not found response with default message")
    void shouldCreateNotFoundResponseWithDefaultMessage() {
        CustomHttpResponse response = CustomHttpResponse.notFound();
        assertAll(
            () -> assertEquals(HttpStatus.NOT_FOUND, response.status()),
            () -> assertTrue(response.body().isPresent())
        );
    }

    @Test
    @DisplayName("Should return processor response when no exception occurs")
    void shouldReturnProcessorResponseWhenNoExceptionOccurs() throws IOException {
        RequestProcessor processor = mock(RequestProcessor.class);
        String route = "/test-route";
        CustomHttpResponse expectedResponse = CustomHttpResponse.success(new MessageDTO("Test Message"));
        when(processor.response()).thenReturn(expectedResponse);
        CustomHttpResponse response = CustomHttpResponse.safeProcess(processor, route);
        assertAll(
            () -> assertEquals(expectedResponse, response),
            () -> assertEquals(HttpStatus.SUCCESS, response.status()),
            () -> assertTrue(response.body().isPresent()),
            () -> assertInstanceOf(MessageDTO.class, response.body().orElseThrow()),
            () -> assertEquals("Test Message", ((MessageDTO) response.body().orElseThrow()).message())
        );
    }

    @Test
    @DisplayName("Should throw NullPointerException when processor is null")
    void shouldThrowNullPointerExceptionWhenProcessorIsNull() {
        String route = "/test-route";
        assertThrows(NullPointerException.class, () -> CustomHttpResponse.safeProcess(null, route));
    }

    @Test
    @DisplayName("Should throw NullPointerException when route is null")
    void shouldThrowNullPointerExceptionWhenRouteIsNull() {
        RequestProcessor processor = mock(RequestProcessor.class);
        assertThrows(NullPointerException.class, () -> CustomHttpResponse.safeProcess(processor, null));
    }


}