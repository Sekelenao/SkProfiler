package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.response.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.dto.send.memory.FormattedBytesDTO;
import io.github.sekelenao.skprofiler.http.dto.send.memory.MemoryDTO;
import io.github.sekelenao.skprofiler.http.dto.send.memory.MemoryZoneDTO;
import io.github.sekelenao.skprofiler.system.EnvironmentProperties;

public final class MemoryEndpoint implements Endpoint {

    @Override
    public String route() {
        return "/memory";
    }

    @Override
    public CustomHttpResponse processGetRequest(String requestQuery) {
        return CustomHttpResponse.success(
            new MemoryDTO(
                new MemoryZoneDTO(
                    FormattedBytesDTO.of(EnvironmentProperties.heapMemoryInitialSize().orElse(-1)),
                    FormattedBytesDTO.of(EnvironmentProperties.heapMemoryUsedSize()),
                    FormattedBytesDTO.of(EnvironmentProperties.heapMemoryCommittedSize()),
                    FormattedBytesDTO.of(EnvironmentProperties.heapMemoryMaxSize().orElse(-1))
                ),
                new MemoryZoneDTO(
                    FormattedBytesDTO.of(EnvironmentProperties.nonHeapMemoryInitialSize().orElse(-1)),
                    FormattedBytesDTO.of(EnvironmentProperties.nonHeapMemoryUsedSize()),
                    FormattedBytesDTO.of(EnvironmentProperties.nonHeapMemoryCommittedSize()),
                    FormattedBytesDTO.of(EnvironmentProperties.nonHeapMemoryMaxSize().orElse(-1))
                )
            )
        );
    }

}
