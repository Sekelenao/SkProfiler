package io.github.sekelenao.skprofiler.http;

import io.github.sekelenao.skprofiler.exception.PaginationException;
import io.github.sekelenao.skprofiler.http.dto.send.PaginatedResponse;
import io.github.sekelenao.skprofiler.util.Assertions;

public record ResultsPage(int pageNumber, int totalPages, int pageSize, int totalResults, int fromIndex, int toIndex) {

    public ResultsPage {
        Assertions.arePositivesOrZero(pageNumber, totalPages, pageSize, totalResults, fromIndex, toIndex);
        Assertions.isLowerOrEqualThan(fromIndex, toIndex);
    }

    public static ResultsPage create(int pageNumber, int totalResults) throws PaginationException {
        Assertions.isPositiveOrZero(totalResults);
        if (pageNumber < 1) {
            throw new PaginationException();
        }
        int totalPages = (totalResults - 1) / PaginatedResponse.PAGE_SIZE + 1;
        if(totalPages < pageNumber) {
            throw new PaginationException();
        }
        if (totalResults == 0) {
            return new ResultsPage(pageNumber, 1, 0, 0, 0, 0);
        }
        int from = (pageNumber - 1) * PaginatedResponse.PAGE_SIZE;
        if (from >= totalResults) {
            throw new PaginationException();
        }
        int pageSize = Math.min(totalResults - from, PaginatedResponse.PAGE_SIZE);
        return new ResultsPage(pageNumber, totalPages, pageSize, totalResults, from, from + pageSize);
    }

}
