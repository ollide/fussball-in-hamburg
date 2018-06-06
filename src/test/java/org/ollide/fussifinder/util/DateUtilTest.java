package org.ollide.fussifinder.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateUtilTest {

    @Test
    public void normalizeDate() {
        String dateWithTime = "10.10.12 15:00";

        // Full-date without previous date leads to the same full-date
        assertEquals(dateWithTime, DateUtil.normalizeDate(dateWithTime, null));

        // Full-date with existing lastDate leads to the same full-date
        assertEquals(dateWithTime, DateUtil.normalizeDate(dateWithTime, dateWithTime));

        String timeOnly = "15:00";
        // Time-only-date uses the date from the previous full-date
        assertEquals(dateWithTime, DateUtil.normalizeDate(timeOnly, dateWithTime));

        // Whitespace and other characters before, in-between or afterwards are removed
        assertEquals(dateWithTime, DateUtil.normalizeDate("Mittwoch, 10.10.12 -> 15:00 Uhr", null));
    }
}
