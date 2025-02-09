package io.github.sekelenao.skprofiler.http.dto.send;

import io.github.sekelenao.skprofiler.http.response.Page;
import io.github.sekelenao.skprofiler.util.Assertions;

import java.util.Objects;

public record PageInfoDTO(int currentPage, int totalPages, int pageSize, int totalResults, String previousPage, String nextPage) {

    public PageInfoDTO {
        Assertions.arePositivesOrZero(currentPage, totalPages, pageSize, totalResults);
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

    public static PageInfoDTO from(Page page, String route) {
        Objects.requireNonNull(page);
        return new PageInfoDTO(
            page.pageNumber(),
            page.totalPages(),
            page.pageSize(),
            page.totalResults(),
            previousPage(route, page.pageNumber()),
            nextPage(route, page.pageNumber(), page.totalPages())
        );
    }

}
