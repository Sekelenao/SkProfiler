package io.github.sekelenao.skprofiler.http.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.json.CustomJsonInterpreter;
import io.github.sekelenao.skprofiler.util.ByteStreams;
import io.github.sekelenao.skprofiler.util.Optionals;

import java.io.IOException;

public interface Endpoint extends HttpHandler {

    String route();

    default CustomHttpResponse processGetRequest(String requestQuery) {
        return CustomHttpResponse.methodNotAllowed();
    }

    default CustomHttpResponse processPostRequest(String requestQuery, String requestBody) {
        return CustomHttpResponse.methodNotAllowed();
    }

    default CustomHttpResponse processDeleteRequest(String requestQuery) {
        return CustomHttpResponse.methodNotAllowed();
    }

    @Override
    default void handle(HttpExchange exchange) throws IOException {
        var response = CustomHttpResponse.notFound();
        var uri = exchange.getRequestURI();
        if (uri.getPath().equals(route())) {
            var query = Optionals.emptyStringIfNull(uri.getQuery());
            response = CustomHttpResponse.safeProcess(() -> switch (exchange.getRequestMethod()) {
                case "GET" -> processGetRequest(query);
                case "POST" -> processPostRequest(
                    query, ByteStreams.readFromInputStream(exchange::getRequestBody)
                );
                case "DELETE" -> processDeleteRequest(query);
                default -> CustomHttpResponse.methodNotAllowed();
            }, route());
        }
        CustomHttpResponse.modifyHeaders(exchange.getResponseHeaders());
        var responseBody = response.body().map(CustomJsonInterpreter::serialize).orElse("").getBytes();
        exchange.sendResponseHeaders(response.status().code(), responseBody.length);
        ByteStreams.writeOnOutputStream(exchange::getResponseBody, responseBody);
    }

}
