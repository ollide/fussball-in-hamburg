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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
        List<MatchDay> cached = findCachedMatchDays(region, period);
        if (cached != null) {
            return cached;
        }

        // Retrieve matches
        List<Match> matches = matchService.getMatches(region, period);
        return MatchUtils.splitIntoMatchDays(matches);
    }

    /**
     * Streams matches to the listener. If the result is already cached, it is replayed as a single batch.
     */
    public void streamMatchDays(Region region, Period period, MatchStreamListener listener) {
        List<MatchDay> cached = findCachedMatchDays(region, period);
        if (cached != null) {
            List<Match> all = cached.stream().flatMap(d -> d.getMatches().stream()).toList();
            listener.onTotal(1);
            if (!all.isEmpty()) {
                listener.onMatches(all);
            }
            listener.onZip3Done();
            return;
        }

        List<Match> collected = new ArrayList<>();
        AtomicBoolean failed = new AtomicBoolean();
        matchService.streamMatches(region, period, new MatchStreamListener() {
            @Override
            public void onTotal(int total) {
                listener.onTotal(total);
            }

            @Override
            public void onMatches(List<Match> batch) {
                collected.addAll(batch);
                listener.onMatches(batch);
            }

            @Override
            public void onZip3Done() {
                listener.onZip3Done();
            }

            @Override
            public void onError(String zip, Exception e) {
                failed.set(true);
                listener.onError(zip, e);
            }

            @Override
            public boolean isCancelled() {
                return listener.isCancelled();
            }
        });

        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null && !failed.get() && !listener.isCancelled()) {
            collected.sort(Comparator.naturalOrder());
            cache.put(matchDayKeyGenerator.generateKey(region, period), MatchUtils.splitIntoMatchDays(collected));
        }
    }

    /**
     * 'Smart' cache search: also considers cached results of widened periods.
     *
     * @return the cached match days (trimmed to the period) or <code>null</code> on a cache miss
     */
    @SuppressWarnings("unchecked")
    private List<MatchDay> findCachedMatchDays(Region region, Period period) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return null;
        }
        List<Period> candidates = new ArrayList<>();
        candidates.add(period);
        candidates.addAll(period.widenedPeriods());
        for (Period candidate : candidates) {
            String key = matchDayKeyGenerator.generateKey(region, candidate);
            List<MatchDay> cachedMatchDays = cache.get(key, List.class);
            if (cachedMatchDays != null) {
                List<MatchDay> resultList = new ArrayList<>();
                for (MatchDay cachedMatchDay : cachedMatchDays) {
                    if (cachedMatchDay.getDay().isAfter(period.getEnd())) {
                        break;
                    }
                    resultList.add(cachedMatchDay);
                }
                return resultList;
            }
        }
        return null;
    }

}
