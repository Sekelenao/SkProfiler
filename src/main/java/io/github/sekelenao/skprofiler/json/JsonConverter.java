package io.github.sekelenao.skprofiler.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class JsonConverter {

    private static final ClassValue<List<Function<Record, String>>> CACHE = new ClassValue<>() {

        @Override
        protected List<Function<Record, String>> computeValue(Class<?> type) {
            return Arrays.stream(type.getRecordComponents())
                    .map(RecordComponent::getAccessor)
                    .map(JsonConverter::accessorToJsonFunction)
                    .toList();
        }

    };

    private JsonConverter() {
        throw new AssertionError("You cannot instantiate this class");
    }

    private static Object secureInvoke(Record instance, Method accessor) {
        try {
            return accessor.invoke(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError();
        } catch (InvocationTargetException e) {
            var cause = e.getCause();
            if (cause instanceof RuntimeException exception) {
                throw exception;
            }
            if (cause instanceof Error error) {
                throw error;
            }
            throw new UndeclaredThrowableException(e);
        }
    }

    private static String escape(Object object) {
        return switch (object){
            case null -> "null";
            case Short s -> s.toString();
            case Integer o -> o.toString();
            case Long l -> l.toString();
            case Float f -> f.toString();
            case Double d -> d.toString();
            case Byte b -> b.toString();
            case Boolean b -> b.toString();
            case Character c -> "\"" + c + "\"";
            case String s -> "\"" + s + "\"";
            case Record r -> convert(r);
            default -> "\"" + object + "\"";
        };
    }

    private static Function<Record, String> accessorToJsonFunction(Method accessor){
        return instance -> "\"" + accessor.getName() + "\"" + ":" + escape(secureInvoke(instance, accessor));
    }

    public static String convert(Record instance){
        Objects.requireNonNull(instance);
        return CACHE.get(instance.getClass()).stream()
                .map(mapping -> mapping.apply(instance))
                .collect(Collectors.joining(",", "{", "}"));
    }

}
