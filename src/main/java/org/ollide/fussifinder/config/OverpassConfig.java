package org.ollide.fussifinder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.ollide.fussifinder.api.OverpassClient;
import org.ollide.fussifinder.http.HeaderInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class OverpassConfig {

    @Value("${fussifinder.overpassUrl}")
    private String overpassUrl;

    @Bean
    public OverpassClient overpassClient(HeaderInterceptor headerInterceptor, ObjectMapper objectMapper) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(overpassUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient(headerInterceptor))
                .build();

        return retrofit.create(OverpassClient.class);
    }

    OkHttpClient okHttpClient(HeaderInterceptor headerInterceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.readTimeout(180, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(180, TimeUnit.SECONDS);
        clientBuilder.callTimeout(180, TimeUnit.SECONDS);
        clientBuilder.addNetworkInterceptor(headerInterceptor);
        return clientBuilder.build();
    }

}
