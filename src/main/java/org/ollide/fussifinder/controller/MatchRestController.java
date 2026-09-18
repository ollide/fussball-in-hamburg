package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.*;
import org.ollide.fussifinder.service.MatchDayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class MatchRestController {

    private static final Logger LOG = LoggerFactory.getLogger(MatchRestController.class);

    private static final String DEFAULT_CITY = "Hamburg";

    private static final Pattern PATTERN_ALLOWED_NAMES = Pattern.compile("^[a-zA-Z0-9_\\-ÜÄÖüäö]+$");

    private final MatchDayService matchDayService;
    private final StreamLimiter streamLimiter;

    @Autowired
    public MatchRestController(MatchDayService matchDayService, StreamLimiter streamLimiter) {
        this.matchDayService = matchDayService;
        this.streamLimiter = streamLimiter;
    }

    @CrossOrigin
    @GetMapping(value = "/api/matches", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MatchDay> getMatchDays(@RequestParam(value = "name", defaultValue = DEFAULT_CITY) String name,
                                       @RequestParam(value = "type", defaultValue = "CITY") RegionType type,
                                       @RequestParam(value = "period", required = false) String period) {

        if (!PATTERN_ALLOWED_NAMES.matcher(name).matches()) {
            return Collections.emptyList();
        }

        Period p = Period.fromString(period);
        Region region = new Region(type, name);
        LOG.debug("Requesting matches for region '{}'", region);
        return matchDayService.getMatchDays(region, p);
    }

    @CrossOrigin
    @GetMapping(value = "/api/matches/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> streamMatchDays(@RequestParam(value = "name", defaultValue = DEFAULT_CITY) String name,
                                      @RequestParam(value = "type", defaultValue = "CITY") RegionType type,
                                      @RequestParam(value = "period", required = false) String period) {
        if (!PATTERN_ALLOWED_NAMES.matcher(name).matches()) {
            return SseStream.response(SseStream.empty());
        }

        Region region = new Region(type, name);
        Period p = Period.fromString(period);
        return streamLimiter.tryAcquire()
                .map(release -> SseStream.response(new SseStream(release)
                        .start(sse -> matchDayService.streamMatchDays(region, p, sse))))
                .orElseGet(StreamLimiter::tooBusy);
    }

}
