package org.ollide.fussifinder.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CacheConfig.class);

    private static final Duration DEFAULT_TTL = Duration.ofHours(6);
    private static final long DEFAULT_MAX_ENTRIES = 5000;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // Only the caches registered below exist; an unknown cache name fails instead of creating an unbounded cache
        cacheManager.setCacheNames(List.of());

        register(cacheManager, "matchCalendar", Duration.ofHours(24), DEFAULT_MAX_ENTRIES);
        register(cacheManager, "matchDayResponse", Duration.ofHours(24), 500);
        register(cacheManager, "matchDetails", DEFAULT_TTL, DEFAULT_MAX_ENTRIES);
        register(cacheManager, "zips", Duration.ofHours(48), DEFAULT_MAX_ENTRIES);
        register(cacheManager, "nearbyZips", Duration.ofHours(72), DEFAULT_MAX_ENTRIES);
        return cacheManager;
    }

    private static void register(CaffeineCacheManager cacheManager, String name, Duration ttl, long maxEntries) {
        cacheManager.registerCustomCache(name, Caffeine.newBuilder()
                .expireAfterWrite(ttl)
                .maximumSize(maxEntries)
                .removalListener((key, value, cause) -> LOG.debug("Cache {} removal: {}, key: {}", name, cause, key))
                .build());
    }
}
