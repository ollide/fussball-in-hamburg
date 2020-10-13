package org.ollide.fussifinder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.ollide.fussifinder.api.OverpassClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class OverpassConfig {

    @Value("${fussifinder.overpassUrl}")
    private String overpassUrl;

    @Bean
    public OverpassClient overpassClient(OkHttpClient client, ObjectMapper objectMapper) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(overpassUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();

        return retrofit.create(OverpassClient.class);
    }

}
