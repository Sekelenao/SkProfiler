package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.ResultsPage;
import io.github.sekelenao.skprofiler.http.dto.send.PaginationDTO;
import io.github.sekelenao.skprofiler.http.dto.send.loadedclasses.LoadedClassesPageDTO;
import io.github.sekelenao.skprofiler.util.ArrayViews;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.instrument.Instrumentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class TestLoadedClassesEndpoint {

    @Test
    @DisplayName("Should return success response with pagination and loaded classes when valid query is passed")
    void shouldReturnSuccessResponseWithPaginationAndLoadedClasses() {
        var instrumentation = mock(Instrumentation.class);
        var loadedClasses = new Class<?>[]{String.class, Integer.class, Double.class};
        when(instrumentation.getAllLoadedClasses()).thenReturn(loadedClasses);

        var endpoint = new LoadedClassesEndpoint(instrumentation);
        var response = endpoint.processGetRequest("page=1");

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(CustomHttpResponse.success(new LoadedClassesPageDTO(
                PaginationDTO.from(ResultsPage.create(1, loadedClasses.length), endpoint.route()),
                ArrayViews.ranged(loadedClasses, 0, 3)
            )).status(), response.status())
        );
    }

    @Test
    @DisplayName("Should return bad request response for non-integer page parameter")
    void shouldReturnBadRequestResponseForNonIntegerPageParameter() {
        var instrumentation = mock(Instrumentation.class);
        when(instrumentation.getAllLoadedClasses()).thenReturn(new Class<?>[]{String.class, Integer.class});

        var endpoint = new LoadedClassesEndpoint(instrumentation);
        var response = endpoint.processGetRequest("page=abc");

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(CustomHttpResponse.badRequest("Page parameter must be an integer").status(), response.status())
        );
    }

    @Test
    @DisplayName("Should return bad request response when requested page does not exist")
    void shouldReturnBadRequestResponseWhenRequestedPageDoesNotExist() {
        var instrumentation = mock(Instrumentation.class);
        when(instrumentation.getAllLoadedClasses()).thenReturn(new Class<?>[]{String.class});

        var endpoint = new LoadedClassesEndpoint(instrumentation);
        var response = endpoint.processGetRequest("page=2");

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(CustomHttpResponse.badRequest("Requested page does not exist").status(), response.status())
        );
    }

    @Test
    @DisplayName("Should return success response on empty loaded classes list")
    void shouldReturnSuccessResponseOnEmptyLoadedClassesList() {
        var instrumentation = mock(Instrumentation.class);
        when(instrumentation.getAllLoadedClasses()).thenReturn(new Class<?>[]{});

        var endpoint = new LoadedClassesEndpoint(instrumentation);
        var response = endpoint.processGetRequest("");

        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(CustomHttpResponse.success(new LoadedClassesPageDTO(
                PaginationDTO.from(ResultsPage.create(1, 0), endpoint.route()),
                ArrayViews.ranged(new Class<?>[]{}, 0, 0)
            )).status(), response.status())
        );
    }
}