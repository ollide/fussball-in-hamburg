package org.ollide.fussifinder.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Team {

    public static final Team HERREN = new Team(1, "Herren");
    public static final Team JUNIOREN_A = new Team(3, "A-Junioren");
    public static final Team JUNIOREN_B = new Team(6, "B-Junioren");
    public static final Team JUNIOREN_C = new Team(8, "C-Junioren");
    public static final Team HERREN_FREIZEIT = new Team(22, "Freizeitsport");

    public static final Team FRAUEN = new Team(4, "Frauen");
    public static final Team JUNIORINNEN_A = new Team(5, "A-Juniorinnen");
    public static final Team JUNIORINNEN_B = new Team(7, "B-Juniorinnen");
    public static final Team JUNIORINNEN_C = new Team(9, "C-Juniorinnen");

    private final int id;
    private final String name;

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<Team> getAllTeams() {
        return Arrays.asList(HERREN, FRAUEN, JUNIOREN_A, JUNIORINNEN_A, JUNIOREN_B, JUNIORINNEN_B, HERREN_FREIZEIT);
    }

    public static String getDefaultTeamsQuery() {
        return getAllTeams().stream()
                .map(Team::getTeamId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public int getTeamId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
