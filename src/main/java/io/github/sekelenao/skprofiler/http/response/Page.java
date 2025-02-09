package io.github.sekelenao.skprofiler.http.response;

import io.github.sekelenao.skprofiler.exception.PaginationException;
import io.github.sekelenao.skprofiler.util.Assertions;

public record Page(int pageNumber, int totalPages, int pageSize, int totalResults, int fromIndex, int toIndex) {

    public Page {
        Assertions.arePositivesOrZero(pageNumber, totalPages, pageSize, totalResults, fromIndex, toIndex);
        Assertions.isLowerOrEqualThan(fromIndex, toIndex);
    }

    public static Page create(int pageNumber, int totalResults) throws PaginationException {
        Assertions.isPositiveOrZero(totalResults);
        if (pageNumber < 1) {
            throw new PaginationException();
        }
        if(totalResults == 0){
            if(pageNumber > 1) throw new PaginationException();
            return new Page(pageNumber, 1, 0, 0, 0, 0);
        }
        int trailingZeros = Integer.numberOfTrailingZeros(PaginatedResponse.MAX_PAGE_SIZE);
        int totalPages = ((totalResults - 1) >> trailingZeros) + 1;
        if (totalPages < pageNumber) {
            throw new PaginationException();
        }
        int from = (pageNumber - 1) << trailingZeros;
        int pageSize = Math.min(totalResults - from, PaginatedResponse.MAX_PAGE_SIZE);
        return new Page(pageNumber, totalPages, pageSize, totalResults, from, from + pageSize);
    }

}
