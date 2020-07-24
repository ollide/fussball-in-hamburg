package org.ollide.fussifinder.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.ollide.fussifinder.api.MatchClient;
import org.ollide.fussifinder.http.HeaderInterceptor;
import org.ollide.fussifinder.http.converter.TextConverterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Value("${fussifinder.crawlUrl}")
    private String crawlUrl;

    @Value("${fussifinder.userAgent}")
    private String userAgent;

    @Bean
    public OkHttpClient okHttpClient(HeaderInterceptor headerInterceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.readTimeout(60, TimeUnit.SECONDS);
        clientBuilder.addNetworkInterceptor(headerInterceptor);
        return clientBuilder.build();
    }

    @Bean
    public HeaderInterceptor headerInterceptor() {
        return new HeaderInterceptor(userAgent);
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl(crawlUrl)
                .addConverterFactory(TextConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        return mapper;
    }

    @Bean
    public MatchClient matchClient(Retrofit retrofit) {
        return retrofit.create(MatchClient.class);
    }

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            @Override
            @NotNull
            public LocalDate parse(@NotNull String text, @NotNull Locale locale) {
                return LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
            }

            @Override
            @NotNull
            public String print(@NotNull LocalDate object, @NotNull Locale locale) {
                return DateTimeFormatter.ISO_DATE.format(object);
            }
        };
    }
}
