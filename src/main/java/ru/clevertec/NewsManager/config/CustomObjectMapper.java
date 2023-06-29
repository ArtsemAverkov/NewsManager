package ru.clevertec.NewsManager.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.protobuf.UnknownFieldSet;

import java.io.IOException;

public class CustomObjectMapper {

        public static ObjectMapper createObjectMapper () {
            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            SimpleModule protobufModule = new SimpleModule();
            protobufModule.addSerializer(UnknownFieldSet.class, new UnknownFieldSetSerializer());
            protobufModule.addDeserializer(UnknownFieldSet.class, new UnknownFieldSetDeserializer());
            objectMapper.registerModule(protobufModule);

            return objectMapper;
        }

        private static class UnknownFieldSetSerializer extends StdSerializer<UnknownFieldSet> {
            public UnknownFieldSetSerializer() {
                super(UnknownFieldSet.class);
            }

            @Override
            public void serialize(UnknownFieldSet unknownFieldSet, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeBinary(unknownFieldSet.toByteArray());
            }
        }

        private static class UnknownFieldSetDeserializer extends StdDeserializer<UnknownFieldSet> {
            public UnknownFieldSetDeserializer() {
                super(UnknownFieldSet.class);
            }

            @Override
            public UnknownFieldSet deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
                ObjectCodec codec = jsonParser.getCodec();
                byte[] bytes = codec.readValue(jsonParser, byte[].class);
                return UnknownFieldSet.newBuilder().mergeFrom(bytes).build();
            }
        }
    }

