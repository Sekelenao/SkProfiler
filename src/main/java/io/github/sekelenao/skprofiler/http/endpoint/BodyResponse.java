package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.HttpStatus;

import java.util.Objects;

public record BodyResponse(HttpStatus status, Record body) {

    public BodyResponse {
        Objects.requireNonNull(status);
        Objects.requireNonNull(body);
    }

}
