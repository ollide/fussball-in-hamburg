package org.ollide.fussifinder.http.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

public class KickoffTimeSerializer extends JsonSerializer<LocalTime> {

    private static final DateTimeFormatter KICKOFF_TIME_FORMAT;
    static {
        KICKOFF_TIME_FORMAT = new DateTimeFormatterBuilder()
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .toFormatter();
    }

    @Override
    public void serialize(LocalTime time, JsonGenerator generator, SerializerProvider sp) throws IOException {
        String formattedDateTime = time.format(KICKOFF_TIME_FORMAT);
        generator.writeString(formattedDateTime);
    }

}
