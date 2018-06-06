package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.MatchDay;
import org.ollide.fussifinder.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
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
        List<MatchDay> matchDayList = splitIntoMatchDays(matchService.getMatches());
        model.addAttribute("matchDays", matchDayList);

        return "home";
    }

    private List<MatchDay> splitIntoMatchDays(List<Match> matches) {
        if (matches.isEmpty()) {
            return Collections.emptyList();
        }

        List<MatchDay> matchDayList = new ArrayList<>();

        MatchDay matchDay = new MatchDay();
        matchDayList.add(matchDay);
        matchDay.setDay(matches.get(0).getDate().toLocalDate());

        for (Match match : matches) {
            if (match.getDate().toLocalDate().equals(matchDay.getDay())) {
                matchDay.getMatches().add(match);
            } else {
                matchDay = new MatchDay();
                matchDayList.add(matchDay);
                matchDay.setDay(match.getDate().toLocalDate());
                matchDay.getMatches().add(match);
            }
        }

        return matchDayList;
    }

}
