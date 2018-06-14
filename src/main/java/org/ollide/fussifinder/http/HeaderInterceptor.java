package org.ollide.fussifinder.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HeaderInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderInterceptor.class);

    private final String userAgent;

    public HeaderInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        LOGGER.debug("Applying required headers.");

        Request requestWithHeaders = request.newBuilder()
                .removeHeader("User-Agent")
                .addHeader("DNT", "1")
                .addHeader("User-Agent", userAgent).build();

        return chain.proceed(requestWithHeaders);
    }
}
