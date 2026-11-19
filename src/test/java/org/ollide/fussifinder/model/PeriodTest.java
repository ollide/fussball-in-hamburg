package org.ollide.fussifinder.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PeriodTest {

    @Test
    void widenedPeriods() {
        Period p = new Period();
        Set<Period> periods = p.widenedPeriods();
        assertEquals(2, periods.size());

        assertEquals(2, Period.fromString("T").widenedPeriods().size());
        assertEquals(1, Period.fromString("D3").widenedPeriods().size());
        assertEquals(0, Period.fromString("D7").widenedPeriods().size());
    }

    @Test
    void equalsHashCode() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(90);

        Period p1 = new Period(start, end);

        assertThat(p1)
                .isEqualTo(p1)
                .hasSameHashCodeAs(p1)
                .isNotEqualTo(null);

        Period p1Clone = new Period(start, end);
        assertThat(p1)
                .isEqualTo(p1Clone)
                .hasSameHashCodeAs(p1Clone);

        Period p2 = new Period(start, start.plusDays(45));
        Period p3 = new Period(start.plusDays(3), end);
        assertThat(p1)
                .isNotEqualTo(p2)
                .isNotEqualTo(p3)
                .doesNotHaveSameHashCodeAs(p2);
    }
}
