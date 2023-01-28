package org.ollide.fussifinder.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void equalsHashCode() {
        String id = UUID.randomUUID().toString();
        Match m1 = new Match();
        m1.setId(id);

        assertThat(m1)
                .isEqualTo(m1)
                .hasSameHashCodeAs(m1)
                .isNotEqualTo(null);

        Match m1Clone = new Match();
        m1Clone.setId(id);
        assertThat(m1)
                .isEqualTo(m1Clone)
                .hasSameHashCodeAs(m1Clone);

        Match m2 = new Match();
        m2.setId(UUID.randomUUID().toString());
        assertThat(m1)
                .isNotEqualTo(m2)
                .doesNotHaveSameHashCodeAs(m2);
    }
}
