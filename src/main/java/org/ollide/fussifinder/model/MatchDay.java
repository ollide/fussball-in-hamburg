package org.ollide.fussifinder.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.ollide.fussifinder.http.serializer.IsoLocalDateSerializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MatchDay {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEEE, dd.MM.yy", Locale.GERMANY);

    @JsonSerialize(using = IsoLocalDateSerializer.class)
    private LocalDate day;
    private List<Match> matches = new ArrayList<>();

    public String formattedDay() {
        return FORMATTER.format(day);
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
