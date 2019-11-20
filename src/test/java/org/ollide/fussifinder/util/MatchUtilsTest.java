package org.ollide.fussifinder.util;

import org.junit.Test;
import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.MatchDay;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MatchUtilsTest {

    @Test
    public void splitIntoMatchDays() {
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0);

        Match day1Match1 = new Match();
        day1Match1.setDate(now.withHour(8));
        Match day1Match2 = new Match();
        day1Match2.setDate(now.withHour(16));

        Match day2Match1 = new Match();
        day2Match1.setDate(now.plusDays(1));

        List<Match> matches = Arrays.asList(day1Match1, day1Match2, day2Match1);

        List<MatchDay> matchDays = MatchUtils.splitIntoMatchDays(matches);
        assertEquals(2, matchDays.size());
        assertEquals(2, matchDays.get(0).getMatches().size());
        assertEquals(1, matchDays.get(1).getMatches().size());
    }

    @Test
    public void testGetLeagueKey() {
        // Friendlies
        assertEquals("FS", MatchUtils.getLeagueKey("K-FS"));
        assertEquals("FS", MatchUtils.getLeagueKey("B-FS"));
        assertEquals("FS", MatchUtils.getLeagueKey("L-FS"));
        assertEquals("FS", MatchUtils.getLeagueKey("X-FS"));

        // Cup
        assertEquals("P", MatchUtils.getLeagueKey("Kreisturnier"));
        assertEquals("P", MatchUtils.getLeagueKey("Verbandspokal"));

        // Verbandsliga
        assertEquals("VL", MatchUtils.getLeagueKey("Verbandsliga Hessen"));

        // Freizeitliga
        assertEquals("FS", MatchUtils.getLeagueKey("Verbandsoberliga (Freizeit)"));

        // Fall through
        final String unknownLeague = "Jupiler Bierliga";
        assertEquals(unknownLeague, MatchUtils.getLeagueKey(unknownLeague));
    }

    @Test
    public void testGetTeamKey() {
        assertEquals("C-Jun", MatchUtils.getTeamKey("C-Juniorinnen"));
        assertEquals("B-Jun", MatchUtils.getTeamKey("B-Junioren"));
        assertEquals("A-Jun", MatchUtils.getTeamKey("A-Jun."));

        final String unknownTeam = "Senioren Ü60";
        assertEquals(unknownTeam, MatchUtils.getTeamKey(unknownTeam));
    }
}
