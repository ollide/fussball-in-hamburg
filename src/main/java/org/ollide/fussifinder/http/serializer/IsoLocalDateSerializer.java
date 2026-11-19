package org.ollide.fussifinder.http.serializer;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IsoLocalDateSerializer extends ValueSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate date, JsonGenerator generator, SerializationContext context) {
        String formattedDateTime = date.format(DateTimeFormatter.ISO_DATE);
        generator.writeString(formattedDateTime);
    }

}
