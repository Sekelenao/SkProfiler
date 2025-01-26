package io.github.sekelenao.skprofiler;

import io.github.sekelenao.skprofiler.http.CustomHttpServer;
import io.github.sekelenao.skprofiler.http.endpoint.HomeEndpoint;
import io.github.sekelenao.skprofiler.http.endpoint.MemoryEndpoint;
import io.github.sekelenao.skprofiler.http.endpoint.SelfDestructEndpoint;
import io.github.sekelenao.skprofiler.http.endpoint.StatusEndpoint;
import io.github.sekelenao.skprofiler.log.CustomLogger;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public final class SkAgent {

    private SkAgent() {
        throw new AssertionError("You cannot instantiate this class");
    }

    private static final CustomLogger LOGGER = CustomLogger.on(SkAgent.class);

    public static void premain(String arguments, Instrumentation instrumentation) {
        try {
            CustomLogger.displayBannerAndStartingLogs(LOGGER);
            var port = CustomHttpServer.parsePort(arguments);
            var server = CustomHttpServer.bind(port);
            server.with(new HomeEndpoint())
                .with(new StatusEndpoint())
                .with(new SelfDestructEndpoint(server::stop))
                .with(new MemoryEndpoint())
                .start();
        } catch (IOException exception) {
            LOGGER.severe("Encountered IOException during starting process: " + exception);
            throw new AssertionError(exception);
        }
    }

}
