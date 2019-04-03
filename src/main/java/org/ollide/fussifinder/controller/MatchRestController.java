package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.RegionType;
import org.ollide.fussifinder.service.MatchService;
import org.ollide.fussifinder.util.AsyncUtil;
import org.ollide.fussifinder.util.MatchUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class MatchRestController {

    private static final Logger LOG = LoggerFactory.getLogger(MatchRestController.class);

    private static final String DEFAULT_CITY = "Hamburg";

    private final MatchService matchService;

    @Autowired
    public MatchRestController(MatchService matchService) {
        this.matchService = matchService;
    }

    @RequestMapping("/api/matches")
    public ResponseEntity getMatchDays() {
        Future<List<Match>> asyncMatches = matchService.getMatches(DEFAULT_CITY, RegionType.CITY, null);

        AsyncUtil.waitMaxQuietly(asyncMatches, 1000);
        if (asyncMatches.isDone()) {
            try {
                List<Match> matches = asyncMatches.get();
                return ResponseEntity.ok(MatchUtils.splitIntoMatchDays(matches));
            } catch (InterruptedException | ExecutionException e) {
                LOG.error("Error retrieving matches", e);
            }
        }
        return ResponseEntity.noContent().build();
    }

}
