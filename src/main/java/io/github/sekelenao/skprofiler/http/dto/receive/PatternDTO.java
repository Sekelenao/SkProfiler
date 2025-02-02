package io.github.sekelenao.skprofiler.http.dto.receive;

import java.util.Objects;

public record PatternDTO(String pattern) {

    public PatternDTO {
        Objects.requireNonNull(pattern);
    }

}
