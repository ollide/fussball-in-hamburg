package org.ollide.fussifinder.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.ollide.fussifinder.api.MatchClient;
import org.ollide.fussifinder.http.converter.TextConverterFactory;
import org.ollide.fussifinder.http.HeaderInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
public class AppConfig {

    @Value("${fussifinder.crawlUrl}")
    private String crawlUrl;

    @Value("${fussifinder.userAgent}")
    private String userAgent;

    @Bean
    public OkHttpClient okHttpClient(HeaderInterceptor headerInterceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addNetworkInterceptor(headerInterceptor);
        return clientBuilder.build();
    }

    @Bean
    public HeaderInterceptor headerInterceptor() {
        return new HeaderInterceptor(userAgent);
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(crawlUrl)
                .addConverterFactory(TextConverterFactory.create())
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

}
