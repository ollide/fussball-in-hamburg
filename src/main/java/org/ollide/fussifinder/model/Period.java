package org.ollide.fussifinder.model;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Period {

    private static final Pattern DATE_PATTERN = Pattern.compile("^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String TODAY = "T";
    private static final String DAYS_3 = "D3";
    private static final String DAYS_7 = "D7";

    private LocalDate start;
    private LocalDate end;

    public Period() {
        this(LocalDate.now(), LocalDate.now());
    }

    public Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public static Period fromString(String p) {
        if (StringUtils.isEmpty(p)) {
            return new Period();
        }

        LocalDate start;
        LocalDate end;

        if (DATE_PATTERN.matcher(p).matches()) {
            start = LocalDate.parse(p, DATE_FORMATTER);
            end = start.plusDays(1);
        } else {
            start = LocalDate.now();
            long days;
            switch (p) {
                case DAYS_3:
                    days = 2;
                    break;
                case DAYS_7:
                    days = 6;
                    break;
                default:
                    days = 0;
            }
            end = start.plusDays(days);
        }

        return new Period(start, end);
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}
