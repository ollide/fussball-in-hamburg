package org.ollide.fussifinder.service;

import com.google.common.util.concurrent.RateLimiter;
import org.ollide.fussifinder.api.MatchClient;
import org.ollide.fussifinder.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MatchClientCrawlService implements MatchCrawlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchClientCrawlService.class);

    private static final String DEFAULT_TEAM_TYPES = Team.getDefaultTeamsQuery();

    private static final RateLimiter RATE_LIMITER = RateLimiter.create(3.0);

    private final MatchClient matchClient;

    @Autowired
    public MatchClientCrawlService(MatchClient matchClient) {
        this.matchClient = matchClient;
    }

    @Override
    @Cacheable(value = "matchCalendar", sync = true)
    public String getMatchCalendar(String dateFrom, String dateTo, String zip) {
        RATE_LIMITER.acquire();
        return getMatchCalendar(dateFrom, dateTo, zip, DEFAULT_TEAM_TYPES);
    }

    @Override
    public String getMatchCalendar(String dateFrom, String dateTo, String zip, String teamTypes) {
        try {
            LOGGER.debug("Querying match calendar from '{}' to '{}' in ZIP '{}'.", dateFrom, dateTo, zip);
            return matchClient.matchCalendar(dateFrom, dateTo, zip, teamTypes).execute().body();
        } catch (IOException e) {
            LOGGER.error("Failed to query match calendar.", e);
        }
        return "";
    }

}
