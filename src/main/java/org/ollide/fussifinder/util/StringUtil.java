package org.ollide.fussifinder.util;

import org.springframework.util.StringUtils;

public final class StringUtil {

    private StringUtil() {
        // do not instantiate
    }

    public static String capitalizeFirstLetter(String word) {
        if (StringUtils.isEmpty(word)) {
            return word;
        }

        String output = word.substring(0, 1).toUpperCase();
        if (word.length() > 1) {
            output += word.substring(1);
        }
        return output;
    }
}
