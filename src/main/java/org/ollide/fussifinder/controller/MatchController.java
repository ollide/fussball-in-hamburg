package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.*;
import org.ollide.fussifinder.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class MatchController {

    private Random random = new Random();

    @RequestMapping("ajax.match.calendar/-/datum-bis/{dateTo}/datum-von/{dateFrom}/plz/{zip}/mannschaftsart/{teamType}")
    public String getMatches(@PathVariable("dateTo") String dateTo, @PathVariable("dateFrom") String dateFrom,
                             @PathVariable("zip") String zip, @PathVariable("teamType") String teamType,
                             Model model) {
        if (zip.length() == 3) {
            model.addAttribute("zips", getRandomZips(zip));
            return "api_overview";
        }

        model.addAttribute("matchDays", getRandomMatches(dateTo, dateFrom));
        return "api_matches";
    }

    protected List<ZIPCode> getRandomZips(String zip3) {
        List<ZIPCode> zips = new ArrayList<>();
        // 80%
        if (random.nextInt(10) < 8) {
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

    protected List<MatchDay> getRandomMatches(String dateTo, String dateFrom) {
        List<MatchDay> matchDays = new ArrayList<>();

        LocalDate localDateFrom = DateUtil.parseAPIDate(dateFrom);
        LocalDate localDateTo = DateUtil.parseAPIDate(dateTo);

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

    protected Match getRandomMatch(LocalDate date) {
        Match match = new Match();

        LocalTime time = LocalTime.of(random.nextInt(24), random.nextInt(60));
        LocalDateTime kickOff = LocalDateTime.of(date, time);
        match.setDate(kickOff);

        List<League> leagues = League.getAllLeagues();
        String league = leagues.get(random.nextInt(leagues.size())).getName();
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

}
