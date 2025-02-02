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

    default CustomHttpResponse processPostRequest(String requestBody){
        return CustomHttpResponse.methodNotAllowed();
    }

    default CustomHttpResponse processDeleteRequest(){
        return CustomHttpResponse.methodNotAllowed();
    }

    @Override
    default void handle(HttpExchange exchange) throws IOException {
        var response = CustomHttpResponse.notFound();
        var uri = exchange.getRequestURI();
        if(uri.getPath().equals(route())){
            response = CustomHttpResponse.safeProcess(() -> switch (exchange.getRequestMethod()){
                case "GET" -> processGetRequest(Optionals.emptyStringIfNull(uri.getQuery()));
                case "POST" -> processPostRequest(
                        ByteStreams.readFromInputStream(exchange::getRequestBody)
                );
                case "DELETE" -> processDeleteRequest();
                default -> CustomHttpResponse.methodNotAllowed();
            }, route());
        }
        CustomHttpResponse.modifyHeaders(exchange.getResponseHeaders());
        var responseBody = response.body().map(CustomJsonInterpreter::serialize).orElse("").getBytes();
        exchange.sendResponseHeaders(response.status().code(), responseBody.length);
        ByteStreams.writeOnOutputStream(exchange::getResponseBody, responseBody);
    }

}
