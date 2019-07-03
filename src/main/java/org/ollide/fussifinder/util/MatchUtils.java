package org.ollide.fussifinder.util;

import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.MatchDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class MatchUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchUtils.class);

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

    public static String getLeagueKey(String fullLeague) {
        String league = fullLeague.toLowerCase();
        if (league.contains("kreisklasse")) {
            return "KK";
        }
        if (league.contains("kreisliga")) {
            return "KL";
        }
        if (league.contains("bezirksklasse") || league.contains("bezirksliga")) {
            return "BL";
        }
        if (league.contains("landesliga")) {
            return "LL";
        }
        if (league.contains("verbandsliga") || league.contains("oberliga")) {
            return "VL";
        }
        if (league.contains("pokal") || league.contains("turnier")) {
            return "P";
        }
        if (league.matches("([a-z]-)?fs")) {
            return "FS";
        }
        if (league.contains("regionalliga")) {
            return "RL";
        }
        if (league.contains("bundesliga")) {
            return "B";
        }
        LOGGER.warn("No league key found for league '{}'", fullLeague);
        return fullLeague;
    }

    public static String getTeamKey(String fullTeam) {
        if ("Herren".equals(fullTeam) || "Frauen".equals(fullTeam)) {
            return fullTeam;
        }

        String team = fullTeam.toLowerCase();
        if (team.contains("a-jun")) {
            return "A-Jun";
        }
        if (team.contains("b-jun")) {
            return "B-Jun";
        }
        if (team.contains("c-jun")) {
            return "C-Jun";
        }

        LOGGER.warn("No team key found for team '{}'", fullTeam);
        return fullTeam;
    }
}
