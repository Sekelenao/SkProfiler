package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.dto.send.JavaDTO;
import io.github.sekelenao.skprofiler.http.dto.send.JavaVirtualMachineDTO;
import io.github.sekelenao.skprofiler.http.dto.send.StatusDTO;
import io.github.sekelenao.skprofiler.system.EnvironmentProperties;
import io.github.sekelenao.skprofiler.util.Units;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Duration;
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
        var javaHome = "/java/home";
        var vmName = "Java HotSpot(TM) 64-Bit Server VM";
        var vmVersion = "25.255-b02";
        var vmVendor = "Oracle Corporation";
        var duration = Duration.ofMillis(15588854684464L);
        var statusEndpoint = new StatusEndpoint();
        try (MockedStatic<EnvironmentProperties> mockedStatic = mockStatic(EnvironmentProperties.class)) {
            mockedStatic.when(EnvironmentProperties::command).thenReturn(Optional.of(command));
            mockedStatic.when(EnvironmentProperties::javaVersion).thenReturn(Optional.of(javaVersion));
            mockedStatic.when(EnvironmentProperties::javaHome).thenReturn(Optional.of(javaHome));
            mockedStatic.when(EnvironmentProperties::vmName).thenReturn(Optional.of(vmName));
            mockedStatic.when(EnvironmentProperties::vmVersion).thenReturn(Optional.of(vmVersion));
            mockedStatic.when(EnvironmentProperties::vmVendor).thenReturn(Optional.of(vmVendor));
            mockedStatic.when(EnvironmentProperties::vmUptime).thenReturn(duration);
            assertAll(
                    () -> assertEquals(
                            CustomHttpResponse.success(
                                    new StatusDTO(
                                            command,
                                            new JavaDTO(
                                                    javaVersion, javaHome
                                            ),
                                            new JavaVirtualMachineDTO(
                                                    vmName,
                                                    vmVersion,
                                                    vmVendor,
                                                    Units.durationAsHumanReadable(duration)
                                            )
                                    )
                            ),
                            statusEndpoint.processGetRequest()
                    ),
                    () -> {
                        mockedStatic.when(EnvironmentProperties::command).thenReturn(Optional.empty());
                        assertEquals(
                                CustomHttpResponse.success(
                                        new StatusDTO(
                                                "Unknown",
                                                new JavaDTO(
                                                    javaVersion, javaHome
                                                ),
                                                new JavaVirtualMachineDTO(
                                                        vmName,
                                                        vmVersion,
                                                        vmVendor,
                                                        Units.durationAsHumanReadable(duration)
                                                )
                                        )
                                ), statusEndpoint.processGetRequest());
                    }
            );
        }
    }


}
