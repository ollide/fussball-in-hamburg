package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.League;
import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.MatchDay;
import org.ollide.fussifinder.model.Team;
import org.ollide.fussifinder.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final MatchService matchService;

    @Autowired
    public HomeController(MatchService matchService) {
        this.matchService = matchService;
    }

    @RequestMapping("/")
    public String home(Model model) {

        model.addAttribute("teams", Team.getAllTeams());
        model.addAttribute("leagues", League.getAllLeagues());

        List<Match> matches = matchService.getMatches();
        model.addAttribute("stats", matchService.getMatchStats(matches));

        List<MatchDay> matchDayList = splitIntoMatchDays(matches);
        model.addAttribute("matchDays", matchDayList);

        return "home";
    }

    private List<MatchDay> splitIntoMatchDays(List<Match> matches) {
        List<MatchDay> matchDayList = new ArrayList<>();
        MatchDay matchDay = null;

        for (Match match : matches) {
            LocalDate matchDate = match.getDate().toLocalDate();
            if (matchDay == null || !matchDate.equals(matchDay.getDay())) {
                matchDay = new MatchDay();
                matchDay.setDay(matchDate);
                matchDayList.add(matchDay);
            }
            matchDay.getMatches().add(match);
        }
        return matchDayList;
    }

}
