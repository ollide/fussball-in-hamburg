package org.ollide.fussifinder.service;

import org.junit.jupiter.api.Test;
import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.MatchDay;
import org.ollide.fussifinder.model.ZIPCode;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockDataServiceTest {

    private final MockDataService mockDataService100 = new MockDataService(1.0);
    private final MockDataService mockDataService0 = new MockDataService(0);

    @Test
    void getRandomZips() {
        final String zip3 = "202";
        List<ZIPCode> randomZips = mockDataService100.getRandomZips(zip3);

        assertTrue(randomZips.size() > 0);
        assertTrue(randomZips.size() < 8);
        assertTrue(randomZips.stream()
                .map(ZIPCode::getPlz)
                .allMatch(z -> z.startsWith(zip3) && z.length() == 5));

        assertTrue(mockDataService0.getRandomZips(zip3).isEmpty());
    }

    @Test
    void getRandomMatches() {
        final String dateTo = "2019-10-28";
        final String dateFrom = "2019-10-21";
        List<MatchDay> randomMatches = mockDataService100.getRandomMatches(dateTo, dateFrom);
        assertEquals(7, randomMatches.size());
    }

    @Test
    void getRandomMatchesInvalid() {
        final String dateTo = "2019-10-21";
        final String dateFrom = "2019-10-27";
        assertThrows(IllegalArgumentException.class, () -> mockDataService100.getRandomMatches(dateTo, dateFrom));
    }

    @Test
    void getRandomMatch() {
        LocalDate now = LocalDate.now();
        Match randomMatch = mockDataService100.getRandomMatch(now);

        assertNotNull(randomMatch.getId());
        assertNotNull(randomMatch.getDate());
        assertNotNull(randomMatch.getTeamType());
        assertNotNull(randomMatch.getLeague());
        assertNotNull(randomMatch.getClubHome());
        assertNotNull(randomMatch.getClubAway());

        assertNull(randomMatch.getScore());
        assertNull(randomMatch.getUrl());
        assertNull(randomMatch.getTeamTypeKey());
        assertNull(randomMatch.getLeagueKey());

        assertEquals(now, randomMatch.getDate().toLocalDate());
    }

    @Test
    void getRandomLocation() {
        assertTrue(StringUtils.hasLength(mockDataService100.getRandomLocation()));
    }

    @Test
    void getRandomCompetition() {
        assertTrue(StringUtils.hasLength(mockDataService100.getRandomCompetition()));
    }
}