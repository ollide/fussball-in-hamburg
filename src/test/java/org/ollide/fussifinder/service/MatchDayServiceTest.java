package org.ollide.fussifinder.service;

import org.junit.jupiter.api.Test;
import org.ollide.fussifinder.config.MatchDayKeyGenerator;
import org.ollide.fussifinder.model.Period;
import org.ollide.fussifinder.model.Region;
import org.ollide.fussifinder.model.RegionType;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.NoOpCache;
import org.springframework.cache.support.SimpleCacheManager;

import java.util.List;

import static org.mockito.Mockito.*;

class MatchDayServiceTest {

    @Test
    void getMatchDays() {
        MatchService matchServiceMock = mock(MatchService.class);
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        MatchDayService service = new MatchDayService(matchServiceMock, cacheManager, new MatchDayKeyGenerator());

        Region region = new Region(RegionType.CITY, "Hamburg");
        Period period = Period.fromString("D3");

        // invocation without any caches
        service.getMatchDays(region, period);
        verify(matchServiceMock, times(1)).getMatches(region, period);
        reset(matchServiceMock);

        // invocation with empty cache
        cacheManager.setCaches(List.of(new NoOpCache("matchDayResponse")));
        service.getMatchDays(region, period);
        verify(matchServiceMock, times(1)).getMatches(region, period);
    }

    @Test
    void streamMatchDays_CachesCompleteResultAndReplaysIt() {
        MatchService matchServiceMock = mock(MatchService.class);
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(new ConcurrentMapCache("matchDayResponse")));
        cacheManager.initializeCaches();
        MatchDayService service = new MatchDayService(matchServiceMock, cacheManager, new MatchDayKeyGenerator());

        Region region = new Region(RegionType.CITY, "Hamburg");
        Period period = Period.fromString("T");
        MatchStreamListener listener = mock(MatchStreamListener.class);

        service.streamMatchDays(region, period, listener);
        verify(matchServiceMock, times(1)).streamMatches(eq(region), eq(period), any());

        // second call is served from the cache
        service.streamMatchDays(region, period, listener);
        verify(matchServiceMock, times(1)).streamMatches(eq(region), eq(period), any());
        verify(listener, atLeastOnce()).onTotal(1);
    }

    @Test
    void streamMatchDays_DoesNotCacheCancelledStream() {
        MatchService matchServiceMock = mock(MatchService.class);
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(new ConcurrentMapCache("matchDayResponse")));
        cacheManager.initializeCaches();
        MatchDayService service = new MatchDayService(matchServiceMock, cacheManager, new MatchDayKeyGenerator());

        Region region = new Region(RegionType.CITY, "Hamburg");
        Period period = Period.fromString("T");
        MatchStreamListener listener = mock(MatchStreamListener.class);
        when(listener.isCancelled()).thenReturn(true);

        service.streamMatchDays(region, period, listener);
        service.streamMatchDays(region, period, listener);
        verify(matchServiceMock, times(2)).streamMatches(eq(region), eq(period), any());
    }
}
