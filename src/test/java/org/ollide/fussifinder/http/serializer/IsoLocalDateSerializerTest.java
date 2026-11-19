package org.ollide.fussifinder.http.serializer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IsoLocalDateSerializerTest {

    private static JsonMapper mapper;

    @BeforeAll
    static void setUp() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new IsoLocalDateSerializer());
        mapper = JsonMapper.builder().addModule(module).build();
    }

    @Test
    void serialize() {
        assertEquals("\"2019-01-12\"", mapper.writeValueAsString(LocalDate.of(2019, 1, 12)));
    }

}
