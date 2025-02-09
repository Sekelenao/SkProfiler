package io.github.sekelenao.skprofiler.http.response;

import io.github.sekelenao.skprofiler.annotation.marker.PowerOfTwo;
import io.github.sekelenao.skprofiler.http.dto.send.PageInfoDTO;

public interface PaginatedResponse {

    @PowerOfTwo
    int MAX_PAGE_SIZE = 512;

    PageInfoDTO pagination();

}
