package org.ollide.fussifinder.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

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
}
