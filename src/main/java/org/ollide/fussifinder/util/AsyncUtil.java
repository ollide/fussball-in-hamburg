package org.ollide.fussifinder.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public final class AsyncUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncUtil.class);

    private AsyncUtil() {
        // do not instantiate
    }

    public static void waitMaxQuietly(Future future, long millis) {
        long step = 100;
        long waited = 0;
        while (!future.isDone() && waited < millis) {
            try {
                waited += step;
                Thread.sleep(step);
            } catch (InterruptedException e) {
                LOG.error("Waiting for future threw an exception", e);
            }
        }
    }
}
