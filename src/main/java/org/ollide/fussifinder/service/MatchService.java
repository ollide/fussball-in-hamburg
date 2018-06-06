package org.ollide.fussifinder.service;

import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.zip.Hamburg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final MatchCrawlService matchCrawlService;
    private final ParseService parseService;

    @Autowired
    public MatchService(MatchCrawlService matchCrawlService, ParseService parseService) {
        this.matchCrawlService = matchCrawlService;
        this.parseService = parseService;
    }

    public List<Match> getMatches() {
        LocalDate now = LocalDate.now();
        String dateFrom = DATE_TIME_FORMATTER.format(now);
        String dateTo = DATE_TIME_FORMATTER.format(now.plusDays(6));

        return Hamburg.getAllZIP3().stream()
                .map((zip3) -> matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip3))
                .map(parseService::parseZipsWithMatches)
                .flatMap(Collection::stream)
                .map((zip5) -> matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip5))
                .map(parseService::parseMatchesForZip)
                .flatMap(Collection::stream)
                .sorted()
                .collect(Collectors.toList());
    }

}
