package org.ollide.fussifinder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.ollide.fussifinder.ResourceHelper;
import org.ollide.fussifinder.config.AppConfig;
import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.util.DateUtil;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ParseServiceTest {

    private ParseService parseService;

    private static final String MATCH_ID = "0568E7K90O003330VS5489B3VVRESQAR";
    private static final String MATCH_UTL = "http://www.example.org/spiel/abc1-def2/-/spiel/" + MATCH_ID;

    @Before
    public void setUp() {
        ObjectMapper mapper = new AppConfig().objectMapper();
        parseService = new ParseService(mapper);
    }

    @Test
    public void parseZipsWithMatches() throws IOException {
        String html = ResourceHelper.readOverview("2_results.html");
        List<String> zips = parseService.parseZipsWithMatches(html);
        assertEquals("ParseService should find 2 ZIP results", 2, zips.size());
    }

    @Test
    public void parseMatchesForZip() throws IOException {
        String html = ResourceHelper.readMatches("2_matches_2_days.html");
        List<Match> matches = parseService.parseMatchesForZip(html);
        assertEquals("ParseService should find 2 matches", 2, matches.size());

        Match match1 = matches.get(0);
        assertEquals(MATCH_ID, match1.getId());
        assertEquals("09.06.18 12:30", DateUtil.formatLocalDateTime(match1.getDate()));
        assertEquals("D-Junioren", match1.getTeamType());
        assertEquals("Kreisklasse", match1.getLeague());
        assertEquals("Home-Team-Name", match1.getClubHome());
        assertEquals("Away-Team-Name", match1.getClubAway());
        assertEquals(MATCH_UTL, match1.getUrl());

        Match match2 = matches.get(1);
        assertEquals(MATCH_ID, match2.getId());
        assertEquals("10.06.18 11:30", DateUtil.formatLocalDateTime(match2.getDate()));
        assertEquals("A-Junioren", match2.getTeamType());
        assertEquals("Bezirksliga", match2.getLeague());
        assertEquals("Heimteam", match2.getClubHome());
        assertEquals("Auswärtsteam", match2.getClubAway());
        assertEquals(MATCH_UTL, match2.getUrl());
    }

    @Test
    public void parseMatchesForZipSameDay() throws IOException {
        String html = ResourceHelper.readMatches("2_matches_1_day.html");
        List<Match> matches = parseService.parseMatchesForZip(html);

        assertEquals("ParseService should find 2 matches", 2, matches.size());
        assertEquals("09.06.18 12:30", DateUtil.formatLocalDateTime(matches.get(0).getDate()));
        // Raw data only says '18:00', we derive the date from the previous match
        assertEquals("09.06.18 18:00", DateUtil.formatLocalDateTime(matches.get(1).getDate()));
    }

}
