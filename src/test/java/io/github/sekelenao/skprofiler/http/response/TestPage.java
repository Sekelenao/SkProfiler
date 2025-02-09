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
    @DisplayName("Should throw PaginationException when page number exceeds total page number")
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

    @Test
    @DisplayName("Should return only one page if total results equals max page size")
    void shouldReturnOnlyOnePageIfTotalResultsEqualsMaxPageSize() throws PaginationException {
        int pageNumber = 1;
        int totalResults = PaginatedResponse.MAX_PAGE_SIZE;
        var page = Page.create(pageNumber, totalResults);
        assertEquals(1, page.totalPages());
    }

    @Test
    @DisplayName("Should return two pages if total results equals max page size plus one")
    void shouldReturnTwoPagesIfTotalResultsEqualsMaxPageSizePlusOne() throws PaginationException {
        int pageNumber = 1;
        int totalResults = PaginatedResponse.MAX_PAGE_SIZE + 1;
        var page = Page.create(pageNumber, totalResults);
        assertEquals(2, page.totalPages());
    }

    @Test
    @DisplayName("Should return two pages if total results equals max page size plus two")
    void shouldReturnTwoPagesIfTotalResultsEqualsMaxPageSizePlusTwo() throws PaginationException {
        int pageNumber = 1;
        int totalResults = PaginatedResponse.MAX_PAGE_SIZE + 3;
        var page = Page.create(pageNumber, totalResults);
        assertEquals(2, page.totalPages());
    }

    @Test
    @DisplayName("Should return three pages if total results equals max page size times two plus two")
    void shouldReturnThreePagesIfTotalResultsEqualsMaxPageSizeTimesThree() throws PaginationException {
        int totalResults = PaginatedResponse.MAX_PAGE_SIZE * 3;
        var page = Page.create(3, totalResults);
        assertAll(
            () -> assertEquals(3, page.totalPages()),
            () -> assertEquals(PaginatedResponse.MAX_PAGE_SIZE, page.pageSize())
        );
    }

    @Test
    @DisplayName("Should return three pages if total results equals max page size times two plus one")
    void shouldReturnThreePagesIfTotalResultsEqualsMaxPageSizeTimesTwoPlusOne() throws PaginationException {
        int totalResults = PaginatedResponse.MAX_PAGE_SIZE * 2 + 1;
        var page = Page.create(3, totalResults);
        assertAll(
            () -> assertEquals(3, page.totalPages()),
            () -> assertEquals(1, page.pageSize())
        );
    }

    @Test
    @DisplayName("Should return three pages if total results equals max page size times two plus two")
    void shouldReturnThreePagesIfTotalResultsEqualsMaxPageSizeTimesTwoPlusTwo() throws PaginationException {
        int totalResults = PaginatedResponse.MAX_PAGE_SIZE * 2 + 2;
        var page = Page.create(3, totalResults);
        assertAll(
            () -> assertEquals(3, page.totalPages()),
            () -> assertEquals(2, page.pageSize())
        );
    }

    @Test
    @DisplayName("Should handle very large totalResults")
    void largeTotalResults() throws PaginationException {
        int totalResults = Integer.MAX_VALUE;
        int maxPage = Integer.MAX_VALUE / PaginatedResponse.MAX_PAGE_SIZE;
        var page = Page.create(maxPage, totalResults);
        assertAll(
            () -> assertTrue(page.totalPages() > 0),
            () -> assertTrue(page.fromIndex() > 0),
            () -> assertTrue(page.toIndex() > 0)
        );
    }

    @Test
    @DisplayName("Throws when too much page and zero results")
    void throwsWhenTooMuchPageAndZeroResults() {
        assertThrows(PaginationException.class, () -> Page.create(2, 0));
    }

}