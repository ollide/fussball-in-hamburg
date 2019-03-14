package org.ollide.fussifinder.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class League {

    public static final League VERBANDSLIGA = new League("Verbandsliga", "VL", true);
    public static final League LANDESLIGA = new League("Landesliga", "LL", true);
    public static final League BEZIRKSLIGA = new League("Bezirksliga", "BL", true);
    public static final League KREISLIGA = new League("Kreisliga", "KL");
    public static final League KREISKLASSE = new League("Kreisklasse", "KK");
    public static final League FREUNDSCHAFTSSPIEL = new League("FS", "FS", true);

    private String name;
    private String abbreviation;
    private boolean active = false;

    public League(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public League(String name, String abbreviation, boolean active) {
        this.name = name;
        this.abbreviation = abbreviation;
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

    public String getAbbreviation() {
        return abbreviation;
    }
}
