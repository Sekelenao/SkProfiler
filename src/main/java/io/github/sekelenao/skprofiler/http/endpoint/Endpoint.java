package io.github.sekelenao.skprofiler.http.endpoint;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.sekelenao.skprofiler.http.HttpResponse;
import io.github.sekelenao.skprofiler.json.CustomJsonInterpreter;
import io.github.sekelenao.skprofiler.util.ByteStreams;

import java.io.IOException;

public interface Endpoint extends HttpHandler {

    String route();

    default HttpResponse processGetRequest() {
        return HttpResponse.methodNotAllowed();
    }

    default HttpResponse processPostRequest(String requestBody){
        return HttpResponse.methodNotAllowed();
    }

    @Override
    default void handle(HttpExchange exchange) throws IOException {
        var response = HttpResponse.notFound();
        if(exchange.getRequestURI().getPath().equals(route())){
            response = switch (exchange.getRequestMethod()){
                case "GET" -> processGetRequest();
                case "POST" -> processPostRequest(
                        ByteStreams.readFromInputStream(exchange::getRequestBody)
                );
                default -> HttpResponse.methodNotAllowed();
            };
        }
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, PUT");
        var responseBody = response.body().map(CustomJsonInterpreter::serialize).orElse("").getBytes();
        exchange.sendResponseHeaders(response.status().code(), responseBody.length);
        ByteStreams.writeOnOutputStream(exchange::getResponseBody, responseBody);
    }

}
