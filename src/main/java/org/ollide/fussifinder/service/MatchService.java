package org.ollide.fussifinder.service;

import org.ollide.fussifinder.model.*;
import org.ollide.fussifinder.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
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
    private final ZipService zipService;

    @Autowired
    public MatchService(MatchCrawlService matchCrawlService, ParseService parseService, ZipService zipService) {
        this.matchCrawlService = matchCrawlService;
        this.parseService = parseService;
        this.zipService = zipService;
    }

    @Async
    public Future<List<Match>> getMatches(String region, RegionType type, @Nullable LocalDate date) {
        String dateFrom;
        String dateTo;
        if (date != null) {
            dateFrom = DateUtil.formatLocalDateForAPI(date);
            dateTo = DateUtil.formatLocalDateForAPI(date.plusDays(1));
        } else {
            LocalDate now = LocalDate.now();
            dateFrom = DateUtil.formatLocalDateForAPI(now);
            dateTo = DateUtil.formatLocalDateForAPI(now.plusDays(6));
        }

        List<String> zips;
        if (type == RegionType.CITY) {
            zips = zipService.getZipsForCity(region);
        } else {
            zips = zipService.getZipsForDistrict(region);
        }

        return new AsyncResult<>(zips.stream()
                .map(zip5 -> zip5.substring(0, 3)).distinct()
                .map(zip3 -> matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip3))
                .map(parseService::parseZipsWithMatches)
                .flatMap(Collection::stream)
                .filter(zips::contains)
                .map(zip5 -> matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip5))
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
                .collect(Collectors.toList()));
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
            case "regionale Freundschaftsspiele":
                shortenedLeague = "r-FS";
                break;
            case "Freundschaftsspiele":
                shortenedLeague = "FS";
                break;
            default:
                shortenedLeague = match.getLeague();
        }
        match.setLeague(shortenedLeague);
        return match;
    }

    protected static boolean isNotSpecialClass7Players(Match match) {
        return !(match.getClubHome().endsWith(MATCH_7_PLAYERS) || match.getClubAway().endsWith(MATCH_7_PLAYERS));
    }

    protected static boolean isNotFutsal(Match match) {
        return !match.getLeague().contains(MATCH_FUTSAL);
    }

    protected static boolean isNotCancelled(Match match) {
        return !match.getScore().equals(MATCH_CANCELLED);
    }

}
