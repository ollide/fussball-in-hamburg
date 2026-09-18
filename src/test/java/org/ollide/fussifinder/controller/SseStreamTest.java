package org.ollide.fussifinder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.junit.jupiter.api.Assertions.*;

class SseStreamTest {

    @Test
    void cancelledOnceEmitterCompletes() {
        SseStream sse = new SseStream();
        assertFalse(sse.isCancelled());
        sse.complete();
        assertTrue(sse.isCancelled());
    }

    @Test
    void sendsAfterCompletionAreIgnored() {
        SseStream sse = new SseStream();
        sse.fail();
        assertDoesNotThrow(() -> {
            sse.onTotal(3);
            sse.onZip3Done();
            sse.complete();
        });
    }

    @Test
    void emptyStreamCompletes() {
        SseEmitter emitter = SseStream.empty();
        assertNotNull(emitter);
    }

    @Test
    void hasFiniteTimeout() {
        assertEquals(SseStream.TIMEOUT_MILLIS, new SseStream().emitter.getTimeout());
    }

    @Test
    void responseDisablesProxyBufferingAndCaching() {
        var response = SseStream.response(new SseStream().emitter);
        assertEquals("no", response.getHeaders().getFirst("X-Accel-Buffering"));
        assertEquals("no-cache", response.getHeaders().getCacheControl());
    }
}
