package org.ollide.fussifinder.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Team {

    public static final int HERREN = 1;
    public static final int HERREN_RESERVE = 39;
    public static final int JUNIOREN_A = 3;
    public static final int JUNIOREN_B = 6;

    public static final int FRAUEN = 4;
    public static final int JUNIORINNEN_A = 5;
    public static final int JUNIORINNEN_B = 7;

    public static Set<Integer> getDefaultTeams() {
        return new HashSet<>(Arrays.asList(HERREN, FRAUEN, JUNIOREN_A, JUNIORINNEN_A));
    }

    public static String getDefaultTeamsQuery() {
        return getDefaultTeams().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
