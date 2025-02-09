package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.response.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.response.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

final class TestSelfDestructEndpoint {

    @Test
    @DisplayName("Should return success response and stop action should be executed when processDeleteRequest is called")
    void shouldReturnSuccessResponseAndExecuteStopAction() {
        Runnable stopActionMock = mock(Runnable.class);
        SelfDestructEndpoint endpoint = new SelfDestructEndpoint(stopActionMock);
        CustomHttpResponse response = endpoint.processDeleteRequest("");
        assertAll(
            () -> verify(stopActionMock, times(1)).run(),
            () -> assertNotNull(response),
            () -> assertEquals(HttpStatus.SUCCESS, response.status())
        );
    }

    @Test
    @DisplayName("Should throw NullPointerException when instantiated with null stopAction")
    void shouldThrowExceptionWhenStopActionIsNull() {
        assertThrows(NullPointerException.class, () -> new SelfDestructEndpoint(null));
    }
}