package org.ollide.fussifinder.http.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IsoLocalDateSerializerTest {

    private static ObjectMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new IsoLocalDateSerializer());
        mapper.registerModule(module);
    }

    @Test
    void serialize() throws JsonProcessingException {
        assertEquals("\"2019-01-12\"", mapper.writeValueAsString(LocalDate.of(2019, 1, 12)));
    }

}
