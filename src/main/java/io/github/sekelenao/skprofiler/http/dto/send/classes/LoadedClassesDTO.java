package io.github.sekelenao.skprofiler.http.dto.send.classes;

import io.github.sekelenao.skprofiler.http.dto.send.PageInfoDTO;
import io.github.sekelenao.skprofiler.http.response.PaginatedResponse;
import io.github.sekelenao.skprofiler.util.Assertions;

import java.util.List;
import java.util.Objects;

public record LoadedClassesDTO(PageInfoDTO pagination, List<Class<?>> loadedClasses) implements PaginatedResponse {

    public LoadedClassesDTO {
        Objects.requireNonNull(pagination);
        Objects.requireNonNull(loadedClasses);
        Assertions.isLowerOrEqualThan(
            loadedClasses.size(),
            PaginatedResponse.MAX_PAGE_SIZE
        );
    }

}
