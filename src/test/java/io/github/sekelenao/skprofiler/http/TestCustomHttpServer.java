package io.github.sekelenao.skprofiler.http;

import io.github.sekelenao.skprofiler.http.endpoint.StatusEndpoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
    void test() throws IOException {
        var server = CustomHttpServer.bind(8081).with(new StatusEndpoint());
        assertDoesNotThrow(server::start);
        assertDoesNotThrow(server::stop);
    }

}
