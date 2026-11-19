package org.ollide.fussifinder.http.serializer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KickoffTimeSerializerTest {

    private static JsonMapper mapper;

    @BeforeAll
    static void setUp() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalTime.class, new KickoffTimeSerializer());
        mapper = JsonMapper.builder().addModule(module).build();
    }

    @Test
    void serialize() {
        assertEquals("\"15:30\"", mapper.writeValueAsString(LocalTime.of(15, 30)));
        assertEquals("\"15:30\"", mapper.writeValueAsString(LocalTime.of(15, 30, 0)));
        assertEquals("\"15:30\"", mapper.writeValueAsString(LocalTime.of(15, 30, 15)));
    }
}
