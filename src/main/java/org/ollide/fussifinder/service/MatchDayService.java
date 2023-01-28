package org.ollide.fussifinder.service;

import org.ollide.fussifinder.config.MatchDayKeyGenerator;
import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.MatchDay;
import org.ollide.fussifinder.model.Period;
import org.ollide.fussifinder.model.Region;
import org.ollide.fussifinder.util.MatchUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchDayService {

    private static final String CACHE_NAME = "matchDayResponse";

    private final MatchService matchService;
    private final CacheManager cacheManager;
    private final MatchDayKeyGenerator matchDayKeyGenerator;

    @Autowired
    public MatchDayService(MatchService matchService, CacheManager cacheManager, MatchDayKeyGenerator keyGenerator) {
        this.matchService = matchService;
        this.cacheManager = cacheManager;
        this.matchDayKeyGenerator = keyGenerator;
    }

    @Cacheable(value = CACHE_NAME, keyGenerator = "matchDayKeyGenerator")
    public List<MatchDay> getMatchDays(Region region, Period period) {

        // 'Smart' cache search
        Cache cache = cacheManager.getCache(CACHE_NAME);
        for (Period widenedPeriod : period.widenedPeriods()) {
            String key = matchDayKeyGenerator.generateKey(region, widenedPeriod);
            if (cache != null && cache.get(key) != null) {
                List<MatchDay> resultList = new ArrayList<>();
                List<MatchDay> cachedMatchDays = cache.get(key, List.class);
                for (MatchDay cachedMatchDay : cachedMatchDays) {
                    if (cachedMatchDay.getDay().isAfter(period.getEnd())) {
                        break;
                    }
                    resultList.add(cachedMatchDay);
                }
                return resultList;
            }
        }

        // Retrieve matches
        List<Match> matches = matchService.getMatches(region, period);
        return MatchUtils.splitIntoMatchDays(matches);
    }

}
