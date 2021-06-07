package org.ollide.fussifinder.http.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsoLocalDateTimeSerializerTest {

    private static ObjectMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new IsoLocalDateTimeSerializer());
        mapper.registerModule(module);
    }

    @Test
    public void serialize() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.of(2019, 1, 12, 15, 30);
        assertEquals("\"2019-01-12T15:30:00\"", mapper.writeValueAsString(localDateTime));
    }

}
