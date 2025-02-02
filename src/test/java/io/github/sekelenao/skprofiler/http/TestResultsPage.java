package io.github.sekelenao.skprofiler.http;

import io.github.sekelenao.skprofiler.exception.PaginationException;
import io.github.sekelenao.skprofiler.http.dto.send.PaginatedResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class TestResultsPage {

    @Test
    @DisplayName("Should create a empty ResultsPage with valid inputs")
    void emptyPage() throws PaginationException {
        int pageNumber = 1;
        int totalResults = 0;
        ResultsPage resultsPage = ResultsPage.create(pageNumber, totalResults);
        assertAll(
            () -> assertEquals(pageNumber, resultsPage.pageNumber()),
            () -> assertEquals(1, resultsPage.totalPages()),
            () -> assertEquals(0, resultsPage.pageSize()),
            () -> assertEquals(totalResults, resultsPage.totalResults()),
            () -> assertEquals(0, resultsPage.fromIndex()),
            () -> assertEquals(0, resultsPage.toIndex())
        );
    }

    @Test
    @DisplayName("Should create a little ResultsPage with valid inputs")
    void onePage() throws PaginationException {
        int pageNumber = 1;
        int totalResults = 3;
        ResultsPage resultsPage = ResultsPage.create(pageNumber, totalResults);
        assertAll(
            () -> assertEquals(pageNumber, resultsPage.pageNumber()),
            () -> assertEquals(1, resultsPage.totalPages()),
            () -> assertEquals(3, resultsPage.pageSize()),
            () -> assertEquals(totalResults, resultsPage.totalResults()),
            () -> assertEquals(0, resultsPage.fromIndex()),
            () -> assertEquals(3, resultsPage.toIndex())
        );
    }

    @Test
    @DisplayName("Should create ResultsPage with valid inputs")
    void shouldCreateResultsPageWithValidInputs() throws PaginationException {
        int pageNumber = 2;
        int totalResults = PaginatedResponse.PAGE_SIZE * 2 + 1;
        int pageSize = PaginatedResponse.PAGE_SIZE;
        ResultsPage resultsPage = ResultsPage.create(pageNumber, totalResults);
        assertAll(
            () -> assertEquals(pageNumber, resultsPage.pageNumber()),
            () -> assertEquals(3, resultsPage.totalPages()),
            () -> assertEquals(pageSize, resultsPage.pageSize()),
            () -> assertEquals(totalResults, resultsPage.totalResults()),
            () -> assertEquals((pageNumber - 1) * pageSize, resultsPage.fromIndex()),
            () -> assertEquals(pageNumber * pageSize, resultsPage.toIndex())
        );
    }

    @Test
    @DisplayName("Should throw PaginationException when page number exceeds total results")
    void shouldThrowPaginationExceptionWhenPageNumberExceedsTotalResults() {
        int pageNumber = 2;
        int totalResults = 5;
        assertThrows(PaginationException.class, () -> ResultsPage.create(pageNumber, totalResults));
    }

    @Test
    @DisplayName("Should throw PaginationException for negative inputs")
    void shouldThrowPaginationExceptionForNegativeInputs() {
        int pageNumber = -1;
        int totalResults = 50;
        assertThrows(IllegalArgumentException.class, () -> ResultsPage.create(pageNumber, totalResults));
    }

}