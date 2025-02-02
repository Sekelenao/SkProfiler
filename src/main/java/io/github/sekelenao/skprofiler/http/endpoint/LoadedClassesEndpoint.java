package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.exception.DynamicTypingException;
import io.github.sekelenao.skprofiler.exception.InvalidJsonException;
import io.github.sekelenao.skprofiler.exception.PaginationException;
import io.github.sekelenao.skprofiler.http.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.QueryParamsAsDynamicTypedMap;
import io.github.sekelenao.skprofiler.http.ResultsPage;
import io.github.sekelenao.skprofiler.http.dto.receive.PatternDTO;
import io.github.sekelenao.skprofiler.http.dto.send.PaginationDTO;
import io.github.sekelenao.skprofiler.http.dto.send.loadedclasses.LoadedClassesPageDTO;
import io.github.sekelenao.skprofiler.json.CustomJsonInterpreter;
import io.github.sekelenao.skprofiler.util.ArrayViews;

import java.lang.instrument.Instrumentation;
import java.util.function.Predicate;
import java.util.regex.Pattern;

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
                    ArrayViews.<Class<?>>ranged(loadedClasses, resultsPage.fromIndex(), resultsPage.toIndex())
                )
            );
        } catch (DynamicTypingException _) {
            return CustomHttpResponse.badRequest("Page parameter must be an integer");
        } catch (PaginationException _) {
            return CustomHttpResponse.badRequest("Requested page does not exist");
        }
    }

    private static Predicate<Class<?>> filterByName(Pattern pattern) {
        return clazz -> pattern.matcher(clazz.toString()).matches();
    }

    @Override
    public CustomHttpResponse processPostRequest(String requestQuery, String requestBody) {
        var params = QueryParamsAsDynamicTypedMap.of(requestQuery);
        try {
            var pageNumber = params.getAsInt("page").orElse(1);
            var patternAsString = CustomJsonInterpreter.deserialize(requestBody, PatternDTO.class).pattern();
            var pattern = Pattern.compile(patternAsString);
            var view = ArrayViews.<Class<?>>filtered(instrumentation.getAllLoadedClasses(), filterByName(pattern));
            var resultsPage = ResultsPage.create(pageNumber, view.size());
            return CustomHttpResponse.success(
                new LoadedClassesPageDTO(
                    PaginationDTO.from(resultsPage, route()),
                    view.subList(resultsPage.fromIndex(), resultsPage.toIndex())
                )
            );
        } catch (DynamicTypingException _) {
            return CustomHttpResponse.badRequest("Page parameter must be an integer");
        } catch (InvalidJsonException _) {
            return CustomHttpResponse.badRequest();
        } catch (PaginationException _) {
            return CustomHttpResponse.badRequest("Requested page does not exist");
        }
    }
}
