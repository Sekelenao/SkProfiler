package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.dto.send.JavaDTO;
import io.github.sekelenao.skprofiler.http.dto.send.JavaVirtualMachineDTO;
import io.github.sekelenao.skprofiler.http.dto.send.StatusDTO;
import io.github.sekelenao.skprofiler.system.EnvironmentProperties;
import io.github.sekelenao.skprofiler.util.Optionals;
import io.github.sekelenao.skprofiler.util.Units;

public final class StatusEndpoint implements Endpoint {

    @Override
    public String route() {
        return "/status";
    }

    @Override
    public CustomHttpResponse processGetRequest() {
        return CustomHttpResponse.success(
            new StatusDTO(
                Optionals.asStringOrMissingDescriptor(EnvironmentProperties.command()),
                new JavaDTO(
                    Optionals.asStringOrMissingDescriptor(EnvironmentProperties.javaVersion()),
                    Optionals.asStringOrMissingDescriptor(EnvironmentProperties.javaHome())
                ),
                new JavaVirtualMachineDTO(
                    Optionals.asStringOrMissingDescriptor(EnvironmentProperties.vmName()),
                    Optionals.asStringOrMissingDescriptor(EnvironmentProperties.vmVersion()),
                    Optionals.asStringOrMissingDescriptor(EnvironmentProperties.vmVendor()),
                    Units.durationAsHumanReadable(EnvironmentProperties.vmUptime())
                )
            )
        );
    }

}