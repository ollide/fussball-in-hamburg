package org.ollide.fussifinder.http.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IsoLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime dateTime, JsonGenerator generator, SerializerProvider sp) throws IOException {
        String formattedDateTime = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        generator.writeString(formattedDateTime);
    }

}
