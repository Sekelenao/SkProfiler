package io.github.sekelenao.skprofiler.http.endpoint;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.dto.send.MessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

final class TestEndpoint {

    private HttpExchange mockedExchange;

    private Headers mockedResponseHeaders;

    private ByteArrayInputStream mockedRequestInput;

    private ByteArrayOutputStream mockedResponseOutput;

    private static final Endpoint FAKE_ENDPOINT = new Endpoint() {

        @Override
        public String route() {
            return "/fake";
        }

        @Override
        public CustomHttpResponse processGetRequest() {
            return CustomHttpResponse.success(new MessageDTO("Hello world !"));
        }

    };

    @BeforeEach
    void setUp() {
        mockedExchange = mock(HttpExchange.class);
        mockedResponseHeaders = mock(Headers.class);
        mockedRequestInput = new ByteArrayInputStream("Hello world !".getBytes());
        mockedResponseOutput = new ByteArrayOutputStream();
    }

    @Test
    @DisplayName("Endpoints http processing is working")
    void httpProcessingIsWorking() throws Exception {

        // Fake exchange
        when(mockedExchange.getRequestURI()).thenReturn(new URI("/fake"));
        when(mockedExchange.getRequestMethod()).thenReturn("GET");
        when(mockedExchange.getResponseBody()).thenReturn(mockedResponseOutput);
        when(mockedExchange.getResponseHeaders()).thenReturn(mockedResponseHeaders);

        // Collect fake exchange headers
        var capturedHeaders = new HashMap<String, String>();
        doAnswer(invocation -> {
            capturedHeaders.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(mockedResponseHeaders).set(anyString(), anyString());

        // Writing fake response in mocks
        FAKE_ENDPOINT.handle(mockedExchange);

        assertAll(
                () -> assertDoesNotThrow(() -> verify(mockedExchange).sendResponseHeaders(eq(200), anyLong())),
                () -> assertEquals("application/json", capturedHeaders.get("Content-Type")),
                () -> assertEquals("{\"message\":\"Hello world !\"}", mockedResponseOutput.toString())
        );
    }

    @Test
    @DisplayName("Not matched route return 404")
    void testHandleRouteNotMatched() throws Exception {

        when(mockedExchange.getRequestURI()).thenReturn(new URI("/Unknown"));
        when(mockedExchange.getResponseBody()).thenReturn(mockedResponseOutput);
        when(mockedExchange.getResponseHeaders()).thenReturn(mockedResponseHeaders);

        FAKE_ENDPOINT.handle(mockedExchange);

        assertAll(
                () -> assertDoesNotThrow(() -> verify(mockedExchange).sendResponseHeaders(eq(404), anyLong())),
                () -> assertEquals(
                        "{\"message\":\"The requested resource does not exist\"}",
                        mockedResponseOutput.toString()
                )
        );
    }

    private void testDefaultBehaviorForRequestMethod(String method) throws Exception {
        var emptyEndpoint = new Endpoint() {

            @Override
            public String route() {
                return "/empty";
            }

        };

        // Fake exchange
        when(mockedExchange.getRequestURI()).thenReturn(new URI("/empty"));
        when(mockedExchange.getRequestMethod()).thenReturn(method);
        when(mockedExchange.getResponseBody()).thenReturn(mockedResponseOutput);
        when(mockedExchange.getResponseHeaders()).thenReturn(mockedResponseHeaders);
        when(mockedExchange.getRequestBody()).thenReturn(mockedRequestInput);

        // Collect fake exchange headers
        var capturedHeaders = new HashMap<String, String>();
        doAnswer(invocation -> {
            capturedHeaders.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(mockedResponseHeaders).set(anyString(), anyString());

        // Writing fake response in mocks
        emptyEndpoint.handle(mockedExchange);

        assertAll(
                () -> assertDoesNotThrow(() -> verify(mockedExchange).sendResponseHeaders(eq(405), anyLong())),
                () -> assertEquals("application/json", capturedHeaders.get("Content-Type")),
                () -> assertEquals(
                        "{\"message\":\"The requested HTTP method is not allowed for this resource.\"}",
                        mockedResponseOutput.toString()
                )
        );
    }

    @Test
    @DisplayName("Default behavior for wrong request method is 405")
    void wrongMethod() throws Exception {
        testDefaultBehaviorForRequestMethod("UNKNOWN");
    }

    @Test
    @DisplayName("Default behavior for GET request method is 405")
    void wrongMethodGet() throws Exception {
        testDefaultBehaviorForRequestMethod("GET");
    }

    @Test
    @DisplayName("Default behavior for POST request method is 405")
    void wrongMethodPost() throws Exception {
        testDefaultBehaviorForRequestMethod("POST");
    }

}
