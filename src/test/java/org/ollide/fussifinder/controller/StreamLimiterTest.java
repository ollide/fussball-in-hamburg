package org.ollide.fussifinder.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamLimiterTest {

    @Test
    void rejectsBeyondCapAndReleasesPermits() {
        StreamLimiter limiter = new StreamLimiter(2);

        Runnable first = limiter.tryAcquire().orElseThrow();
        limiter.tryAcquire().orElseThrow();
        assertTrue(limiter.tryAcquire().isEmpty());

        first.run();
        assertTrue(limiter.tryAcquire().isPresent());
    }

    @Test
    void releaseIsIdempotent() {
        StreamLimiter limiter = new StreamLimiter(1);
        Runnable release = limiter.tryAcquire().orElseThrow();

        release.run();
        release.run();

        assertEquals(1, limiter.availablePermits());
    }

    @Test
    void tooBusyIs503WithRetryAfter() {
        var response = StreamLimiter.tooBusy();
        assertEquals(503, response.getStatusCode().value());
        assertEquals("5", response.getHeaders().getFirst("Retry-After"));
    }
}
