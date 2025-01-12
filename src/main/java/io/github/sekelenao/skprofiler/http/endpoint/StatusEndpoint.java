package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.dto.send.JavaDTO;
import io.github.sekelenao.skprofiler.http.dto.send.JavaVirtualMachineDTO;
import io.github.sekelenao.skprofiler.http.dto.send.StatusDTO;
import io.github.sekelenao.skprofiler.system.EnvironmentProperties;
import io.github.sekelenao.skprofiler.util.Durations;

public final class StatusEndpoint implements Endpoint {

    private static final String MISSING_INFORMATION = "Unknown";

    @Override
    public String route() {
        return "/status";
    }

    @Override
    public CustomHttpResponse processGetRequest() {
        return CustomHttpResponse.success(
                new StatusDTO(
                        EnvironmentProperties.command().orElse(MISSING_INFORMATION),
                        new JavaDTO(
                                EnvironmentProperties.javaVersion().orElse(MISSING_INFORMATION),
                                EnvironmentProperties.javaHome().orElse(MISSING_INFORMATION)
                        ),
                        new JavaVirtualMachineDTO(
                                EnvironmentProperties.vmName().orElse(MISSING_INFORMATION),
                                EnvironmentProperties.vmVersion().orElse(MISSING_INFORMATION),
                                EnvironmentProperties.vmVendor().orElse(MISSING_INFORMATION),
                                Durations.asHumanReadable(EnvironmentProperties.vmUptime())
                        )
                )
        );
    }

}