package org.ollide.fussifinder.util;

import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.MatchDay;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class MatchUtils {

    private MatchUtils() {
        // do not instantiate
    }

    public static List<MatchDay> splitIntoMatchDays(List<Match> matches) {
        List<MatchDay> matchDayList = new ArrayList<>();
        MatchDay matchDay = null;

        for (Match match : matches) {
            LocalDate matchDate = match.getDate().toLocalDate();
            if (matchDay == null || !matchDate.equals(matchDay.getDay())) {
                matchDay = new MatchDay();
                matchDay.setDay(matchDate);
                matchDayList.add(matchDay);
            }
            matchDay.getMatches().add(match);
        }
        return matchDayList;
    }
}
