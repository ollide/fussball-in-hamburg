package org.ollide.fussifinder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.ollide.fussifinder.ResourceHelper;
import org.ollide.fussifinder.config.AppConfig;
import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.Period;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MatchServiceTest {

    private MatchService matchService;
    private MatchCrawlService matchCrawlService;

    @Before
    public void setUp() {
        matchCrawlService = mock(MatchCrawlService.class);
        ObjectMapper objectMapper = new AppConfig().objectMapper();
        matchService = new MatchService(matchCrawlService, new ParseService(objectMapper),
                mock(ZipService.class));
    }

    @Test
    public void getMatches() throws IOException {
        Collection<String> zips = Arrays.asList("20359", "22525");

        String htmlOverview = ResourceHelper.readOverview("2_results.html");
        when(matchCrawlService.getMatchCalendar(anyString(), anyString(), eq("203"))).thenReturn(htmlOverview);
        String htmlMatches = ResourceHelper.readMatches("2_matches_2_days.html");
        when(matchCrawlService.getMatchCalendar(anyString(), anyString(), eq("20359"))).thenReturn(htmlMatches);

        List<Match> matches = matchService.getMatches(zips, new Period());
        assertEquals(2, matches.size());
    }

    @Test
    public void getMatchesWithShortZip() throws IOException {
        final String shortZip3 = "203";
        final String shortZip4 = "2035";
        final String fullZip = "20359";

        String htmlOverview = ResourceHelper.readOverview("2_results.html");
        when(matchCrawlService.getMatchCalendar(anyString(), anyString(), eq(shortZip3))).thenReturn(htmlOverview);
        when(matchCrawlService.getMatchCalendar(anyString(), anyString(), eq(shortZip4))).thenReturn(htmlOverview);
        String htmlMatches = ResourceHelper.readMatches("2_matches_2_days.html");
        when(matchCrawlService.getMatchCalendar(anyString(), anyString(), eq(fullZip))).thenReturn(htmlMatches);

        // query with 3-digits zip
        List<Match> matches = matchService.getMatches(Collections.singleton(shortZip3), new Period());
        assertEquals(2, matches.size());

        // query with 4-digits zip
        matches = matchService.getMatches(Collections.singleton(shortZip4), new Period());
        assertEquals(2, matches.size());
    }

    @Test
    public void shortenLeague() {
        Match kFS = new Match();
        kFS.setLeague("Kreisfreundschaftsspiele");
        assertEquals("K-FS", matchService.shortenLeague(kFS).getLeague());

        Match bFS = new Match();
        bFS.setLeague("Bezirksfreundschaftsspiele");
        assertEquals("B-FS", matchService.shortenLeague(bFS).getLeague());

        Match lFS = new Match();
        lFS.setLeague("Landesfreundschaftsspiele");
        assertEquals("L-FS", matchService.shortenLeague(lFS).getLeague());

        Match rFS = new Match();
        rFS.setLeague("regionale Freundschaftsspiele");
        assertEquals("r-FS", matchService.shortenLeague(rFS).getLeague());

        Match fs = new Match();
        fs.setLeague("Freundschaftsspiele");
        assertEquals("FS", matchService.shortenLeague(fs).getLeague());

        Match verbandsliga = new Match();
        verbandsliga.setLeague("Verbandsliga");
        assertEquals("Verbandsliga", matchService.shortenLeague(verbandsliga).getLeague());

        Match kreisklasse = new Match();
        kreisklasse.setLeague("3.Kreisklasse");
        assertEquals("Kreisklasse", matchService.shortenLeague(kreisklasse).getLeague());

        kreisklasse.setLeague("Kreisklasse B");
        assertEquals("Kreisklasse", matchService.shortenLeague(kreisklasse).getLeague());

        kreisklasse.setLeague("2. Kreisklasse A");
        assertEquals("Kreisklasse", matchService.shortenLeague(kreisklasse).getLeague());

        Match kreisligaB = new Match();
        kreisligaB.setLeague("2.Kreisliga (B)");
        assertEquals("Kreisliga", matchService.shortenLeague(kreisligaB).getLeague());

        Match kreisStadtliga = new Match();
        kreisStadtliga.setLeague("1.Kreisliga (A) / Stadtliga");
        assertEquals("Kreisliga", matchService.shortenLeague(kreisStadtliga).getLeague());

        Match bezirksklasse = new Match();
        bezirksklasse.setLeague("1.Bezirksklasse");
        assertEquals("Bezirksklasse", matchService.shortenLeague(bezirksklasse).getLeague());

        Match oberliga = new Match();
        oberliga.setLeague("Oberliga Niedersachsen");
        assertEquals("Oberliga", matchService.shortenLeague(oberliga).getLeague());
    }

    @Test
    public void shortenTeamNames() {
        Match j1 = new Match();
        j1.setClubHome("Walddörfer 1.A (J1)");
        j1.setClubAway("Rahlstedt 1.A (J1)");
        j1 = matchService.shortenTeamNames(j1);
        assertEquals("Walddörfer 1.A", j1.getClubHome());
        assertEquals("Rahlstedt 1.A", j1.getClubAway());

        Match a1 = new Match();
        a1.setClubHome("TSV Sasel");
        a1.setClubAway("BU 1.A (A1)");
        a1 = matchService.shortenTeamNames(a1);
        assertEquals("TSV Sasel", a1.getClubHome());
        assertEquals("BU 1.A", a1.getClubAway());

        Match j1A2 = new Match();
        j1A2.setClubHome("Altenwerder 1.A (J1)");
        j1A2.setClubAway("Vorw. Wacker 3.B (A2)");
        j1A2 = matchService.shortenTeamNames(j1A2);
        assertEquals("Altenwerder 1.A", j1A2.getClubHome());
        assertEquals("Vorw. Wacker 3.B", j1A2.getClubAway());

        Match a1Sg = new Match();
        a1Sg.setClubHome("SC V. M. 1.A");
        a1Sg.setClubAway("Altengamme/Börnsen 1.A (A1) SG");
        a1Sg = matchService.shortenTeamNames(a1Sg);
        assertEquals("SC V. M. 1.A", a1Sg.getClubHome());
        assertEquals("Altengamme/Börnsen 1.A SG", a1Sg.getClubAway());
    }

    @Test
    public void isNotSpecialClass7Players() {
        Match normalMatch = new Match();
        normalMatch.setClubHome("TUS Altertal");
        normalMatch.setClubAway("SC Victoria");
        assertTrue(MatchService.isNotSpecialClass(normalMatch));

        Match match7Players = new Match();
        match7Players.setClubHome("TUS Altertal 7er");
        match7Players.setClubAway("SC Victoria 7er");
        assertFalse(MatchService.isNotSpecialClass(match7Players));

        Match match9Players = new Match();
        match9Players.setClubHome("Witzhaver SV II");
        match9Players.setClubAway("Delingsdorfer SV III (9er)");
        assertFalse(MatchService.isNotSpecialClass(match9Players));

        Match match9PlayersStart = new Match();
        match9PlayersStart.setClubHome("9er U17 JSG TSV Auetal/MTV Egestorf");
        match9PlayersStart.setClubAway("U16 TSV Winsen/Luhe");
        assertFalse(MatchService.isNotSpecialClass(match9PlayersStart));

        Match match9PlayersApostrophe = new Match();
        match9PlayersApostrophe.setClubHome("JSG Geest (U19)");
        match9PlayersApostrophe.setClubAway("SV Burweg (U19) 9'er");
        assertFalse(MatchService.isNotSpecialClass(match9PlayersApostrophe));

        Match matchSpecialClass = new Match();
        matchSpecialClass.setLeague("Sonderklasse");
        assertFalse(MatchService.isNotSpecialClass(matchSpecialClass));
    }

    @Test
    public void isNotFutsal() {
        Match normalMatch = new Match();
        normalMatch.setLeague("Verbandsliga");
        assertTrue(MatchService.isNotFutsal(normalMatch));

        Match futsalMatch = new Match();
        futsalMatch.setLeague("Futsal Regionalliga");
        assertFalse(MatchService.isNotFutsal(futsalMatch));
    }

    @Test
    public void isNotEFoot() {
        Match normalMatch = new Match();
        normalMatch.setClubHome("Viktoria Harburg 1.");
        normalMatch.setClubAway("TSG Bergedorf 2.");
        assertTrue(MatchService.isNotEFoot(normalMatch));

        Match eFootMatch = new Match();
        eFootMatch.setClubHome("Eimsbüttel I (eFoot)");
        eFootMatch.setClubAway("Komet Blankenese I (eFoot)");
        assertFalse(MatchService.isNotEFoot(eFootMatch));
    }

    @Test
    public void isNotCancelled() {
        Match normalMatch = new Match();
        normalMatch.setScore("");
        assertTrue(MatchService.isNotCancelled(normalMatch));

        Match cancelledMatch = new Match();
        cancelledMatch.setScore("Absetzung");
        assertFalse(MatchService.isNotCancelled(cancelledMatch));

        Match noShowMatch = new Match();
        noShowMatch.setScore("Absetzung HEIM");
        assertFalse(MatchService.isNotCancelled(noShowMatch));
    }

    @Test
    public void testIsNotIndoor() {
        Match normalMatch = new Match();
        normalMatch.setScore("");
        assertTrue(MatchService.isNotIndoor(normalMatch));

        Match awayTeamSingleMatch = new Match();
        awayTeamSingleMatch.setClubAway("FC Hallenbach 05");
        assertTrue(MatchService.isNotIndoor(awayTeamSingleMatch));

        Match indoorLeagueMatch = new Match();
        indoorLeagueMatch.setLeague("Hallen-Kreisturnier");
        assertFalse(MatchService.isNotIndoor(indoorLeagueMatch));

        Match indoorAwayTeamMatch = new Match();
        indoorAwayTeamMatch.setClubAway("Hallenturnier bis ca. 19.45 Uhr");
        assertFalse(MatchService.isNotIndoor(indoorAwayTeamMatch));
    }

}
