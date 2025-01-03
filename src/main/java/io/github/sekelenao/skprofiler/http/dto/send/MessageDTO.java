package io.github.sekelenao.skprofiler.http.dto.send;

import java.util.Objects;

public record MessageDTO(String message) {

    public MessageDTO {
        Objects.requireNonNull(message);
    }

}
