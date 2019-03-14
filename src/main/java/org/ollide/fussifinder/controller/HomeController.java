package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.*;
import org.ollide.fussifinder.model.filter.JsFilter;
import org.ollide.fussifinder.service.MatchService;
import org.ollide.fussifinder.util.AsyncUtil;
import org.ollide.fussifinder.util.MatchUtils;
import org.ollide.fussifinder.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
public class HomeController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    private static final String DEFAULT_CITY = "Hamburg";

    private final MatchService matchService;

    @Autowired
    public HomeController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(value = "datum", required = false) LocalDate date) {
        return populateModel(model, DEFAULT_CITY, RegionType.CITY, date);
    }

    @GetMapping("/kreis/{district}")
    public String matchesForDistrict(Model model, @PathVariable(name = "district") String district,
                                     @RequestParam(value = "datum", required = false) LocalDate date) {
        return populateModel(model, district, RegionType.DISTRICT, date);
    }

    @GetMapping("/stadt/{city}")
    public String matchesForCity(Model model, @PathVariable(name = "city") String city,
                                 @RequestParam(value = "datum", required = false) LocalDate date) {
        return populateModel(model, city, RegionType.CITY, date);
    }

    @GetMapping("/spezial/{name}")
    public String matchesForSpecial(Model model, @PathVariable(name = "name") String name,
                                 @RequestParam(value = "datum", required = false) LocalDate date) {
        return populateModel(model, name, RegionType.SPECIAL, date);
    }

    private String populateModel(Model model, String regionName, RegionType type, @Nullable LocalDate date) {
        model.addAttribute("city", StringUtil.capitalizeFirstLetter(regionName));
        model.addAttribute("teamFilter", JsFilter.getTeamFilter());
        model.addAttribute("leagues", League.getAllLeagues());
        model.addAttribute("date", date);

        Future<List<Match>> asyncMatches = matchService.getMatches(regionName, type, date);

        AsyncUtil.waitMaxQuietly(asyncMatches, 1000);
        if (asyncMatches.isDone()) {
            try {
                List<Match> matches = asyncMatches.get();
                model.addAttribute("stats", matchService.getMatchStats(matches));
                List<MatchDay> matchDayList = MatchUtils.splitIntoMatchDays(matches);
                model.addAttribute("matchDays", matchDayList);
            } catch (InterruptedException | ExecutionException e) {
                LOG.error("Error retrieving matches", e);
            }
            return "home";
        }
        return "wait";
    }

}
