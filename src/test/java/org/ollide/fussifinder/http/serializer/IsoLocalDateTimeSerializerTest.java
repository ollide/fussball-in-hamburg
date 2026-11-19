package org.ollide.fussifinder.http.serializer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IsoLocalDateTimeSerializerTest {

    private static JsonMapper mapper;

    @BeforeAll
    static void setUp() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new IsoLocalDateTimeSerializer());
        mapper = JsonMapper.builder().addModule(module).build();
    }

    @Test
    void serialize() {
        LocalDateTime localDateTime = LocalDateTime.of(2019, 1, 12, 15, 30);
        assertEquals("\"2019-01-12T15:30:00\"", mapper.writeValueAsString(localDateTime));
    }

}
