package ru.clevertec.NewsManager.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.UnknownFieldSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.NewsManager.config.CustomObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomObjectMapperTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = CustomObjectMapper.createObjectMapper();
    }

    @Test
    void shouldSerializeAndDeserializeUnknownFieldSet() throws Exception {
// Create an UnknownFieldSet object with some fields
        UnknownFieldSet unknownFieldSet = UnknownFieldSet.newBuilder()
                .addField(1, UnknownFieldSet.Field.newBuilder().addVarint(42).build())
                .addField(2, UnknownFieldSet.Field.newBuilder().addFixed32(123).build())
                .build();

// Serialize the object to JSON
        String json = objectMapper.writeValueAsString(unknownFieldSet);

// Deserialize the JSON back to an object
        UnknownFieldSet deserialized = objectMapper.readValue(json, UnknownFieldSet.class);

// Assert that the deserialized object is equal to the original one
        assertThat(deserialized).isEqualTo(unknownFieldSet);
    }
}
