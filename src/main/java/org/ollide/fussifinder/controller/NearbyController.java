package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.MatchDay;
import org.ollide.fussifinder.model.Period;
import org.ollide.fussifinder.service.MatchService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.List;

@RestController
public class NearbyController {

    private final MatchService matchService;
    private final StreamLimiter streamLimiter;

    public NearbyController(MatchService matchService, StreamLimiter streamLimiter) {
        this.matchService = matchService;
        this.streamLimiter = streamLimiter;
    }

    @CrossOrigin
    @GetMapping(value = "/api/nearby", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MatchDay> getMatchDays(@RequestParam(value = "zip") String zip,
                                       @RequestParam(value = "distance", defaultValue = "10000") int distance,
                                       @RequestParam(value = "period", required = false) String period) {
        if (!isValidZip(zip)) {
            return Collections.emptyList();
        }

        Period p = Period.fromString(period);

        return matchService.getNearbyMatches(zip, clampDistance(distance), p);
    }

    @CrossOrigin
    @GetMapping(value = "/api/nearby/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> streamNearby(@RequestParam(value = "zip") String zip,
                                   @RequestParam(value = "distance", defaultValue = "10000") int distance,
                                   @RequestParam(value = "period", required = false) String period) {
        if (!isValidZip(zip)) {
            return SseStream.response(SseStream.empty());
        }

        int dist = clampDistance(distance);
        Period p = Period.fromString(period);
        return streamLimiter.tryAcquire()
                .map(release -> SseStream.response(new SseStream(release)
                        .start(sse -> matchService.streamNearbyMatches(zip, dist, p, sse))))
                .orElseGet(StreamLimiter::tooBusy);
    }

    private static boolean isValidZip(String zip) {
        if (zip.length() != 5) {
            return false;
        }
        try {
            Integer.parseInt(zip);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    // Allowed values [1000-100000] (1km - 100km)
    private static int clampDistance(int distance) {
        return Math.min(Math.max(distance, 1000), 100000);
    }
}
