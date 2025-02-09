package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.dto.send.file.FileDescriptorsDTO;
import io.github.sekelenao.skprofiler.system.EnvironmentProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

final class TestFileDescriptorsEndpoint {

    @Test
    @DisplayName("Should return success response with valid open and max file descriptor counts")
    void shouldReturnSuccessResponseWithValidFileDescriptorCounts() {
        try (MockedStatic<EnvironmentProperties> mockedEnvironmentProperties = mockStatic(EnvironmentProperties.class)) {
            mockedEnvironmentProperties.when(EnvironmentProperties::openFileDescriptorCount)
                .thenReturn(OptionalLong.of(100));
            mockedEnvironmentProperties.when(EnvironmentProperties::maxFileDescriptorCount)
                .thenReturn(OptionalLong.of(200));
            var endpoint = new FileDescriptorsEndpoint();
            var response = endpoint.processGetRequest("");
            assertAll(
                () -> assertNotNull(response.body()),
                () -> assertTrue(response.body().isPresent()),
                () -> assertTrue(
                    response.body().orElseThrow() instanceof FileDescriptorsDTO(long opened, long max)
                        && opened == 100
                        && max == 200
                )
            );
        }
    }

    @Test
    @DisplayName("Should return success response with missing value descriptors when counts are unavailable")
    void shouldReturnSuccessResponseWhenCountsAreUnavailable() {
        try (MockedStatic<EnvironmentProperties> mockedEnvironmentProperties = mockStatic(EnvironmentProperties.class)) {
            mockedEnvironmentProperties.when(EnvironmentProperties::openFileDescriptorCount)
                .thenReturn(OptionalLong.empty());
            mockedEnvironmentProperties.when(EnvironmentProperties::maxFileDescriptorCount)
                .thenReturn(OptionalLong.empty());
            var endpoint = new FileDescriptorsEndpoint();
            var response = endpoint.processGetRequest("");
            assertAll(
                () -> assertNotNull(response.body()),
                () -> assertTrue(response.body().isPresent()),
                () -> assertTrue(
                    response.body().orElseThrow() instanceof FileDescriptorsDTO(long opened, long max)
                        && opened == -1
                        && max == -1
                )
            );
        }
    }

}