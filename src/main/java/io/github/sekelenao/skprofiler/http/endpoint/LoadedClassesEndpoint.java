package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.exception.DynamicTypingException;
import io.github.sekelenao.skprofiler.exception.PaginationException;
import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.QueryParamsAsDynamicTypedMap;
import io.github.sekelenao.skprofiler.http.ResultsPage;
import io.github.sekelenao.skprofiler.http.dto.send.PaginationDTO;
import io.github.sekelenao.skprofiler.http.dto.send.loadedclasses.LoadedClassesPageDTO;
import io.github.sekelenao.skprofiler.util.ArrayView;

import java.lang.instrument.Instrumentation;

public final class LoadedClassesEndpoint implements Endpoint {

    private final Instrumentation instrumentation;

    public LoadedClassesEndpoint(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public String route() {
        return "/loaded-classes";
    }

    @Override
    public CustomHttpResponse processGetRequest(String requestQuery) {
        var loadedClasses = instrumentation.getAllLoadedClasses();
        var params = QueryParamsAsDynamicTypedMap.of(requestQuery);
        try {
            var resultsPage = ResultsPage.create(params.getAsInt("page").orElse(1), loadedClasses.length);
            return CustomHttpResponse.success(
                new LoadedClassesPageDTO(
                    PaginationDTO.from(resultsPage, route()),
                    new ArrayView<Class<?>>(loadedClasses, resultsPage.fromIndex(), resultsPage.toIndex())
                )
            );
        } catch (DynamicTypingException _) {
            return CustomHttpResponse.badRequest("Page parameter must be an integer");
        } catch (PaginationException _) {
            return CustomHttpResponse.badRequest("Requested page does not exist");
        }
    }

}
