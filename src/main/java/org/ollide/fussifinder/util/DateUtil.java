package org.ollide.fussifinder.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateUtil {

    private static final Pattern DATE_PATTERN = Pattern.compile(".*([0-9]{2}\\.[0-9]{2}\\.[0-9]{2}).*");
    private static final Pattern HOUR_PATTERN = Pattern.compile(".*([0-9]{2}:[0-9]{2}).*");

    private DateUtil() {
        // do not instantiate
    }

    public static String normalizeDate(String date, String lastDate) {
        Matcher dateMatcher = DATE_PATTERN.matcher(date);
        Matcher hourMatcher = HOUR_PATTERN.matcher(date);

        if (dateMatcher.matches() && hourMatcher.matches()) {
            return dateMatcher.group(1) + " " + hourMatcher.group(1);
        } else {
            if (HOUR_PATTERN.matcher(date).matches() && lastDate != null) {
                Matcher matcher = DATE_PATTERN.matcher(lastDate);
                if (matcher.matches()) {
                    return matcher.group(1) + " " + date;
                }
            }
        }
        return "";
    }

}
