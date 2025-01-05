package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.dto.send.StatusDTO;
import io.github.sekelenao.skprofiler.system.SystemProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

final class TestStatusEndpoint {

    @Test
    @DisplayName("Status GET is working")
    void testStatusGetEndpoint() {
        var command = "ApplicationTest.jar";
        var javaVersion = "1.0.0-SNAPSHOT";
        var statusEndpoint = new StatusEndpoint();
        try (MockedStatic<SystemProperties> mockedStatic = mockStatic(SystemProperties.class)) {
            mockedStatic.when(SystemProperties::command).thenReturn(Optional.of(command));
            mockedStatic.when(SystemProperties::javaVersion).thenReturn(javaVersion);
            assertAll(
                    () -> assertEquals(
                            CustomHttpResponse.success(new StatusDTO(command, javaVersion)),
                            statusEndpoint.processGetRequest()
                    ),
                    () -> {
                        mockedStatic.when(SystemProperties::command).thenReturn(Optional.empty());
                        assertEquals(
                                CustomHttpResponse.success(new StatusDTO("Unknown", javaVersion)),
                                statusEndpoint.processGetRequest()
                        );
                    }
            );
        }
    }


}
