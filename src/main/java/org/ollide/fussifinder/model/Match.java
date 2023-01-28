package org.ollide.fussifinder.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.ollide.fussifinder.http.serializer.IsoLocalDateTimeSerializer;
import org.ollide.fussifinder.http.serializer.KickoffTimeSerializer;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class Match implements Comparable<Match>, Serializable {

    private static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("HH:mm", Locale.GERMANY);
    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("E, dd.MM.yy | HH:mm", Locale.GERMANY);

    private String id;
    @JsonSerialize(using = IsoLocalDateTimeSerializer.class)
    private LocalDateTime date;
    private String teamType;
    private String teamTypeKey;
    private String league;
    private String leagueKey;
    private String clubHome;
    private String clubAway;
    private String score;
    private String url;

    // Manually added
    private String zip;

    public String formattedTime() {
        return FORMATTER_TIME.format(date);
    }

    public String formattedDateTime() {
        return FORMATTER_DATE.format(date);
    }

    @JsonSerialize(using = KickoffTimeSerializer.class)
    public LocalTime getKickoff() {
        return date.toLocalTime();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTeamTypeKey() {
        return teamTypeKey;
    }

    public void setTeamTypeKey(String teamTypeKey) {
        this.teamTypeKey = teamTypeKey;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getLeagueKey() {
        return leagueKey;
    }

    public void setLeagueKey(String leagueKey) {
        this.leagueKey = leagueKey;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public int compareTo(@NonNull Match o) {
        return Comparator.comparing(Match::getDate).thenComparing(Match::getId).compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return id.equals(match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
