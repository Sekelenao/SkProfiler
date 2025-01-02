package io.github.sekelenao.skprofiler.http;

import java.util.Objects;
import java.util.Optional;

public record HttpResponse(HttpStatus status, Optional<Record> body) {

    public HttpResponse {
        Objects.requireNonNull(status);
        Objects.requireNonNull(body);
    }

    public static HttpResponse notFound(){
        return new HttpResponse(HttpStatus.NOT_FOUND, Optional.empty());
    }

    public static HttpResponse success(Record body){
        Objects.requireNonNull(body);
        return new HttpResponse(HttpStatus.SUCCESS, Optional.of(body));
    }

}
