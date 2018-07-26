package org.ollide.fussifinder.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateUtil {

    private static final Pattern DATE_PATTERN = Pattern.compile(".*([0-9]{2}\\.[0-9]{2}\\.[0-9]{2}).*");
    private static final Pattern HOUR_PATTERN = Pattern.compile(".*([0-9]{2}:[0-9]{2}).*");

    /**
     * Used to format {@link LocalDate} to the required API query format.
     */
    private static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Used to parse the textual representation of a kickoff date to a {@link LocalDateTime}.
     */
    private static final DateTimeFormatter MATCH_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private DateUtil() {
        // do not instantiate
    }

    public static String normalizeDate(String date, String lastDate) {
        Matcher dateMatcher = DATE_PATTERN.matcher(date);
        Matcher hourMatcher = HOUR_PATTERN.matcher(date);

        if (dateMatcher.matches()) {
            if (hourMatcher.matches()) {
                return dateMatcher.group(1) + " " + hourMatcher.group(1);
            } else {
                return dateMatcher.group(1) + " 00:00";
            }
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

    public static LocalDateTime parseLocalDateTime(String date) {
        return LocalDateTime.parse(date, MATCH_DATE_TIME_FORMATTER);
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return MATCH_DATE_TIME_FORMATTER.format(dateTime);
    }

    public static LocalDate parseAPIDate(String date) {
        return LocalDate.parse(date, API_DATE_FORMATTER);
    }

    public static String formatLocalDateForAPI(LocalDate date) {
        return API_DATE_FORMATTER.format(date);
    }

}
