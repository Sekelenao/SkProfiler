package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.dto.send.file.FileDescriptorsDTO;
import io.github.sekelenao.skprofiler.http.response.CustomHttpResponse;
import io.github.sekelenao.skprofiler.system.EnvironmentProperties;

public final class FileDescriptorsEndpoint implements Endpoint {

    @Override
    public String route() {
        return "/file-descriptors";
    }

    @Override
    public CustomHttpResponse processGetRequest(String requestQuery) {
        return CustomHttpResponse.success(
            new FileDescriptorsDTO(
                EnvironmentProperties.openFileDescriptorCount().orElse(-1),
                EnvironmentProperties.maxFileDescriptorCount().orElse(-1)
            )
        );
    }
}
