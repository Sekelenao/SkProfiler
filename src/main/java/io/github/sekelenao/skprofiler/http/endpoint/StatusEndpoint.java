package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.HttpStatus;
import io.github.sekelenao.skprofiler.http.dto.StatusDTO;

import java.io.InputStream;

public final class StatusEndpoint implements Endpoint {

    @Override
    public String route() {
        return "/status";
    }

    @Override
    public BodyResponse process(InputStream requestBody) {
        return new BodyResponse(HttpStatus.SUCCESS, new StatusDTO(true));
    }

}
