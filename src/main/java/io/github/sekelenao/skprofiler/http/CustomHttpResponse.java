package io.github.sekelenao.skprofiler.http;

import com.sun.net.httpserver.Headers;
import io.github.sekelenao.skprofiler.http.dto.send.MessageDTO;
import io.github.sekelenao.skprofiler.log.CustomLogger;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public record CustomHttpResponse(HttpStatus status, Optional<Record> body) {

    private static final CustomLogger LOGGER = CustomLogger.on(CustomHttpServer.class);

    public CustomHttpResponse {
        Objects.requireNonNull(status);
        Objects.requireNonNull(body);
    }

    public static void modifyHeaders(Headers headers){
        Objects.requireNonNull(headers);
        headers.set("Content-Type", "application/json");
        headers.set("Access-Control-Allow-Methods", "GET, PUT, DELETE");
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

    public static CustomHttpResponse badRequest(String message){
        Objects.requireNonNull(message);
        return new CustomHttpResponse(HttpStatus.BAD_REQUEST,
            Optional.of(
                new MessageDTO(message)
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

    public static CustomHttpResponse internalServerError(){
        return new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                Optional.of(
                        new MessageDTO(
                                "An internal error occurred during processing"
                        )
                )
        );
    }

    public static CustomHttpResponse safeProcess(RequestProcessor processor, String route){
        Objects.requireNonNull(processor);
        try {
            return processor.response();
        } catch (Exception exception) {
            LOGGER.warning(
                    "Encountered Exception during request processing on {0}: {1}{2}",
                    route,
                    exception,
                    Arrays.stream(exception.getStackTrace())
                            .limit(15)
                            .map(StackTraceElement::toString)
                            .collect(Collectors.joining("\n\tat ", "\n\tat ", "\n\t..."))
            );
            return CustomHttpResponse.internalServerError();
        }
    }

}
