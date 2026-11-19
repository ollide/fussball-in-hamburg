package org.ollide.fussifinder.util;

import org.jspecify.annotations.Nullable;

public final class StringUtil {

    private StringUtil() {
        // do not instantiate
    }

    public static boolean containsAllIgnoreCase(@Nullable String str, String... toContain) {
        if (str == null) {
            return false;
        }
        String text = str.toLowerCase();
        for (String tc : toContain) {
            if (!text.contains(tc)) {
                return false;
            }
        }
        return true;
    }
}
