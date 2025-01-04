package io.github.sekelenao.skprofiler.http;

import io.github.sekelenao.skprofiler.http.dto.send.MessageDTO;

import java.util.Objects;
import java.util.Optional;

public record HttpResponse(HttpStatus status, Optional<Record> body) {

    public HttpResponse {
        Objects.requireNonNull(status);
        Objects.requireNonNull(body);
    }

    public static HttpResponse notFound(){
        return new HttpResponse(HttpStatus.NOT_FOUND,
                Optional.of(
                        new MessageDTO(
                                "The requested resource does not exist"
                        )
                )
        );
    }

    public static HttpResponse methodNotAllowed(){
        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED,
                Optional.of(
                        new MessageDTO(
                                "The requested HTTP method is not allowed for this resource."
                        )
                )
        );
    }

    public static HttpResponse success(Record body){
        Objects.requireNonNull(body);
        return new HttpResponse(HttpStatus.SUCCESS, Optional.of(body));
    }

}
