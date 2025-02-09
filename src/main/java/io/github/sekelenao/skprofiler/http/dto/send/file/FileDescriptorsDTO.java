package io.github.sekelenao.skprofiler.http.dto.send.file;

import io.github.sekelenao.skprofiler.util.Assertions;

public record FileDescriptorsDTO(String opened, String max) {

    public FileDescriptorsDTO {
        Assertions.requireNonNulls(opened, max);
    }

}
