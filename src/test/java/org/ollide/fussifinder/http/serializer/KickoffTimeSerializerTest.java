package org.ollide.fussifinder.http.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class KickoffTimeSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalTime.class, new KickoffTimeSerializer());
        mapper.registerModule(module);
    }

    @Test
    public void serialize() throws JsonProcessingException {
        assertEquals("\"15:30\"", mapper.writeValueAsString(LocalTime.of(15, 30)));
        assertEquals("\"15:30\"", mapper.writeValueAsString(LocalTime.of(15, 30, 0)));
        assertEquals("\"15:30\"", mapper.writeValueAsString(LocalTime.of(15, 30, 15)));
    }
}
