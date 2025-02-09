package io.github.sekelenao.skprofiler.http;

import com.sun.net.httpserver.HttpServer;
import io.github.sekelenao.skprofiler.http.endpoint.Endpoint;
import io.github.sekelenao.skprofiler.log.CustomLogger;
import io.github.sekelenao.skprofiler.util.Assertions;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public final class CustomHttpServer {

    private static final CustomLogger LOGGER = CustomLogger.on(CustomHttpServer.class);

    private volatile boolean stopped = false;

    private final HttpServer server;

    private CustomHttpServer(HttpServer server) {
        this.server = Objects.requireNonNull(server);
    }

    public static int parsePort(String portAsString) {
        if(portAsString == null || portAsString.isBlank()){
            LOGGER.severe("No port was provided for the HttpServer. Please read documentation for more information.");
            throw new IllegalArgumentException();
        }
        try {
            var port = Integer.parseInt(portAsString);
            Assertions.checkPort(port);
            return port;
        } catch (IllegalArgumentException exception) {
            LOGGER.severe("Provided port is not a valid port. Encountered the following Exception: {0}", exception);
            throw new IllegalArgumentException();
        }
    }

    public static CustomHttpServer bind(int port) throws IOException {
        Assertions.checkPort(port);
        var server = HttpServer.create(new InetSocketAddress(port), 0);
        return new CustomHttpServer(server);
    }

    public CustomHttpServer with(Endpoint endpoint){
        Objects.requireNonNull(endpoint);
        server.createContext(endpoint.route(), endpoint);
        return this;
    }

    public CustomHttpServer start(){
        server.start();
        stopped = false;
        LOGGER.info(
                "HttpServer started on port: {0}",
                String.valueOf(server.getAddress().getPort())
        );
        return this;
    }

    public int port(){
        return server.getAddress().getPort();
    }

    public void stop(){
        if(!stopped){
            stopped = true;
            var stopTask = new TimerTask() {
                @Override
                public void run() {
                    server.stop(5);
                    LOGGER.info("HttpServer stopped successfully");
                }
            };
            new Timer().schedule(
                    stopTask,
                    Duration.ofSeconds(5).toMillis()
            );
        }
    }

}
