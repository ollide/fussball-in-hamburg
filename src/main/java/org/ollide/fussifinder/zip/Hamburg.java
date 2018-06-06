package org.ollide.fussifinder.zip;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Hamburg {

    private static final Set<String> ZIP_3 = new HashSet<>();
    static  {
        ZIP_3.addAll(Arrays.asList(
                "200", "201", "202", "203", "204", "205",
                "210", "211",
                "220", "221", "222", "223", "224", "225", "226", "227"));
    }

    public static Set<String> getAllZIP3() {
        return new HashSet<>(ZIP_3);
    }
}
