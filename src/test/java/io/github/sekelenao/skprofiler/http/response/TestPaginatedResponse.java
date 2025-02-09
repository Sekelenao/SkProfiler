package io.github.sekelenao.skprofiler.http.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class TestPaginatedResponse {

    @Test
    @SuppressWarnings("ConstantConditions")
    @DisplayName("Max page size is a power of two")
    void maxPageSizeIsAPowerOfTwo() {
        int maxPageSize = PaginatedResponse.MAX_PAGE_SIZE;
        assertEquals(0, (maxPageSize & (maxPageSize - 1)));
    }

}
