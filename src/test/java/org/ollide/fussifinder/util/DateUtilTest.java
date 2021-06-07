package org.ollide.fussifinder.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void testNormalizeAndParseAsteriskKickoff() {
        String date = "So, 10.10.12 | **";

        String normalizedDate = DateUtil.normalizeDate(date, null);
        assertEquals("10.10.12 00:00", normalizedDate);

        LocalDateTime localDateTime = DateUtil.parseLocalDateTime(normalizedDate);
        assertEquals(0, localDateTime.getHour());
        assertEquals(0, localDateTime.getMinute());
    }

    @Test
    public void testTimeNotPublishedAndNormalize() {
        String date1 = "So, 10.10.12 | **";
        String date2 = "**";

        String date1Normalized = DateUtil.normalizeDate(date1, null);
        String date2Normalized = DateUtil.normalizeDate(date2, date1Normalized);

        assertEquals("10.10.12 00:00", date1Normalized);
        assertEquals("10.10.12 00:00", date2Normalized);
    }

    @Test
    public void testInvalidDates() {
        assertEquals("", DateUtil.normalizeDate("No date", null));
        assertEquals("", DateUtil.normalizeDate("No date", "No date"));
        assertEquals("", DateUtil.normalizeDate("No date", "10.10.12 15:23"));
    }
}
