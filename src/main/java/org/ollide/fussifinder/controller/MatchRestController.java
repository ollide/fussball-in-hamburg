package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.*;
import org.ollide.fussifinder.service.MatchService;
import org.ollide.fussifinder.util.MatchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class MatchRestController {

    private static final Logger LOG = LoggerFactory.getLogger(MatchRestController.class);

    private static final String DEFAULT_CITY = "Hamburg";

    private final MatchService matchService;

    @Autowired
    public MatchRestController(MatchService matchService) {
        this.matchService = matchService;
    }

    @CrossOrigin
    @GetMapping(value = "/api/matches", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MatchDay> getMatchDays(@RequestParam(value = "zip", required = false) String zip,
                                       @RequestParam(value = "name", defaultValue = DEFAULT_CITY) String name,
                                       @RequestParam(value = "type", defaultValue = "CITY") RegionType type,
                                       @RequestParam(value = "period", required = false) String period) {
        List<Match> matches;

        Period p = Period.fromString(period);

        if (zip != null) {
            LOG.debug("Requesting matches for ZIP '{}'", zip);
            matches = matchService.getMatches(Collections.singleton(zip), p);
        } else {
            Region region = new Region(type, name);
            LOG.debug("Requesting matches for region '{}'", region);
            matches = matchService.getMatches(region, p);
        }

        return MatchUtils.splitIntoMatchDays(matches);
    }

}
