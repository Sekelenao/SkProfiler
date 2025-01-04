package io.github.sekelenao.skprofiler;

import io.github.sekelenao.skprofiler.http.CustomHttpServer;
import io.github.sekelenao.skprofiler.http.endpoint.StatusEndpoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.instrument.Instrumentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

final class TestSkAgent {

    private static final String ARGUMENTS = "8081";

    @Mock private Instrumentation mockedInstrumentation;

    @Test
    @DisplayName("Application is starting")
    void testPremainStartsApplication() {
        var mockedHttpServer = Mockito.mock(CustomHttpServer.class);
        try (MockedStatic<CustomHttpServer> mockedStatic = mockStatic(CustomHttpServer.class)) {
            mockedStatic.when(() -> CustomHttpServer.bind(anyInt()))
                    .thenReturn(mockedHttpServer);
            when(mockedHttpServer.with(any())).thenReturn(mockedHttpServer);
            SkAgent.premain(ARGUMENTS, mockedInstrumentation);
            verify(mockedHttpServer, times(1)).with(any(StatusEndpoint.class));
            verify(mockedHttpServer, times(1)).start();
        }
    }

}
