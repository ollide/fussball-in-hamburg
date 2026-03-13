package org.ollide.fussifinder.http.serializer;

import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.temporal.ChronoField.*;

public class KickoffTimeSerializer extends ValueSerializer<LocalTime> {

    private static final DateTimeFormatter KICKOFF_TIME_FORMAT;
    static {
        KICKOFF_TIME_FORMAT = new DateTimeFormatterBuilder()
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .toFormatter();
    }

    @Override
    public void serialize(LocalTime time, tools.jackson.core.JsonGenerator generator, SerializationContext context) {
        String formattedDateTime = time.format(KICKOFF_TIME_FORMAT);
        generator.writeString(formattedDateTime);
    }

}
