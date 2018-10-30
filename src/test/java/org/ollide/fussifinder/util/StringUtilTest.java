package org.ollide.fussifinder.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilTest {

    @Test
    public void capitalizeFirstLetter() {
        assertEquals("Lübeck", StringUtil.capitalizeFirstLetter("lübeck"));
        assertEquals("Hamburg", StringUtil.capitalizeFirstLetter("Hamburg"));
        assertEquals("O", StringUtil.capitalizeFirstLetter("o"));
        assertEquals("", StringUtil.capitalizeFirstLetter(""));
    }
}
