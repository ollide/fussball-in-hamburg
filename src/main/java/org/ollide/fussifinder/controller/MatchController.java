package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.service.MockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MatchController {

    private final MockDataService mockDataService;

    @Autowired
    public MatchController(MockDataService mockDataService) {
        this.mockDataService = mockDataService;
    }

    @GetMapping("ajax.match.calendar/-/datum-bis/{dateTo}/datum-von/{dateFrom}/plz/{zip}/mannschaftsart/{teamType}")
    public String getMatches(@PathVariable("dateTo") String dateTo, @PathVariable("dateFrom") String dateFrom,
                             @PathVariable("zip") String zip, @PathVariable("teamType") String teamType,
                             Model model) {
        if (zip.length() == 3) {
            model.addAttribute("zips", mockDataService.getRandomZips(zip));
            return "api_overview";
        }

        model.addAttribute("matchDays", mockDataService.getRandomMatches(dateTo, dateFrom));
        return "api_matches";
    }

    @GetMapping("spiel/-/spiel/{id}")
    public String getMatchDetails(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("location", mockDataService.getRandomLocation());
        model.addAttribute("competition", mockDataService.getRandomCompetition());
        return "api_matchdetails";
    }

}
