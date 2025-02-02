package io.github.sekelenao.skprofiler.http;

import io.github.sekelenao.skprofiler.http.dto.send.MessageDTO;
import io.github.sekelenao.skprofiler.http.endpoint.Endpoint;
import io.github.sekelenao.skprofiler.http.endpoint.StatusEndpoint;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

final class TestCustomHttpServer {

    @Test
    @DisplayName("Port parsing is working")
    void testPortParsing() {
        assertAll(
                () -> assertEquals(8081, CustomHttpServer.parsePort("8081")),
                () -> assertThrows(IllegalArgumentException.class, () -> CustomHttpServer.parsePort(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> CustomHttpServer.parsePort("")),
                () -> assertThrows(IllegalArgumentException.class, () -> CustomHttpServer.parsePort("-1")),
                () -> assertThrows(IllegalArgumentException.class, () -> CustomHttpServer.parsePort("4585654")),
                () -> assertThrows(IllegalArgumentException.class, () -> CustomHttpServer.parsePort("8081F"))
        );
    }

    private static boolean portNotReachable(int port) {
        try (Socket _ = new Socket("localhost", port)){
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    @Test
    @DisplayName("CustomHttpServer is starting then stopping")
    void customHttpServerIsStartingThenStopping() throws IOException {
        var endpoint = new StatusEndpoint();
        var server = CustomHttpServer.bind(0).with(endpoint);
        assertDoesNotThrow(server::start);
        try (var client = java.net.http.HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + server.port() + endpoint.route()))
                    .GET()
                    .build();
            var response = assertDoesNotThrow(
                    () -> client.send(request, HttpResponse.BodyHandlers.ofString())
            );
            assertAll(
                    () -> assertEquals(200, response.statusCode()),
                    () -> assertNotNull(response.body()),
                    () -> assertFalse(response.body().isEmpty()),
                    () -> assertDoesNotThrow(server::stop),
                    () -> Awaitility.await().atMost(10, TimeUnit.SECONDS).until(
                            () -> portNotReachable(server.port())
                    )
            );
        }
    }

    @Test
    @DisplayName("Exception in endpoint is converted to InternalServerError")
    void exceptionInControllerIsStopped() throws IOException {
        var endpoint = new Endpoint(){

            @Override
            public String route() {
                return "/exception";
            }

            @Override
            @SuppressWarnings("all")
            public CustomHttpResponse processGetRequest(String requestQuery) {
                Integer.parseInt("impossible");
                return CustomHttpResponse.success(new MessageDTO("Exception"));
            }

        };
        var server = CustomHttpServer.bind(0).with(endpoint);
        assertDoesNotThrow(server::start);
        try (var client = java.net.http.HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + server.port() + endpoint.route()))
                    .GET()
                    .build();

            var response = assertDoesNotThrow(
                    () -> client.send(request, HttpResponse.BodyHandlers.ofString())
            );
            assertAll(
                    () -> assertEquals(500, response.statusCode()),
                    () -> assertNotNull(response.body()),
                    () -> assertFalse(response.body().isEmpty()),
                    () -> assertDoesNotThrow(server::stop)
            );
        }
    }

}
