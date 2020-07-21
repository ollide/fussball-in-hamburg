package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.MatchDay;
import org.ollide.fussifinder.model.Period;
import org.ollide.fussifinder.service.MatchService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class NearbyController {

    private final MatchService matchService;

    public NearbyController(MatchService matchService) {
        this.matchService = matchService;
    }

    @CrossOrigin
    @GetMapping(value = "/api/nearby", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MatchDay> getMatchDays(@RequestParam(value = "zip") String zip,
                                       @RequestParam(value = "distance", defaultValue = "10000") int distance,
                                       @RequestParam(value = "period", required = false) String period) {
        try {
            Integer.parseInt(zip);
        } catch (NumberFormatException nfe) {
            return Collections.emptyList();
        }

        if (zip.length() != 5) {
            return Collections.emptyList();
        }

        // Allowed values [1000-100000] (1km - 100km)
        distance = Math.max(distance, 1000);
        distance = Math.min(distance, 100000);

        Period p = Period.fromString(period);

        return matchService.getNearbyMatches(zip, distance, p);
    }
}
