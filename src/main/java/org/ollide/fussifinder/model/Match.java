package org.ollide.fussifinder.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Match implements Comparable<Match> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private LocalDateTime date;
    private String teamType;
    private String league;
    private String clubHome;
    private String clubAway;
    private String score;
    private String url;

    public String formattedTime() {
        return FORMATTER.format(date);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTeamType() {
        return teamType;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getClubHome() {
        return clubHome;
    }

    public void setClubHome(String clubHome) {
        this.clubHome = clubHome;
    }

    public String getClubAway() {
        return clubAway;
    }

    public void setClubAway(String clubAway) {
        this.clubAway = clubAway;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Match o) {
        return getDate().compareTo(o.getDate());
    }
}
