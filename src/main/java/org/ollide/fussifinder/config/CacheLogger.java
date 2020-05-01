package org.ollide.fussifinder.config;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheLogger implements CacheEventListener<Object, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(CacheLogger.class);

    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        LOG.debug("Cache update: {}, key: {}", cacheEvent.getType(), cacheEvent.getKey());
    }
}
