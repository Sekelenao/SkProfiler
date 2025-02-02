package io.github.sekelenao.skprofiler.http;

import io.github.sekelenao.skprofiler.exception.DynamicTypingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;

public final class RequestQueryAsDynamicTypedMap {

    private final Map<String, String> params;

    private RequestQueryAsDynamicTypedMap(Map<String, String> params) {
        this.params = Objects.requireNonNull(params);
    }

    private static Map<String, String> parseQuery(String query) {
        var params = query.split("&");
        var map = new HashMap<String, String>();
        for (var param : params) {
            var keyValue = param.split("=", 2);
            if(keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

    public static RequestQueryAsDynamicTypedMap of(String query) {
        Objects.requireNonNull(query);
        return new RequestQueryAsDynamicTypedMap(parseQuery(query));
    }

    public OptionalInt getAsInt(String key) throws DynamicTypingException {
        Objects.requireNonNull(key);
        try {
            var value = params.get(key);
            if(value == null){
                return OptionalInt.empty();
            }
            return OptionalInt.of(Integer.parseInt(params.get(key)));
        } catch (NumberFormatException e) {
            throw new DynamicTypingException(e);
        }
    }

}
