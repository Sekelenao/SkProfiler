package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.HttpResponse;
import io.github.sekelenao.skprofiler.http.dto.send.StatusDTO;

public final class StatusEndpoint implements Endpoint {

    @Override
    public String route() {
        return "/status";
    }

    @Override
    public HttpResponse processGetRequest() {
        return HttpResponse.success(new StatusDTO(true));
    }

}