package io.github.sekelenao.skprofiler.http.dto.send;

import java.util.Objects;

public record StatusDTO(String runningWithin, String javaVersion) {

    public StatusDTO {
        Objects.requireNonNull(javaVersion);
        Objects.requireNonNull(runningWithin);
    }

}
