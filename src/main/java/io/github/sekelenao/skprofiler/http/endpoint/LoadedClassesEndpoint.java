package io.github.sekelenao.skprofiler.http.endpoint;

import io.github.sekelenao.skprofiler.exception.InvalidJsonException;
import io.github.sekelenao.skprofiler.exception.InvalidQueryParamException;
import io.github.sekelenao.skprofiler.exception.PaginationException;
import io.github.sekelenao.skprofiler.http.dto.receive.PatternDTO;
import io.github.sekelenao.skprofiler.http.dto.send.PageInfoDTO;
import io.github.sekelenao.skprofiler.http.dto.send.classes.LoadedClassesDTO;
import io.github.sekelenao.skprofiler.http.param.QueryParamsReader;
import io.github.sekelenao.skprofiler.http.response.CustomHttpResponse;
import io.github.sekelenao.skprofiler.http.response.Page;
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
        var paramsReader = new QueryParamsReader(requestQuery);
        try {
            var page = Page.create(paramsReader.presentOrDefault("page", 1), loadedClasses.length);
            return CustomHttpResponse.success(
                new LoadedClassesDTO(
                    PageInfoDTO.from(page, route()),
                    ArrayViews.<Class<?>>ranged(loadedClasses, page.fromIndex(), page.toIndex())
                )
            );
        } catch (InvalidQueryParamException _) {
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
        var paramsReader = new QueryParamsReader(requestQuery);
        try {
            var patternAsString = CustomJsonInterpreter.deserialize(requestBody, PatternDTO.class).pattern();
            var pattern = Pattern.compile(patternAsString);
            var view = ArrayViews.<Class<?>>filtered(instrumentation.getAllLoadedClasses(), filterByName(pattern));
            var page = Page.create(paramsReader.presentOrDefault("page", 1), view.size());
            return CustomHttpResponse.success(
                new LoadedClassesDTO(
                    PageInfoDTO.from(page, route()),
                    view.subList(page.fromIndex(), page.toIndex())
                )
            );
        } catch (InvalidQueryParamException _) {
            return CustomHttpResponse.badRequest("Page parameter must be an integer");
        } catch (InvalidJsonException _) {
            return CustomHttpResponse.badRequest();
        } catch (PaginationException _) {
            return CustomHttpResponse.badRequest("Requested page does not exist");
        }
    }
}
