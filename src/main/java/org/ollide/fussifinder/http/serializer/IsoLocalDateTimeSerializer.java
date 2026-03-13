package org.ollide.fussifinder.http.serializer;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IsoLocalDateTimeSerializer extends ValueSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime dateTime, JsonGenerator generator, SerializationContext context) {
        String formattedDateTime = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        generator.writeString(formattedDateTime);
    }

}
