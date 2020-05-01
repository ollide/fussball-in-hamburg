package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.*;
import org.ollide.fussifinder.service.MatchDayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MatchRestController {

    private static final Logger LOG = LoggerFactory.getLogger(MatchRestController.class);

    private static final String DEFAULT_CITY = "Hamburg";

    private final MatchDayService matchDayService;

    @Autowired
    public MatchRestController(MatchDayService matchDayService) {
        this.matchDayService = matchDayService;
    }

    @CrossOrigin
    @GetMapping(value = "/api/matches", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MatchDay> getMatchDays(@RequestParam(value = "name", defaultValue = DEFAULT_CITY) String name,
                                       @RequestParam(value = "type", defaultValue = "CITY") RegionType type,
                                       @RequestParam(value = "period", required = false) String period) {
        Period p = Period.fromString(period);
        Region region = new Region(type, name);
        LOG.debug("Requesting matches for region '{}'", region);
        return matchDayService.getMatchDays(region, p);
    }

}
