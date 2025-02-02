package io.github.sekelenao.skprofiler.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sekelenao.skprofiler.exception.InvalidJsonFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

final class TestCustomJsonInterpreter {

    private static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();

    private static final String PERSON_JSON = """
            { \t \n
                "name":"Me",
                 "age":25.9
                }
            """;

    record Person(String name, double age) {

        @Override
        public String toString() {
            return name + " is " + age + " years old";
        }

    }

    private static class TestObject {

        @Override
        public String toString() {
            return "TestObject";
        }

    }

    record AllTypes(short a, int b, long c, float d, double e, byte f, boolean g, char h, String i, String nul) {}

    record SubObjectIterable(List<?> list) {}

    record SubObject(TestObject testObject) {}

    record SubRecord(Person person) {}

    record NullRecord(String string) {}

    record BooleanRecord(boolean bool) {}

    @Test
    @DisplayName("Json serialization assertions")
    void jsonConversionAssertions() {
        assertThrows(NullPointerException.class, () -> CustomJsonInterpreter.serialize(null));
    }

    @Test
    @DisplayName("Json serialization is working")
    void jsonConversionIsWorking() throws JsonProcessingException {
        var person = new Person("Me", 25.9);

        assertEquals(
            JACKSON_MAPPER.writeValueAsString(person),
            CustomJsonInterpreter.serialize(person)
        );
    }

    @Test
    @DisplayName("All primitive types are well serialized")
    void subObjectIsWellSerialized() throws JsonProcessingException {
        AllTypes allTypes = new AllTypes((short) 1, 42, 123456789L, 3.14f, 2.71828, (byte) 8, true, 'A', "Hello", null);
        assertEquals(
            JACKSON_MAPPER.writeValueAsString(allTypes),
            CustomJsonInterpreter.serialize(allTypes)
        );
    }

    @Test
    @DisplayName("Null is serialized")
    void nullIsSerialized() throws JsonProcessingException {
        var nullRecord = new NullRecord(null);
        assertEquals(
                JACKSON_MAPPER.writeValueAsString(nullRecord),
                CustomJsonInterpreter.serialize(nullRecord)
        );
    }

    @Test
    @DisplayName("Sub object iterable serialization behavior")
    void subObjectIterableBehavior() {
        var subObject = new SubObjectIterable(List.of(1, 3, 5));
        assertEquals(
                "{\"list\":" + subObject.list().toString() + "}",
                CustomJsonInterpreter.serialize(subObject)
        );
    }

    @Test
    @DisplayName("Sub object (not a record or an iterable) serialization behavior")
    void subObjectBehavior() {
        var testObject = new SubObject(new TestObject());
        assertEquals(
            "{\"testObject\":\"TestObject\"}",
            CustomJsonInterpreter.serialize(testObject)
        );
    }

    @Test
    @DisplayName("Sub record serialization behavior")
    void subRecordBehavior() throws JsonProcessingException {
        var subRecord = new SubRecord(new Person("Me", 25.9));
        assertEquals(
                JACKSON_MAPPER.writeValueAsString(subRecord),
                CustomJsonInterpreter.serialize(subRecord)
        );
    }

    @Test
    @DisplayName("Json deserialization assertions")
    void deserializationAssertions() {
        assertAll(
                () -> assertThrows(
                        NullPointerException.class,
                        () -> CustomJsonInterpreter.deserialize(null, Person.class)
                ),
                () -> assertThrows(
                        NullPointerException.class,
                        () -> CustomJsonInterpreter.deserialize(PERSON_JSON, null)
                )
        );
    }

    @Test
    @DisplayName("Json deserialization is working")
    void deserializationIsWorking() {
        var person = new Person("Me", 25.9);
        assertEquals(
                person,
                CustomJsonInterpreter.deserialize(PERSON_JSON, Person.class)
        );
    }

    private static void checkJsonThrow(String json) {
        assertThrows(
                InvalidJsonFormatException.class,
                () -> CustomJsonInterpreter.deserialize(json, Person.class)
        );
    }

    @Test
    @DisplayName("Comma inside a number throws at deserialization")
    void invalidJsonFormat1() {
        checkJsonThrow("""
                {
                    "name":"Me",
                    "age":25,9
                }
                """);
    }

    @Test
    @DisplayName("Comma at the last entry throws at deserialization")
    void invalidJsonFormat2() {
        checkJsonThrow("""
                {
                    "name": "Me",
                    "age": 25.9,
                }
                """);
    }

    @Test
    @DisplayName("Incorrect key throws at deserialization")
    void invalidJsonFormat4() {
        checkJsonThrow(
                """
                {
                    name:"Me",
                    "age":25.9
                }
                """
        );
    }

    @Test
    @DisplayName("No braces throws at deserialization")
    void invalidJsonFormat5() {
        checkJsonThrow(
                """
                
                    name:"Me",
                    "age":25.9
                }
                """
        );
        checkJsonThrow(
                """
                {
                    name:"Me",
                    "age":25.9
                
                """
        );
    }

    @Test
    @DisplayName("Wrong boolean throws at deserialization")
    void invalidJsonFormat6() {
        var json = "{\"bool\":True}";
        assertThrows(
                InvalidJsonFormatException.class,
                () -> CustomJsonInterpreter.deserialize(json, BooleanRecord.class)
        );
    }

    @Test
    @DisplayName("All types are working at deserialization")
    void allTypes() throws JsonProcessingException {
        var allTypes = new AllTypes((short) 1, 42, 123456789L, 3.14f, 2.71828, (byte) 8, true, 'A', "Hello", "");
        assertEquals(
                allTypes,
                CustomJsonInterpreter.deserialize(JACKSON_MAPPER.writeValueAsString(allTypes), AllTypes.class)
        );
    }

    @Test
    @DisplayName("Null not permitted at deserialization")
    void nullAsAValue() {
        checkJsonThrow(
                """
                {
                    "name":null,
                    "age":25.9
                }
                """
        );
    }


}
