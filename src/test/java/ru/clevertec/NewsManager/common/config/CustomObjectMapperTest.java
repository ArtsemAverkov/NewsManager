package ru.clevertec.NewsManager.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.UnknownFieldSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.NewsManager.config.CustomObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 The CustomObjectMapperTest class is a test class for the CustomObjectMapper class.
 It tests the serialization and deserialization of UnknownFieldSet using the custom ObjectMapper.
 */
public class CustomObjectMapperTest {

    private ObjectMapper objectMapper;

    /**
     Sets up the test by creating the custom ObjectMapper instance.
     */
    @BeforeEach
    void setUp() {
        objectMapper = CustomObjectMapper.createObjectMapper();
    }

    /**
     Tests the serialization and deserialization of UnknownFieldSet.
     @throws Exception if an error occurs during the test execution.
     */
    @Test
    void shouldSerializeAndDeserializeUnknownFieldSet() throws Exception {
        UnknownFieldSet unknownFieldSet = UnknownFieldSet.newBuilder()
                .addField(1, UnknownFieldSet.Field.newBuilder().addVarint(42).build())
                .addField(2, UnknownFieldSet.Field.newBuilder().addFixed32(123).build())
                .build();

        String json = objectMapper.writeValueAsString(unknownFieldSet);
        UnknownFieldSet deserialized = objectMapper.readValue(json, UnknownFieldSet.class);

        assertThat(deserialized).isEqualTo(unknownFieldSet);
    }
}
