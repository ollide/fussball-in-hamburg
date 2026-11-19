package org.ollide.fussifinder.http;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class HeaderInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderInterceptor.class);

    private final String userAgent;

    public HeaderInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public @NonNull ClientHttpResponse intercept(HttpRequest request,
                                                 byte @NonNull [] body,
                                                 ClientHttpRequestExecution execution) throws IOException {
        LOGGER.trace("Applying required headers.");

        HttpHeaders headers = request.getHeaders();
        headers.set("User-Agent", userAgent);
        headers.set("DNT", "1");
        return execution.execute(request, body);
    }
}
