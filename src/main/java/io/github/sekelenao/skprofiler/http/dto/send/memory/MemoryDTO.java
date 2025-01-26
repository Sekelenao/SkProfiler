package io.github.sekelenao.skprofiler.http.dto.send.memory;

import io.github.sekelenao.skprofiler.util.Assertions;

public record MemoryDTO(MemoryZoneDTO heap, MemoryZoneDTO nonHeap) {

    public MemoryDTO {
        Assertions.requireNonNulls(heap, nonHeap);
    }

}
