package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.response.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.CustomHttpServer;
import io.github.sekelenao.skprofiler.http.dto.send.MessageDTO;
import io.github.sekelenao.skprofiler.log.CustomLogger;

import java.util.Objects;

public final class SelfDestructEndpoint implements Endpoint {

    private static final CustomLogger LOGGER = CustomLogger.on(CustomHttpServer.class);

    private final Runnable stopAction;

    public SelfDestructEndpoint(Runnable stopAction) {
        Objects.requireNonNull(stopAction);
        this.stopAction = stopAction;
    }

    @Override
    public String route() {
        return "/self-destruct";
    }

    @Override
    public CustomHttpResponse processDeleteRequest(String requestQuery) {
        LOGGER.info("Self-destruct route was called, stopping the agent in a few seconds");
        stopAction.run();
        return CustomHttpResponse.success(
                new MessageDTO("The agent will stop in a few seconds")
        );
    }

}
