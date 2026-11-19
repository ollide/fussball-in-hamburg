package org.ollide.fussifinder.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class AppConfig {

    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(JsonReadFeature.ALLOW_UNQUOTED_PROPERTY_NAMES, true)
                .configure(JsonReadFeature.ALLOW_SINGLE_QUOTES, true)
                .build();
    }

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<>() {
            @Override
            @NonNull
            public LocalDate parse(@NonNull String text, @NonNull Locale locale) {
                return LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
            }

            @Override
            @NonNull
            public String print(@NonNull LocalDate object, @NonNull Locale locale) {
                return DateTimeFormatter.ISO_DATE.format(object);
            }
        };
    }
}
