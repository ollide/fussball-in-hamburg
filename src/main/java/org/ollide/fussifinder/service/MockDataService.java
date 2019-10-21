package org.ollide.fussifinder.service;

import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.MatchDay;
import org.ollide.fussifinder.model.Team;
import org.ollide.fussifinder.model.ZIPCode;
import org.ollide.fussifinder.util.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class MockDataService {

    private static final List<String> LOCATIONS = Arrays.asList(
            "Kunstrasenplatz, Sachsenweg 2 (Kunstrasen), Sachsenweg 78, 22455 Hamburg",
            "Kunstrasenplatz, Langenfort 1, Langenfort 70, 22307 Hamburg",
            "Rasenplatz, Edmund-Plambeck-Stadion, Ochsenzoller Str. 58, 22848 Norderstedt",
            "Hartplatz, Grunewaldstr.61/I, Grunewaldstr. 61, 22149 Hamburg",
            "Kunstrasenplatz, Stadion Hoheluft, Lokstedter Steindamm 87, 22529 Hamburg");

    private static final List<String> LEAGUES = Arrays.asList("Verbandsliga", "Landesliga", "Bezirksliga", "Kreisliga",
            "Kreisklasse", "FS");

    private static final List<String> COMPETITIONS = Arrays.asList("Freundschaftsspiele Hamburg", "Oberliga",
            "Hammonia", "Bezirksliga Nord", "Bezirksliga Süd", "Kreisliga");

    private Random random = new Random();

    private final double probabilityMod;

    public MockDataService(@Value("${mock.probability:0.8}") double probabilityMod) {
        this.probabilityMod = probabilityMod;
    }

    public List<ZIPCode> getRandomZips(String zip3) {
        List<ZIPCode> zips = new ArrayList<>();
        if (includeResult()) {
            // 0-7 results
            int numberOfZipsInOverview = random.nextInt(8);
            for (int i = 0; i < numberOfZipsInOverview; i++) {
                // generate random 5 digit ZIP
                String zip = zip3 + random.nextInt(10) + random.nextInt(10);
                zips.add(new ZIPCode(zip));
            }
        }
        return zips;
    }

    public List<MatchDay> getRandomMatches(String dateTo, String dateFrom) {
        List<MatchDay> matchDays = new ArrayList<>();

        LocalDate localDateFrom = DateUtil.parseAPIDate(dateFrom);
        LocalDate localDateTo = DateUtil.parseAPIDate(dateTo);

        if (localDateTo.isBefore(localDateFrom)) {
            throw new IllegalArgumentException("Invalid dates requested.");
        }

        while (!localDateFrom.isEqual(localDateTo)) {

            MatchDay matchDay = new MatchDay();
            matchDay.setDay(localDateFrom);

            // 70%
            if (random.nextInt(10) < 7) {

                // 0-9 matches
                int numberOfMatches = random.nextInt(10);
                for (int i = 0; i < numberOfMatches; i++) {
                    matchDay.getMatches().add(getRandomMatch(localDateFrom));
                }
            }

            matchDays.add(matchDay);
            localDateFrom = localDateFrom.plusDays(1);
        }

        return matchDays;
    }

    public Match getRandomMatch(LocalDate date) {
        Match match = new Match();

        match.setId(UUID.randomUUID().toString().replace("-", ""));

        LocalTime time = LocalTime.of(random.nextInt(24), random.nextInt(60));
        LocalDateTime kickOff = LocalDateTime.of(date, time);
        match.setDate(kickOff);

        String league = LEAGUES.get(random.nextInt(LEAGUES.size()));
        match.setLeague(league);

        List<Team> teams = Team.getAllTeams();
        String team = teams.get(random.nextInt(teams.size())).getName();
        match.setTeamType(team);

        String homeTeam = "Heim-Team #" + random.nextInt(200);
        String awayTeam = "Auswärts-Team #" + random.nextInt(200);
        match.setClubHome(homeTeam);
        match.setClubAway(awayTeam);

        return match;
    }

    public String getRandomLocation() {
        return LOCATIONS.get(random.nextInt(LOCATIONS.size()));
    }

    public String getRandomCompetition() {
        return COMPETITIONS.get(random.nextInt(COMPETITIONS.size()));
    }

    private boolean includeResult() {
        return random.nextDouble() < probabilityMod;
    }

}
