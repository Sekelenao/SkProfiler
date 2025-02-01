package io.github.sekelenao.skprofiler.http.dto.send;

public interface PaginatedResponse {

    int PAGE_SIZE = 500;

    PaginationDTO pagination();

}
