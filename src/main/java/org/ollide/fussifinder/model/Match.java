package org.ollide.fussifinder.model;

public class Match implements Comparable<Match> {

    private String date;
    private String teamType;
    private String league;
    private String clubHome;
    private String clubAway;
    private String url;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
