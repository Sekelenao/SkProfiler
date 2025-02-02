package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.dto.send.memory.FormattedBytesDTO;
import io.github.sekelenao.skprofiler.http.dto.send.memory.MemoryDTO;
import io.github.sekelenao.skprofiler.http.dto.send.memory.MemoryZoneDTO;
import io.github.sekelenao.skprofiler.system.EnvironmentProperties;
import io.github.sekelenao.skprofiler.util.Optionals;
import io.github.sekelenao.skprofiler.util.Units;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

final class TestMemoryEndpoint {

    @Test
    @DisplayName("Memory GET is working")
    void testMemoryGetEndpoint() {
        var heapMemoryInitialSize = 228589568L;
        long heapMemoryUsedSize = 53535640;
        long heapMemoryCommittedSize = 92274688;
        var heapMemoryMaxSize = 3649044480L;
        var nonHeapMemoryInitialSize = 7667712;
        long nonHeapMemoryUsedSize = 101915464;
        long nonHeapMemoryCommittedSize = 103874560;
        var memoryEndpoint = new MemoryEndpoint();
        try (MockedStatic<EnvironmentProperties> mockedStatic = mockStatic(EnvironmentProperties.class)) {
            mockedStatic.when(EnvironmentProperties::heapMemoryInitialSize)
                .thenReturn(OptionalLong.of(heapMemoryInitialSize));
            mockedStatic.when(EnvironmentProperties::heapMemoryUsedSize)
                .thenReturn(heapMemoryUsedSize);
            mockedStatic.when(EnvironmentProperties::heapMemoryCommittedSize)
                .thenReturn(heapMemoryCommittedSize);
            mockedStatic.when(EnvironmentProperties::heapMemoryMaxSize)
                .thenReturn(OptionalLong.of(heapMemoryMaxSize));
            mockedStatic.when(EnvironmentProperties::nonHeapMemoryInitialSize)
                .thenReturn(OptionalLong.of(nonHeapMemoryInitialSize));
            mockedStatic.when(EnvironmentProperties::nonHeapMemoryUsedSize)
                .thenReturn(nonHeapMemoryUsedSize);
            mockedStatic.when(EnvironmentProperties::nonHeapMemoryCommittedSize)
                .thenReturn(nonHeapMemoryCommittedSize);
            mockedStatic.when(EnvironmentProperties::nonHeapMemoryMaxSize)
                .thenReturn(OptionalLong.empty());
            assertAll(
                () -> assertEquals(
                    CustomHttpResponse.success(
                        new MemoryDTO(
                            new MemoryZoneDTO(
                                new FormattedBytesDTO(
                                    heapMemoryInitialSize,
                                    Units.bytesAsHumanReadable(heapMemoryInitialSize)
                                ),
                                new FormattedBytesDTO(
                                    heapMemoryUsedSize,
                                    Units.bytesAsHumanReadable(heapMemoryUsedSize)
                                ),
                                new FormattedBytesDTO(
                                    heapMemoryCommittedSize,
                                    Units.bytesAsHumanReadable(heapMemoryCommittedSize)
                                ),
                                new FormattedBytesDTO(
                                    heapMemoryMaxSize,
                                    Units.bytesAsHumanReadable(heapMemoryMaxSize)
                                )
                            ),
                            new MemoryZoneDTO(
                                new FormattedBytesDTO(
                                    nonHeapMemoryInitialSize,
                                    Units.bytesAsHumanReadable(nonHeapMemoryInitialSize)
                                ),
                                new FormattedBytesDTO(
                                    nonHeapMemoryUsedSize,
                                    Units.bytesAsHumanReadable(nonHeapMemoryUsedSize)
                                ),
                                new FormattedBytesDTO(
                                    nonHeapMemoryCommittedSize,
                                    Units.bytesAsHumanReadable(nonHeapMemoryCommittedSize)
                                ),
                                new FormattedBytesDTO(
                                    -1,
                                    Optionals.missingValueDescriptor()
                                )
                            )
                        )
                    ), memoryEndpoint.processGetRequest("")
                )
            );
        }
    }

}
