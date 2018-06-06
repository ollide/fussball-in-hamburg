package org.ollide.fussifinder;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class ResourceHelper {

    private ResourceHelper() {
        // do not instantiate
    }

    public static String readOverview(String fileName) throws IOException {
        InputStream resourceAsStream = ResourceHelper.class.getResourceAsStream("/html/overview/" + fileName);
        return StreamUtils.copyToString(resourceAsStream, Charset.defaultCharset());
    }

    public static String readMatches(String fileName) throws IOException {
        InputStream resourceAsStream = ResourceHelper.class.getResourceAsStream("/html/matches/" + fileName);
        return StreamUtils.copyToString(resourceAsStream, Charset.defaultCharset());
    }
}
