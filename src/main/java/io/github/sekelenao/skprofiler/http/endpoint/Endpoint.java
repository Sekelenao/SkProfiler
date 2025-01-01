package io.github.sekelenao.skprofiler.http.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.sekelenao.skprofiler.json.CustomJsonInterpreter;

import java.io.IOException;
import java.io.InputStream;

public interface Endpoint extends HttpHandler {

    String route();

    BodyResponse process(InputStream requestBody);

    @Override
    default void handle(HttpExchange exchange) throws IOException {
        var response = process(exchange.getRequestBody());
        var body = CustomJsonInterpreter.serialize(response.body()).getBytes();
        exchange.sendResponseHeaders(response.status().code(), body.length);
        try (var outputStream = exchange.getResponseBody()) {
            outputStream.write(body);
        }
    }

}
