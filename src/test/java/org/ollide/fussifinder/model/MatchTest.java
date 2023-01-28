package org.ollide.fussifinder.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    void compareTo() {
        LocalDateTime now = LocalDateTime.now();
        Match matchNow = new Match();
        matchNow.setId("1");
        matchNow.setClubHome("Home Early");
        matchNow.setDate(now);

        Match matchNow2 = new Match();
        matchNow2.setId("0");
        matchNow2.setClubAway("Away Early");
        matchNow2.setDate(now);

        Match matchLater = new Match();
        matchLater.setId("2");
        matchLater.setClubHome("Home late");
        matchLater.setDate(now.plusHours(2));

        List<Match> sorted = Stream.of(matchNow, matchLater, matchNow2).sorted().collect(Collectors.toList());

        assertEquals("0", sorted.get(0).getId());
        assertEquals("1", sorted.get(1).getId());
        assertEquals("2", sorted.get(2).getId());
    }
}
