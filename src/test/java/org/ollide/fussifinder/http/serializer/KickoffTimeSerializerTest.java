package org.ollide.fussifinder.http.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KickoffTimeSerializerTest {

    private static ObjectMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalTime.class, new KickoffTimeSerializer());
        mapper.registerModule(module);
    }

    @Test
    void serialize() throws JsonProcessingException {
        assertEquals("\"15:30\"", mapper.writeValueAsString(LocalTime.of(15, 30)));
        assertEquals("\"15:30\"", mapper.writeValueAsString(LocalTime.of(15, 30, 0)));
        assertEquals("\"15:30\"", mapper.writeValueAsString(LocalTime.of(15, 30, 15)));
    }
}
