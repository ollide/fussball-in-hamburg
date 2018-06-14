package org.ollide.fussifinder.service;

import org.ollide.fussifinder.model.League;
import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.MatchStats;
import org.ollide.fussifinder.model.Team;
import org.ollide.fussifinder.util.DateUtil;
import org.ollide.fussifinder.zip.Hamburg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchService {

    /**
     * Teams who only play with 7 players are usually marked with ' 7er' at the end of the team name.
     */
    private static final String MATCH_7_PLAYERS = " 7er";
    private static final String MATCH_FUTSAL = "Futsal";

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
                // Run required filters
                .filter(MatchService::isNotSpecialClass7Players)
                .filter(MatchService::isNotFutsal)
                .filter(MatchService::isNotCancelled)
                // Beautify/shorten some things
                .map(this::shortenLeague)
                // sort and collect
                .sorted()
                .collect(Collectors.toList());
    }

    public MatchStats getMatchStats(List<Match> matches) {
        Set<String> types = new HashSet<>();
        types.addAll(League.getLeagueNames());
        types.addAll(Team.getTeamTypeNames());

        MatchStats matchStats = new MatchStats();
        matchStats.setNumberOfMatches(matches.size());
        for (String type : types) {
            matchStats.setType(type, matchesForType(type, matches));
        }
        return matchStats;
    }

    protected int matchesForType(String type, List<Match> matches) {
        return (int) matches.stream()
                .filter((match -> (match.getLeague().endsWith(type)) || match.getTeamType().equals(type)))
                .count();
    }

    protected Match shortenLeague(Match match) {
        String shortenedLeague;
        switch (match.getLeague()) {
            case "Landesfreundschaftsspiele":
                shortenedLeague = "L-FS";
                break;
            case "Bezirksfreundschaftsspiele":
                shortenedLeague = "B-FS";
                break;
            case "Kreisfreundschaftsspiele":
                shortenedLeague = "K-FS";
                break;
            default:
                shortenedLeague = match.getLeague();
        }
        match.setLeague(shortenedLeague);
        return match;
    }

    private static boolean isNotSpecialClass7Players(Match match) {
        return !(match.getClubHome().endsWith(MATCH_7_PLAYERS) || match.getClubAway().endsWith(MATCH_7_PLAYERS));
    }

    private static boolean isNotFutsal(Match match) {
        return !match.getLeague().contains(MATCH_FUTSAL);
    }

    private static boolean isNotCancelled(Match match) {
        return !match.getScore().equals(MATCH_CANCELLED);
    }

}
