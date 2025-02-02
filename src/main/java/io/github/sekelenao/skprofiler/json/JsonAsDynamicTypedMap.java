package io.github.sekelenao.skprofiler.json;

import io.github.sekelenao.skprofiler.exception.DynamicTypingException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

final class JsonAsDynamicTypedMap {

    private final Map<String, String> dynamicMap;

    public JsonAsDynamicTypedMap() {
        this.dynamicMap = new HashMap<>();
    }

    public int size() {
        return dynamicMap.size();
    }

    public void put(String key, String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        dynamicMap.put(key, value);
    }

    private static boolean parseBoolean(String value) throws DynamicTypingException{
        return switch (value) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new DynamicTypingException("Value is not a boolean: " + value);
        };
    }

    private static char parseChar(String value) throws DynamicTypingException {
        if(value.length() != 1){
            throw new DynamicTypingException("String have wrong size for a char: " + value);
        }
        return value.charAt(0);
    }

    private static Object transformValueIntoTargetType(String value, Class<?> type) throws DynamicTypingException {
        try {
            return switch (type.getSimpleName()) {
                case "Boolean", "boolean" -> parseBoolean(value);
                case "byte", "Byte" -> Byte.parseByte(value);
                case "Short", "short" -> Short.parseShort(value);
                case "Integer", "int" -> Integer.parseInt(value);
                case "Long", "long" -> Long.parseLong(value);
                case "Float", "float" -> Float.parseFloat(value);
                case "Double", "double" -> Double.parseDouble(value);
                case "Character", "char" -> parseChar(value);
                case "String" -> value;
                default -> throw new IllegalArgumentException("Provided type is not supported: " + type.getName());
            };
        } catch (DynamicTypingException | IllegalArgumentException exception) {
            throw new DynamicTypingException(exception);
        }
    }

    public Object get(String key, Class<?> targetType) throws DynamicTypingException {
        Objects.requireNonNull(key);
        Objects.requireNonNull(targetType);
        var value = dynamicMap.get(key);
        if(value == null){
            throw new NoSuchElementException(key);
        }
        return transformValueIntoTargetType(value, targetType);
    }

}
