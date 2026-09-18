package org.ollide.fussifinder.controller;

import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.service.MatchStreamListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Bridges a {@link MatchStreamListener} crawl to an {@link SseEmitter}.
 * <p>
 * Events: <code>init</code> {total}, <code>matches</code> [Match], <code>progress</code> {current,total},
 * <code>error</code> {zip,message} (non-fatal, crawl continues) and finally exactly one
 * <code>complete</code> {partial} or <code>error</code> {fatal:true}. The server closes the connection after
 * the final event; clients (e.g. <code>EventSource</code>) should close on it to avoid auto-reconnects.
 * <p>
 * Lines starting with <code>:</code> (an initial <code>connected</code> and periodic <code>ping</code> comments)
 * are not events; they flush the response headers early and keep the connection alive.
 */
class SseStream implements MatchStreamListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SseStream.class);

    /** Upper bound for a stream; protects against leaked connections when the remote hangs. */
    static final long TIMEOUT_MILLIS = 10 * 60 * 1000L;

    /**
     * Interval of keep-alive comments. Crawls can be silent for long while waiting on the rate limiter.
     */
    static final long HEARTBEAT_SECONDS = 15;

    private static final ScheduledExecutorService HEARTBEATS = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "sse-heartbeat");
        t.setDaemon(true);
        return t;
    });

    private static final String ERROR_MESSAGE = "Failed to load matches";

    final SseEmitter emitter = new SseEmitter(TIMEOUT_MILLIS);
    private final AtomicBoolean active = new AtomicBoolean(true);
    private final AtomicBoolean partial = new AtomicBoolean(false);
    private final AtomicInteger current = new AtomicInteger(0);
    private final AtomicInteger total = new AtomicInteger(0);

    private final Runnable onClose;
    private volatile ScheduledFuture<?> heartbeat;

    SseStream() {
        this(() -> {});
    }

    SseStream(Runnable onClose) {
        this.onClose = onClose;
        emitter.onCompletion(this::closed);
        emitter.onTimeout(this::closed);
        emitter.onError(e -> closed());
    }

    private void closed() {
        active.set(false);
        stopHeartbeat();
        onClose.run();
    }

    /**
     * Wraps the emitter in a response that reverse proxies must not buffer or cache:
     * <code>X-Accel-Buffering: no</code> disables nginx' proxy buffering for this response only.
     */
    static ResponseEntity<SseEmitter> response(SseEmitter emitter) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .header("X-Accel-Buffering", "no")
                .body(emitter);
    }

    /** A stream that immediately completes without results, e.g. for invalid input. */
    static SseEmitter empty() {
        SseStream sse = new SseStream();
        sse.complete();
        return sse.emitter;
    }

    /** Runs the crawl on a virtual thread and returns the emitter to hand back to Spring. */
    SseEmitter start(Consumer<SseStream> crawl) {
        heartbeat = HEARTBEATS.scheduleAtFixedRate(this::heartbeat, HEARTBEAT_SECONDS, HEARTBEAT_SECONDS,
                TimeUnit.SECONDS);
        Thread.ofVirtual().start(() -> {
            try {
                // commits the response right away, so clients see the connection open
                comment("connected");
                crawl.accept(this);
                complete();
            } catch (Exception e) {
                LOGGER.warn("SSE stream failed", e);
                fail();
            } finally {
                stopHeartbeat();
                onClose.run();
            }
        });
        return emitter;
    }

    @Override
    public void onTotal(int n) {
        total.set(n);
        send("init", Map.of("total", n));
    }

    @Override
    public void onMatches(List<Match> batch) {
        send("matches", batch);
    }

    @Override
    public void onZip3Done() {
        send("progress", Map.of("current", current.incrementAndGet(), "total", total.get()));
    }

    @Override
    public void onError(String zip, Exception e) {
        LOGGER.warn("Crawling zip '{}' failed", zip, e);
        partial.set(true);
        send("error", Map.of("zip", zip, "message", ERROR_MESSAGE));
    }

    @Override
    public boolean isCancelled() {
        return !active.get();
    }

    void complete() {
        send("complete", Map.of("partial", partial.get()));
        finish();
    }

    /** Terminates with an error event instead of {@code completeWithError}, which would corrupt the stream. */
    void fail() {
        send("error", Map.of("fatal", true, "message", ERROR_MESSAGE));
        finish();
    }

    /** Sends an SSE comment, which clients ignore. A failed write marks the stream as cancelled. */
    void heartbeat() {
        comment("ping");
    }

    void comment(String text) {
        if (!active.get()) return;
        try {
            emitter.send(SseEmitter.event().comment(text));
        } catch (IOException | IllegalStateException e) {
            active.set(false);
            stopHeartbeat();
        }
    }

    private void stopHeartbeat() {
        ScheduledFuture<?> h = heartbeat;
        if (h != null) {
            h.cancel(false);
        }
    }

    private void finish() {
        stopHeartbeat();
        if (active.getAndSet(false)) {
            emitter.complete();
        }
    }

    private void send(String eventName, Object data) {
        if (!active.get()) return;
        try {
            emitter.send(SseEmitter.event().name(eventName).data(data, MediaType.APPLICATION_JSON));
        } catch (IOException | IllegalStateException e) {
            active.set(false);
        }
    }
}
