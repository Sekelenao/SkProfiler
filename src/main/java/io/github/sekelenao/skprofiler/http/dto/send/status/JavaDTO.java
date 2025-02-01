package io.github.sekelenao.skprofiler.http.dto.send.status;

import java.util.Objects;

public record JavaDTO(String version, String home) {

    public JavaDTO {
        Objects.requireNonNull(version);
        Objects.requireNonNull(home);
    }

}
