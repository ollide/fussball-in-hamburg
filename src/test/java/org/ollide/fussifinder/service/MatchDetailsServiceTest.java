package org.ollide.fussifinder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ollide.fussifinder.ResourceHelper;
import org.ollide.fussifinder.api.MatchClient;
import org.ollide.fussifinder.model.MatchDetails;
import org.ollide.fussifinder.model.Pitch;
import retrofit2.mock.Calls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchDetailsServiceTest {

    private MatchDetailsService matchDetailsService;
    private final MatchClient matchClient = mock(MatchClient.class);

    // === Artificial Pitches ===
    private static final String LOCATION_1 = "Kunstrasenplatz, Sachsenweg 2 (Kunstrasen), Sachsenweg 78, 22455 Hamburg";
    private static final String LOCATION_2 = "Kunstrasenplatz, Beethovenstraße, Beethovenstr. 51, 22083 Hamburg";
    private static final String LOCATION_3 = "Kunstrasenplatz, Langenfort 1, Langenfort 70, 22307 Hamburg";
    private static final String LOCATION_4 = "Kunstrasenplatz, Berner Heerweg 190/II, Berner Heerweg 190, 22159 Hamburg";
    private static final String LOCATION_5 = "Kunstrasenplatz, Stad. Allhorn, Ahrensburger Weg 28, 22359 Hamburg";
    private static final String LOCATION_6 = "Kunstrasenplatz, Stadion Hoheluft, Lokstedter Steindamm 87, 22529 Hamburg";

    // === Indoor ===
    private static final String LOCATION_7 = "Halle, Walter-Rückert-Halle, Meessen 32, 22113 Oststeinbek";
    private static final String LOCATION_8 = "Halle, Heimgartenschule Reesenbüttel, Reesenbüttler Redder 4, 22926 Ahrensburg";

    // === Grass ===
    private static final String LOCATION_9 = "Rasenplatz, Edmund-Plambeck-Stadion, Ochsenzoller Str. 58, 22848 Norderstedt";
    private static final String LOCATION_10 = "Rasenplatz, Millerntor-Stadion, Heiligengeistfeld, 20359 Hamburg";
    private static final String LOCATION_11 = "Rasenplatz, Merkur Spiel–Arena, Arena-Str. 1, 40474 Düsseldorf";
    // 4 segments
    private static final String LOCATION_12 = "Rasenplatz, Stadiongelände, Weserstadion, Franz-Böhmert-Str. 7, 28205 Bremen";

    // === Clay ===
    private static final String LOCATION_13 = "Hartplatz, Grunewaldstr.61/I, Grunewaldstr. 61, 22149 Hamburg";

    @BeforeEach
    void setUp() {
        MatchClientCrawlService matchClientCrawlService = new MatchClientCrawlService(matchClient);
        matchDetailsService = new MatchDetailsService(matchClientCrawlService);
    }

    @Test
    void testGetMatchDetails() throws Exception {
        String html = ResourceHelper.readMatchDetails("matchdetails_1.html");
        when(matchClient.matchDetails(anyString())).thenReturn(Calls.response(html));

        MatchDetails matchDetails = matchDetailsService.getMatchDetails("234345423");
        assertEquals(Pitch.ARTIFICIAL, matchDetails.getPitch());
        assertEquals("Sachsenweg 78, 22455 Hamburg", matchDetails.getAddress());
        assertEquals("Sachsenweg 2", matchDetails.getGround());
        assertEquals("Freundschaftsspiele Hamburg", matchDetails.getCompetition());
    }

    @Test
    void testGetMatchDetailsError() throws Exception {
        String html = ResourceHelper.readMatchDetails("matchdetails_1.html");
        when(matchClient.matchDetails(anyString())).thenReturn(Calls.response(html));

        MatchDetails matchDetails = matchDetailsService.getMatchDetails("234345423");
        assertEquals(Pitch.ARTIFICIAL, matchDetails.getPitch());
        assertEquals("Sachsenweg 78, 22455 Hamburg", matchDetails.getAddress());
        assertEquals("Sachsenweg 2", matchDetails.getGround());
        assertEquals("Freundschaftsspiele Hamburg", matchDetails.getCompetition());
    }

    @Test
    void parseMatchDetails() throws Exception {
        String html = ResourceHelper.readMatchDetails("matchdetails_1.html");
        MatchDetails matchDetails = matchDetailsService.parseMatchDetails(html);

        assertEquals(Pitch.ARTIFICIAL, matchDetails.getPitch());
        assertEquals("Sachsenweg 78, 22455 Hamburg", matchDetails.getAddress());
        assertEquals("Sachsenweg 2", matchDetails.getGround());
        assertEquals("Freundschaftsspiele Hamburg", matchDetails.getCompetition());
    }

    @Test
    void parseMatchDetailsSuspended() throws Exception {
        String html = ResourceHelper.readMatchDetails("matchdetails_2.html");
        MatchDetails matchDetails = matchDetailsService.parseMatchDetails(html);

        assertNull(matchDetails.getPitch());
        assertNull(matchDetails.getAddress());
        assertNull(matchDetails.getGround());
        assertEquals("Freundschaftsspiele Bezirksebene Hamburg", matchDetails.getCompetition());
    }

    @Test
    void testParseAddress() {
        assertNull(matchDetailsService.parseAddress(""));
        assertNull(matchDetailsService.parseAddress("This is not an address."));

        assertEquals("Sachsenweg 78, 22455 Hamburg", matchDetailsService.parseAddress(LOCATION_1));
        assertEquals("Beethovenstraße 51, 22083 Hamburg", matchDetailsService.parseAddress(LOCATION_2));

        assertEquals("Franz-Böhmert-Str. 7, 28205 Bremen", matchDetailsService.parseAddress(LOCATION_12));
    }

    @Test
    void testParsePitch() {
        assertNull(matchDetailsService.parsePitch(""));
        assertNull(matchDetailsService.parsePitch("This is not a pitch."));

        assertEquals(Pitch.ARTIFICIAL, matchDetailsService.parsePitch(LOCATION_1));
        assertEquals(Pitch.ARTIFICIAL, matchDetailsService.parsePitch(LOCATION_2));
        assertEquals(Pitch.ARTIFICIAL, matchDetailsService.parsePitch(LOCATION_3));
        assertEquals(Pitch.ARTIFICIAL, matchDetailsService.parsePitch(LOCATION_4));
        assertEquals(Pitch.ARTIFICIAL, matchDetailsService.parsePitch(LOCATION_5));
        assertEquals(Pitch.ARTIFICIAL, matchDetailsService.parsePitch(LOCATION_6));

        assertEquals(Pitch.INDOOR, matchDetailsService.parsePitch(LOCATION_7));
        assertEquals(Pitch.INDOOR, matchDetailsService.parsePitch(LOCATION_8));

        assertEquals(Pitch.GRASS, matchDetailsService.parsePitch(LOCATION_9));
        assertEquals(Pitch.GRASS, matchDetailsService.parsePitch(LOCATION_10));
        assertEquals(Pitch.GRASS, matchDetailsService.parsePitch(LOCATION_11));
        assertEquals(Pitch.GRASS, matchDetailsService.parsePitch(LOCATION_12));

        assertEquals(Pitch.CLAY, matchDetailsService.parsePitch(LOCATION_13));
    }

    @Test
    void testParseGround() {
        assertNull(matchDetailsService.parseGround(""));
        assertNull(matchDetailsService.parseGround("This is not a ground."));

        assertEquals("Sachsenweg 2", matchDetailsService.parseGround(LOCATION_1));
        assertEquals("Beethovenstraße", matchDetailsService.parseGround(LOCATION_2));
        assertEquals("Langenfort 1", matchDetailsService.parseGround(LOCATION_3));
        assertEquals("Berner Heerweg 190/II", matchDetailsService.parseGround(LOCATION_4));
        assertEquals("Stadion Allhorn", matchDetailsService.parseGround(LOCATION_5));
        assertEquals("Stadion Hoheluft", matchDetailsService.parseGround(LOCATION_6));

        assertEquals("Walter-Rückert-Halle", matchDetailsService.parseGround(LOCATION_7));

        assertEquals("Millerntor-Stadion", matchDetailsService.parseGround(LOCATION_10));
    }
}
