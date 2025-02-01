package io.github.sekelenao.skprofiler.http.dto.send;

import io.github.sekelenao.skprofiler.http.ResultsPage;
import io.github.sekelenao.skprofiler.util.Assertions;

import java.util.Objects;

public record PaginationDTO(int currentPage, int totalPages, int pageSize, int totalResults, String previousPage, String nextPage) {

    public PaginationDTO {
        Assertions.arePositives(currentPage, totalPages, pageSize, totalResults);
    }

    private static String previousPage(String route, int page) {
        if(page > 1) {
            return route + "?page=" + (page - 1);
        }
        return null;
    }

    private static String nextPage(String route, int page, int totalPages) {
        if(page < totalPages) {
            return route + "?page=" + (page + 1);
        }
        return null;
    }

    public static PaginationDTO from(ResultsPage resultsPage, String route) {
        Objects.requireNonNull(resultsPage);
        return new PaginationDTO(
            resultsPage.pageNumber(),
            resultsPage.totalPages(),
            resultsPage.pageSize(),
            resultsPage.totalResults(),
            previousPage(route, resultsPage.pageNumber()),
            nextPage(route, resultsPage.pageNumber(), resultsPage.totalPages())
        );
    }

}
