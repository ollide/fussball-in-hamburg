package org.ollide.fussifinder.service;

import org.junit.Before;
import org.junit.Test;
import org.ollide.fussifinder.model.Match;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class MatchServiceTest {

    private MatchService matchService;

    @Before
    public void setUp() {
        matchService = new MatchService(mock(MatchCrawlService.class), mock(ParseService.class));
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

        Match verbandsliga = new Match();
        verbandsliga.setLeague("Verbandsliga");
        assertEquals("Verbandsliga", matchService.shortenLeague(verbandsliga).getLeague());
    }
}
