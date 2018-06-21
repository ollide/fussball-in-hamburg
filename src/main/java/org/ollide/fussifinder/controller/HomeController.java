package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.*;
import org.ollide.fussifinder.service.MatchService;
import org.ollide.fussifinder.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private static final String DEFAULT_CITY = "Hamburg";

    private final MatchService matchService;

    @Autowired
    public HomeController(MatchService matchService) {
        this.matchService = matchService;
    }

    @RequestMapping("/")
    public String home(Model model) {
        populateModel(model, DEFAULT_CITY, RegionType.CITY);
        return "home";
    }

    @RequestMapping("/kreis/{district}")
    public String matchesForDistrict(Model model, @PathVariable(name = "district") String district) {
        populateModel(model, district, RegionType.DISTRICT);
        return "home";
    }

    @RequestMapping("/stadt/{city}")
    public String matchesForCity(Model model, @PathVariable(name = "city") String city) {
        populateModel(model, city, RegionType.CITY);
        return "home";
    }

    private void populateModel(Model model, String regionName, RegionType type) {
        model.addAttribute("city", StringUtil.capitalizeFirstLetter(regionName));
        model.addAttribute("teams", Team.getAllTeams());
        model.addAttribute("leagues", League.getAllLeagues());

        List<Match> matches = matchService.getMatches(regionName, type);
        model.addAttribute("stats", matchService.getMatchStats(matches));

        List<MatchDay> matchDayList = splitIntoMatchDays(matches);
        model.addAttribute("matchDays", matchDayList);
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
