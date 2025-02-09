package io.github.sekelenao.skprofiler.http.dto.send.status;

import io.github.sekelenao.skprofiler.util.Assertions;

public record JavaVirtualMachineDTO(String name, String version, String vendor, String uptime, long pid) {

    public JavaVirtualMachineDTO {
        Assertions.requireNonNulls(name, version, vendor, uptime);
    }

}
