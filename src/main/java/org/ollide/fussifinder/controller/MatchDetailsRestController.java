package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.MatchDetails;
import org.ollide.fussifinder.service.MatchDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MatchDetailsRestController {

    private final MatchDetailsService matchDetailsService;

    @Autowired
    public MatchDetailsRestController(MatchDetailsService matchDetailsService) {
        this.matchDetailsService = matchDetailsService;
    }

    @GetMapping(value = "/api/details", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MatchDetails getMatchDetails(@RequestParam String id) throws IOException {
        return matchDetailsService.getMatchDetails(id);
    }

}
