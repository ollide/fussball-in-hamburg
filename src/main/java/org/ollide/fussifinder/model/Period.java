package org.ollide.fussifinder.model;

import org.jspecify.annotations.NonNull;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public record Period(@NonNull LocalDate start, @NonNull LocalDate end) {

    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String TODAY = "T";
    private static final String DAYS_3 = "D3";
    private static final String DAYS_7 = "D7";

    public Period() {
        this(LocalDate.now(), LocalDate.now());
    }

    public static Period fromString(String p) {
        if (!StringUtils.hasText(p)) {
            return new Period();
        }

        LocalDate start;
        LocalDate end;

        if (DATE_PATTERN.matcher(p).matches()) {
            start = LocalDate.parse(p, DATE_FORMATTER);
            end = start.plusDays(1);
        } else {
            start = LocalDate.now();
            long days = switch (p) {
                case DAYS_3 -> 2;
                case DAYS_7 -> 6;
                case TODAY -> 0;
                default -> 0;
            };
            end = start.plusDays(days);
        }

        return new Period(start, end);
    }

    public Set<Period> widenedPeriods() {
        Set<Period> periods = new HashSet<>();
        if (start.equals(end)) {
            // T -> D3 & D7
            periods.add(new Period(start, start.plusDays(2)));
            periods.add(new Period(start, start.plusDays(6)));
        } else if (start.plusDays(2).equals(end)) {
            // D3 -> D7
            periods.add(new Period(start, start.plusDays(6)));
        }
        return periods;
    }
}
