package org.ollide.fussifinder.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class League {

    public static final League VERBANDSLIGA = new League("Verbandsliga", true);
    public static final League LANDESLIGA = new League("Landesliga", true);
    public static final League BEZIRKSLIGA = new League("Bezirksliga", true);
    public static final League KREISLIGA = new League("Kreisliga");
    public static final League KREISKLASSE = new League("Kreisklasse");
    public static final League FREUNDSCHAFTSSPIEL = new League("FS", true);

    private String name;
    private boolean active = false;

    public League(String name) {
        this.name = name;
    }

    public League(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    public static List<League> getAllLeagues() {
        return Arrays.asList(VERBANDSLIGA, LANDESLIGA, BEZIRKSLIGA, KREISLIGA, KREISKLASSE, FREUNDSCHAFTSSPIEL);
    }

    public static List<String> getLeagueNames() {
        return getAllLeagues().stream().map(League::getName).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }
}
