package org.ollide.fussifinder.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Caps the number of concurrently running SSE crawls. Every stream competes for the same throttled remote,
 * so admitting unlimited streams only makes all of them slower.
 */
@Component
public class StreamLimiter {

    static final String RETRY_AFTER_SECONDS = "5";

    private final Semaphore permits;

    public StreamLimiter(@Value("${fussifinder.sse.maxStreams:20}") int maxStreams) {
        this.permits = new Semaphore(maxStreams);
    }

    Optional<Runnable> tryAcquire() {
        if (!permits.tryAcquire()) {
            return Optional.empty();
        }
        AtomicBoolean released = new AtomicBoolean();
        return Optional.of(() -> {
            if (released.compareAndSet(false, true)) {
                permits.release();
            }
        });
    }

    int availablePermits() {
        return permits.availablePermits();
    }

    static ResponseEntity<SseEmitter> tooBusy() {
        return ResponseEntity.status(503).header("Retry-After", RETRY_AFTER_SECONDS).build();
    }
}
