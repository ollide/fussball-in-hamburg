package org.ollide.fussifinder.service;

import org.ollide.fussifinder.model.Match;

import java.util.List;

public interface MatchStreamListener {

    /** Number of 3-digit zip areas that will be crawled. */
    void onTotal(int total);

    /** A batch of (filtered and normalized) matches for a single zip. */
    void onMatches(List<Match> batch);

    /** A 3-digit zip area has been fully processed (successfully or not). */
    void onZip3Done();

    /**
     * Crawling a zip failed. The crawl continues with the next zip, unless this method throws.
     */
    void onError(String zip, Exception e);

    /** Whether the consumer is gone; the crawl stops as soon as possible. */
    boolean isCancelled();
}
