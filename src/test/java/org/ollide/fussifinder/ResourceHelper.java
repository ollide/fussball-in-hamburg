package org.ollide.fussifinder;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class ResourceHelper {

    private static final String HTML_OVERVIEW = "/html/overview/";
    private static final String HTML_MATCHES = "/html/matches/";
    private static final String HTML_MATCHDETAILS = "/html/matchdetails/";

    private ResourceHelper() {
        // do not instantiate
    }

    public static String readOverview(String fileName) throws IOException {
        return readFile(HTML_OVERVIEW + fileName);
    }

    public static String readMatches(String fileName) throws IOException {
        return readFile(HTML_MATCHES + fileName);
    }

    public static String readMatchDetails(String fileName) throws IOException {
        return readFile(HTML_MATCHDETAILS + fileName);
    }

    public static String readFile(String fileName) throws IOException {
        InputStream resourceAsStream = ResourceHelper.class.getResourceAsStream(fileName);
        return StreamUtils.copyToString(resourceAsStream, Charset.defaultCharset());
    }
}
