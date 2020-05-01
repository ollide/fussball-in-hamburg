package org.ollide.fussifinder.service;

public interface MatchCrawlService {

    String getMatchCalendar(String dateFrom, String dateTo, String zip);

    String getMatchCalendar(String dateFrom, String dateTo, String zip, String teamTypes);

    String getMatchDetails(String id);
}
