package org.ollide.fussifinder.service;

import org.junit.Before;
import org.junit.Test;
import org.ollide.fussifinder.model.League;
import org.ollide.fussifinder.model.Match;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class MatchServiceTest {

    private MatchService matchService;

    @Before
    public void setUp() {
        matchService = new MatchService(mock(MatchCrawlService.class), mock(ParseService.class),
                mock(ZipService.class));
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
    }

    @Test
    public void isNotSpecialClass7Players() {
        Match normalMatch = new Match();
        normalMatch.setClubHome("TUS Altertal");
        normalMatch.setClubAway("SC Victoria");
        assertTrue(MatchService.isNotSpecialClass7Players(normalMatch));

        Match match7Players = new Match();
        match7Players.setClubHome("TUS Altertal 7er");
        match7Players.setClubAway("SC Victoria 7er");
        assertFalse(MatchService.isNotSpecialClass7Players(match7Players));
    }

    @Test
    public void isNotFutsal() {
        Match normalMatch = new Match();
        normalMatch.setLeague(League.VERBANDSLIGA.getName());
        assertTrue(MatchService.isNotFutsal(normalMatch));

        Match futsalMatch = new Match();
        futsalMatch.setLeague("Futsal Regionalliga");
        assertFalse(MatchService.isNotFutsal(futsalMatch));
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
