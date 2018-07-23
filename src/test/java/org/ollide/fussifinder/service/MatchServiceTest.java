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
    }

}
