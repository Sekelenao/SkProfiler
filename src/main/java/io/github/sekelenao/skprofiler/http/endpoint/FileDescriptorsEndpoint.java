package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.dto.send.file.FileDescriptorsDTO;
import io.github.sekelenao.skprofiler.http.response.CustomHttpResponse;
import io.github.sekelenao.skprofiler.system.EnvironmentProperties;
import io.github.sekelenao.skprofiler.util.Optionals;

public final class FileDescriptorsEndpoint implements Endpoint {

    @Override
    public String route() {
        return "/file-descriptors";
    }

    @Override
    public CustomHttpResponse processGetRequest(String requestQuery) {
        var open = EnvironmentProperties.openFileDescriptorCount().orElse(-1);
        var max = EnvironmentProperties.maxFileDescriptorCount().orElse(-1);
        return CustomHttpResponse.success(
            new FileDescriptorsDTO(
                open != -1 ? String.valueOf(open) : Optionals.missingValueDescriptor(),
                max != -1 ? String.valueOf(max) : Optionals.missingValueDescriptor()
            )
        );
    }
}
