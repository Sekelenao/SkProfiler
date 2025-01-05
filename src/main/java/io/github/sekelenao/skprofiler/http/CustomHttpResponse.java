package io.github.sekelenao.skprofiler.http;

import com.sun.net.httpserver.Headers;
import io.github.sekelenao.skprofiler.http.dto.send.MessageDTO;

import java.util.Objects;
import java.util.Optional;

public record CustomHttpResponse(HttpStatus status, Optional<Record> body) {

    public CustomHttpResponse {
        Objects.requireNonNull(status);
        Objects.requireNonNull(body);
    }

    public static void modifyHeaders(Headers headers){
        Objects.requireNonNull(headers);
        headers.set("Content-Type", "application/json");
        headers.set("Access-Control-Allow-Methods", "GET, PUT");
    }

    public static CustomHttpResponse success(Record body){
        Objects.requireNonNull(body);
        return new CustomHttpResponse(HttpStatus.SUCCESS, Optional.of(body));
    }

    public static CustomHttpResponse badRequest(){
        return new CustomHttpResponse(HttpStatus.BAD_REQUEST,
                Optional.of(
                        new MessageDTO("Invalid parameters or malformed request, please check documentation")
                )
        );
    }

    public static CustomHttpResponse notFound(){
        return new CustomHttpResponse(HttpStatus.NOT_FOUND,
                Optional.of(
                        new MessageDTO(
                                "The requested resource does not exist"
                        )
                )
        );
    }

    public static CustomHttpResponse methodNotAllowed(){
        return new CustomHttpResponse(HttpStatus.METHOD_NOT_ALLOWED,
                Optional.of(
                        new MessageDTO(
                                "The requested HTTP method is not allowed for this resource"
                        )
                )
        );
    }

}
