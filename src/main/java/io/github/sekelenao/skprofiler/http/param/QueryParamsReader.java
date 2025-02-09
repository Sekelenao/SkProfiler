package io.github.sekelenao.skprofiler.http.param;

import io.github.sekelenao.skprofiler.exception.DynamicTypingException;
import io.github.sekelenao.skprofiler.exception.InvalidQueryParamException;

import java.util.Objects;

public final class QueryParamsReader {

    private final QueryParamsAsDynamicTypedMap params;

    public QueryParamsReader(String query) {
        Objects.requireNonNull(query);
        params = QueryParamsAsDynamicTypedMap.of(query);
    }

    public int presentOrDefault(String key, int defaultValue) throws InvalidQueryParamException {
        Objects.requireNonNull(key);
        try {
            return params.getAsInt(key).orElse(defaultValue);
        } catch (DynamicTypingException exception){
            throw new InvalidQueryParamException(exception);
        }
    }

}
