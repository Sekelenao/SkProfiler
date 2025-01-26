package io.github.sekelenao.skprofiler.http.dto.send.memory;

import io.github.sekelenao.skprofiler.util.Assertions;

public record MemoryZoneDTO(FormattedBytesDTO initial, FormattedBytesDTO used,
                            FormattedBytesDTO committed, FormattedBytesDTO max) {

    public MemoryZoneDTO {
        Assertions.requireNonNulls(initial, max, used, committed);
    }

}