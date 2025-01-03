package io.github.sekelenao.skprofiler;

import io.github.sekelenao.skprofiler.http.CustomHttpServer;
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
            CustomHttpServer.bind(port)
                    .with(new StatusEndpoint())
                    .start();
        } catch (IOException exception) {
            LOGGER.severe("Encountered IOException: " + exception);
            throw new IllegalStateException(exception);
        }
    }

}
