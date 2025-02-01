package io.github.sekelenao.skprofiler.json;

import io.github.sekelenao.skprofiler.exception.InvalidJsonFormatException;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class CustomJsonInterpreter {

    private static final Pattern KEY_PATTERN = Pattern.compile("\"[a-zA-Z]+\"");

    private static final Pattern VALUE_PATTERN = Pattern.compile("\"[^\"]*\"|\\d+(\\.\\d+)?|true|false");

    private static final ClassValue<List<Function<Record, String>>> ACCESSORS_CACHE = new ClassValue<>() {

        @Override
        protected List<Function<Record, String>> computeValue(Class<?> type) {
            return Arrays.stream(type.getRecordComponents())
                    .map(RecordComponent::getAccessor)
                    .map(CustomJsonInterpreter::accessorToJsonFunction)
                    .toList();
        }

    };

    private record ComponentDescriptor(String name, Class<?> type){

        public ComponentDescriptor {
            Objects.requireNonNull(name);
            Objects.requireNonNull(type);
        }

        public static ComponentDescriptor extract(RecordComponent component){
            return new ComponentDescriptor(component.getName(), component.getType());
        }

    }

    private static final ClassValue<ComponentDescriptor[]> COMPONENTS_DESCRIPTOR_CACHE = new ClassValue<>() {

        @Override
        protected ComponentDescriptor[] computeValue(Class<?> type) {
            return Arrays.stream(type.getRecordComponents())
                    .map(ComponentDescriptor::extract)
                    .toArray(ComponentDescriptor[]::new);
        }

    };

    private CustomJsonInterpreter() {
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

    private static <R extends Record> R secureInstantiate(Constructor<R> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Class could not be instantiate: ", e);
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
            throw new UndeclaredThrowableException(cause);
        }
    }

    private static <R extends Record> Constructor<R> secureDeclaredConstructor(Class<R> type, Class<?>... types){
        try {
            return type.getDeclaredConstructor(types);
        } catch (NoSuchMethodException exception){
            throw new AssertionError();
        }
    }

    private static String iterableAsJsonArray(Iterable<?> iterable){
        var builder = new StringBuilder("[");
        var iterator = iterable.iterator();
        while(iterator.hasNext()){
            builder.append(escape(iterator.next()));
            if(iterator.hasNext()){
                builder.append(", ");
            }
        }
        return builder.append("]").toString();
    }

    private static String escape(Object object) {
        return switch (object){
            case null -> "null";
            case Boolean b -> b.toString();
            case Byte b -> b.toString();
            case Short s -> s.toString();
            case Integer o -> o.toString();
            case Long l -> l.toString();
            case Float f -> f.toString();
            case Double d -> d.toString();
            case Character c -> "\"" + c + "\"";
            case String s -> "\"" + s + "\"";
            case Iterable<?> it -> iterableAsJsonArray(it);
            case Record r -> serialize(r);
            default -> "\"" + object + "\"";
        };
    }

    private static Function<Record, String> accessorToJsonFunction(Method accessor){
        return instance -> "\"" + accessor.getName() + "\"" + ":" + escape(secureInvoke(instance, accessor));
    }

    public static String serialize(Record instance){
        Objects.requireNonNull(instance);
        return ACCESSORS_CACHE.get(instance.getClass()).stream()
                .map(mapping -> mapping.apply(instance))
                .collect(Collectors.joining(",", "{", "}"));
    }

    private static String removeJsonBraces(String json){
        var trimmedJson = json.trim();
        if(!trimmedJson.startsWith("{") || !trimmedJson.endsWith("}")) {
            throw new InvalidJsonFormatException("Not a Json format: " + json);
        }
        return trimmedJson.substring(1, trimmedJson.length() - 1).trim();
    }

    private static String removeJsonKeyQuotes(String key){
        return key.substring(1, key.length() - 1);
    }

    private static String removeJsonValueQuotes(String value){
        if(value.startsWith("\"")){
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static void computeJsonEntryAsKeyValuePair(String entry, BiConsumer<String, String> consumer){
        var rawKeyValuePair = entry.split(":", 2);
        if(rawKeyValuePair.length != 2) {
            throw new InvalidJsonFormatException("Not a Json entry: " + entry);
        }
        var key = rawKeyValuePair[0].trim();
        var value = rawKeyValuePair[1].trim();
        if(!KEY_PATTERN.matcher(key).matches() || !VALUE_PATTERN.matcher(value).matches()){
            throw new InvalidJsonFormatException("Not a Json entry: " + entry);
        }
        consumer.accept(removeJsonKeyQuotes(key), removeJsonValueQuotes(value));
    }

    private static JsonAsDynamicTypedMap jsonAsMap(String json){
        var jsonWithoutBraces = removeJsonBraces(json);
        var jsonAsMap = new JsonAsDynamicTypedMap();
        if(jsonWithoutBraces.endsWith(",")) {
            throw new InvalidJsonFormatException("Not a Json format: " + json);
        }
        var entriesArray = jsonWithoutBraces.split(",");
        for (var jsonEntry : entriesArray) {
            computeJsonEntryAsKeyValuePair(jsonEntry, jsonAsMap::put);
        }
        return jsonAsMap;
    }

    public static <R extends Record> R deserialize(String json, Class<R> type) {
        Objects.requireNonNull(json);
        Objects.requireNonNull(type);
        var jsonAsMap = jsonAsMap(json);
        var components = COMPONENTS_DESCRIPTOR_CACHE.get(type);
        var componentsTypes = Arrays.stream(components)
                .map(ComponentDescriptor::type)
                .toArray(Class<?>[]::new);
        var values = Arrays.stream(components)
                .map(descriptor -> jsonAsMap.get(descriptor.name(), descriptor.type()))
                .toArray(Object[]::new);
        var constructor = secureDeclaredConstructor(type, componentsTypes);
        return secureInstantiate(constructor, values);
    }

}
