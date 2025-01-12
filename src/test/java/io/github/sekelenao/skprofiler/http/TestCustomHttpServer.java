package io.github.sekelenao.skprofiler.http;

import io.github.sekelenao.skprofiler.http.endpoint.StatusEndpoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

    @Test
    @DisplayName("CustomHttpServer is starting then stopping")
    void customHttpServerIsStartingThenStopping() throws IOException {
        var server = CustomHttpServer.bind(0).with(new StatusEndpoint());
        assertDoesNotThrow(server::start);
        try (var client = java.net.http.HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + server.port() + "/status"))
                    .GET()
                    .build();
            var response = assertDoesNotThrow(
                    () -> client.send(request, HttpResponse.BodyHandlers.ofString())
            );
            assertAll(
                    () -> assertEquals(200, response.statusCode()),
                    () -> assertNotNull(response.body()),
                    () -> assertFalse(response.body().isEmpty()),
                    () -> assertDoesNotThrow(server::stop)
            );
        }
    }

}
