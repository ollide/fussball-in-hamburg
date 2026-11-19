package org.ollide.fussifinder.config;

import org.ollide.fussifinder.api.MatchClient;
import org.ollide.fussifinder.api.OverpassClient;
import org.ollide.fussifinder.http.HeaderInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

@Configuration
@ImportHttpServices(group = "matches", types = { MatchClient.class })
@ImportHttpServices(group = "overpass", types = { OverpassClient.class })
public class ClientConfig {

    @Bean
    HeaderInterceptor headerInterceptor(@Value("${fussifinder.userAgent}") String userAgent) {
        return new HeaderInterceptor(userAgent);
    }

    @Bean
    RestClientHttpServiceGroupConfigurer groupConfigurer(
            @Value("${fussifinder.crawlUrl}") String crawlUrl,
            @Value("${fussifinder.overpassUrl}") String overpassUrl,
            HeaderInterceptor headerInterceptor) {
        return groups -> {

            groups.filterByName("matches").forEachClient((group, builder) ->
                    builder
                            .requestFactory(matchesRequestFactory())
                            .baseUrl(crawlUrl)
                            .requestInterceptor(headerInterceptor)
                            .configureMessageConverters(clientBuilder -> {
                                JacksonJsonHttpMessageConverter jsonConverter = new JacksonJsonHttpMessageConverter();
                                jsonConverter.setSupportedMediaTypes(List.of(
                                        MediaType.APPLICATION_JSON,
                                        new MediaType("application", "*+json"),
                                        // Some JSON responses are returned with the wrong mime-type
                                        MediaType.TEXT_HTML
                                ));
                                clientBuilder
                                        .addCustomConverter(new StringHttpMessageConverter())
                                        .addCustomConverter(jsonConverter);
                            }));

            groups.filterByName("overpass").forEachClient((group, builder) ->
                    builder
                            .requestFactory(overpassRequestFactory())
                            .baseUrl(overpassUrl)
                            .requestInterceptor(headerInterceptor));
        };
    }

    ClientHttpRequestFactory matchesRequestFactory() {
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        JdkClientHttpRequestFactory jdkRequestFactory = new JdkClientHttpRequestFactory(httpClient);
        jdkRequestFactory.setReadTimeout(Duration.ofMinutes(1));
        return jdkRequestFactory;
    }

    ClientHttpRequestFactory overpassRequestFactory() {
        JdkClientHttpRequestFactory jdkRequestFactory = new JdkClientHttpRequestFactory();
        jdkRequestFactory.setReadTimeout(Duration.ofMinutes(2));
        return jdkRequestFactory;
    }
}
