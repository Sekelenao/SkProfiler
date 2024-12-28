package io.github.sekelenao.skprofiler.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class TestJsonConverter {

    private static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();

    private record Person(String name, double age) {

        @Override
        public String toString() {
            return name + " is " + age + " years old";
        }

    }

    private record AllTypes(
            short a, int b, long c, float d, double e, byte f, boolean g, char h, String i, Object nul
    ) {
    }

    private record SubObject(List<?> list){}

    private record SubRecord(Person person){}

    @Test
    @DisplayName("Json conversion assertions")
    void jsonConversionAssertions() {
        assertThrows(NullPointerException.class, () -> JsonConverter.convert(null));
    }

    @Test
    @DisplayName("Json conversion is working")
    void jsonConversionIsWorking() throws JsonProcessingException {
        var person = new Person("Me", 25.9);
        assertEquals(
                JACKSON_MAPPER.writeValueAsString(person),
                JsonConverter.convert(person)
        );
    }

    @Test
    @DisplayName("All primitive types are well serialized")
    void subObjectIsWellSerialized() throws JsonProcessingException {
        AllTypes allTypes = new AllTypes((short) 1, 42, 123456789L, 3.14f, 2.71828, (byte) 8, true, 'A', "Hello", null);
        assertEquals(
                JACKSON_MAPPER.writeValueAsString(allTypes),
                JsonConverter.convert(allTypes)
        );
    }

    @Test
    @DisplayName("Sub object (not a record) behavior")
    void subObjectBehavior() {
        var subObject = new SubObject(List.of(1, 3 ,5));
        assertEquals(
                "{\"list\":\"" + subObject.list().toString() + "\"}",
                JsonConverter.convert(subObject)
        );
    }

    @Test
    @DisplayName("Sub record behavior")
    void subRecordBehavior() throws JsonProcessingException {
        var subRecord = new SubRecord(new Person("Me", 25.9));
        assertEquals(
                JACKSON_MAPPER.writeValueAsString(subRecord),
                JsonConverter.convert(subRecord)
        );
    }

}
