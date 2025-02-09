package io.github.sekelenao.skprofiler.http.response;

import io.github.sekelenao.skprofiler.http.dto.send.PageInfoDTO;

public interface PaginatedResponse {

    int MAX_PAGE_SIZE = 512;

    PageInfoDTO pagination();

}
