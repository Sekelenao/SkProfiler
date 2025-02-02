package io.github.sekelenao.skprofiler.http.dto.send.loadedclasses;

import io.github.sekelenao.skprofiler.http.dto.send.PaginationDTO;
import io.github.sekelenao.skprofiler.http.dto.send.PaginatedResponse;

import java.util.Objects;

public record LoadedClassesPageDTO(PaginationDTO pagination, Iterable<Class<?>> loadedClasses) implements PaginatedResponse {

    public LoadedClassesPageDTO {
        Objects.requireNonNull(pagination);
        Objects.requireNonNull(loadedClasses);
    }

}
