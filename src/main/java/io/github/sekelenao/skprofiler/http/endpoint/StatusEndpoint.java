package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.dto.send.status.JavaDTO;
import io.github.sekelenao.skprofiler.http.dto.send.status.JavaVirtualMachineDTO;
import io.github.sekelenao.skprofiler.http.dto.send.status.StatusDTO;
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
                EnvironmentProperties.command().orElse(Optionals.missingValueDescriptor()),
                new JavaDTO(
                    EnvironmentProperties.javaVersion().orElse(Optionals.missingValueDescriptor()),
                    EnvironmentProperties.javaHome().orElse(Optionals.missingValueDescriptor())
                ),
                new JavaVirtualMachineDTO(
                    EnvironmentProperties.vmName().orElse(Optionals.missingValueDescriptor()),
                    EnvironmentProperties.vmVersion().orElse(Optionals.missingValueDescriptor()),
                    EnvironmentProperties.vmVendor().orElse(Optionals.missingValueDescriptor()),
                    Units.durationAsHumanReadable(EnvironmentProperties.vmUptime())
                )
            )
        );
    }

}