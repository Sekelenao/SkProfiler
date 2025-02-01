package io.github.sekelenao.skprofiler.http.dto.send.status;

import io.github.sekelenao.skprofiler.util.Assertions;

public record StatusDTO(String runningWithin, JavaDTO java, JavaVirtualMachineDTO jvm) {

    public StatusDTO {
        Assertions.requireNonNulls(runningWithin, java, jvm);
    }

}
