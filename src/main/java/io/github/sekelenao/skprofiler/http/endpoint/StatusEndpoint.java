package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.HttpResponse;
import io.github.sekelenao.skprofiler.http.dto.send.StatusDTO;
import io.github.sekelenao.skprofiler.system.SystemProperties;

public final class StatusEndpoint implements Endpoint {

    @Override
    public String route() {
        return "/status";
    }

    @Override
    public HttpResponse processGetRequest() {
        return HttpResponse.success(
                new StatusDTO(
                        SystemProperties.command().orElse("Unknown"),
                        SystemProperties.javaVersion()
                )
        );
    }

}