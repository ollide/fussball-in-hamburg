package org.ollide.fussifinder.service;

import com.google.common.util.concurrent.RateLimiter;
import org.ollide.fussifinder.api.MatchClient;
import org.ollide.fussifinder.model.AjaxModel;
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
        return getMatchCalendar(dateFrom, dateTo, zip, DEFAULT_TEAM_TYPES);
    }

    @Override
    public String getMatchCalendar(String dateFrom, String dateTo, String zip, String teamTypes) {
        try {
            RATE_LIMITER.acquire();
            LOGGER.debug("Querying match calendar from '{}' to '{}' in ZIP '{}'.", dateFrom, dateTo, zip);
            String body = matchClient.matchCalendar(dateFrom, dateTo, zip, teamTypes).execute().body();

            if (body != null && body.contains(MatchClient.LOAD_MORE)) {
                StringBuilder html = new StringBuilder(body);

                int insertIndex = html.indexOf("</tbody>");
                int offset = 10;
                AjaxModel lastResponse;
                do {
                    RATE_LIMITER.acquire();
                    LOGGER.debug("Querying match calendar from '{}' to '{}' in ZIP '{}' with offset '{}'.",
                            dateFrom, dateTo, zip, offset);
                    lastResponse = matchClient.loadMoreMatches(dateFrom, dateTo, zip, teamTypes, offset).execute().body();

                    if (lastResponse != null && lastResponse.isSuccess()) {
                        String moreResultsHtml = lastResponse.getHtml();
                        html.insert(insertIndex, moreResultsHtml);

                        insertIndex += moreResultsHtml.length();
                        offset += 10;
                    }
                }
                while (lastResponse != null && lastResponse.isSuccess() && !lastResponse.isFinalResponse());
                body = html.toString();
            }
            return body;

        } catch (IOException e) {
            LOGGER.error("Failed to query match calendar.", e);
        }
        return "";
    }

    @Override
    @Cacheable(value = "matchDetails", sync = true)
    public String getMatchDetails(String id) {
        try {
            RATE_LIMITER.acquire();
            return matchClient.matchDetails(id).execute().body();
        } catch (IOException e) {
            LOGGER.error("Failed to query match details.", e);
        }
        return "";
    }
}
