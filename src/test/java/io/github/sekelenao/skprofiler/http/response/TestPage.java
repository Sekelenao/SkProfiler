package io.github.sekelenao.skprofiler.http.response;

import io.github.sekelenao.skprofiler.exception.PaginationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class TestPage {

    @Test
    @DisplayName("Should create a empty ResultsPage with valid inputs")
    void emptyPage() throws PaginationException {
        int pageNumber = 1;
        int totalResults = 0;
        Page page = Page.create(pageNumber, totalResults);
        assertAll(
            () -> assertEquals(pageNumber, page.pageNumber()),
            () -> assertEquals(1, page.totalPages()),
            () -> assertEquals(0, page.pageSize()),
            () -> assertEquals(totalResults, page.totalResults()),
            () -> assertEquals(0, page.fromIndex()),
            () -> assertEquals(0, page.toIndex())
        );
    }

    @Test
    @DisplayName("Should create a little ResultsPage with valid inputs")
    void onePage() throws PaginationException {
        int pageNumber = 1;
        int totalResults = 3;
        Page page = Page.create(pageNumber, totalResults);
        assertAll(
            () -> assertEquals(pageNumber, page.pageNumber()),
            () -> assertEquals(1, page.totalPages()),
            () -> assertEquals(3, page.pageSize()),
            () -> assertEquals(totalResults, page.totalResults()),
            () -> assertEquals(0, page.fromIndex()),
            () -> assertEquals(3, page.toIndex())
        );
    }

    @Test
    @DisplayName("Should create ResultsPage with valid inputs")
    void shouldCreateResultsPageWithValidInputs() throws PaginationException {
        int pageNumber = 2;
        int totalResults = PaginatedResponse.MAX_PAGE_SIZE * 2 + 1;
        int pageSize = PaginatedResponse.MAX_PAGE_SIZE;
        Page page = Page.create(pageNumber, totalResults);
        assertAll(
            () -> assertEquals(pageNumber, page.pageNumber()),
            () -> assertEquals(3, page.totalPages()),
            () -> assertEquals(pageSize, page.pageSize()),
            () -> assertEquals(totalResults, page.totalResults()),
            () -> assertEquals((pageNumber - 1) * pageSize, page.fromIndex()),
            () -> assertEquals(pageNumber * pageSize, page.toIndex())
        );
    }

    @Test
    @DisplayName("Should throw PaginationException when page number exceeds total results")
    void shouldThrowPaginationExceptionWhenPageNumberExceedsTotalResults() {
        int pageNumber = 2;
        int totalResults = 5;
        assertThrows(PaginationException.class, () -> Page.create(pageNumber, totalResults));
    }

    @Test
    @DisplayName("Should throw PaginationException for negative inputs")
    void shouldThrowPaginationExceptionForNegativeInputs() {
        int pageNumber = -1;
        int totalResults = 50;
        assertThrows(PaginationException.class, () -> Page.create(pageNumber, totalResults));
    }

}