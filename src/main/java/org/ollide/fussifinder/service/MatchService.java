package org.ollide.fussifinder.service;

import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.util.DateUtil;
import org.ollide.fussifinder.zip.Hamburg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {

    /**
     * Teams who only play with 7 players are usually marked with ' 7er' at the end of the team name.
     */
    private static final String MATCH_7_PLAYERS = " 7er";

    private static final String MATCH_CANCELLED = "Absetzung";

    private final MatchCrawlService matchCrawlService;
    private final ParseService parseService;

    @Autowired
    public MatchService(MatchCrawlService matchCrawlService, ParseService parseService) {
        this.matchCrawlService = matchCrawlService;
        this.parseService = parseService;
    }

    public List<Match> getMatches() {
        LocalDate now = LocalDate.now();
        String dateFrom = DateUtil.formatLocalDateForAPI(now);
        String dateTo = DateUtil.formatLocalDateForAPI(now.plusDays(6));

        return Hamburg.getAllZIP3().stream()
                .map((zip3) -> matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip3))
                .map(parseService::parseZipsWithMatches)
                .flatMap(Collection::stream)
                .map((zip5) -> matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip5))
                .map(parseService::parseMatchesForZip)
                .flatMap(Collection::stream)
                .filter(MatchService::isNotSpecialClass7Players)
                .filter(MatchService::isNotCancelled)
                .sorted()
                .collect(Collectors.toList());
    }

    private static boolean isNotSpecialClass7Players(Match match) {
        return !(match.getClubHome().endsWith(MATCH_7_PLAYERS) || match.getClubAway().endsWith(MATCH_7_PLAYERS));
    }

    private static boolean isNotCancelled(Match match) {
        return !match.getScore().equals(MATCH_CANCELLED);
    }

}
