package org.ollide.fussifinder.service;

import org.ollide.fussifinder.model.*;
import org.ollide.fussifinder.util.DateUtil;
import org.ollide.fussifinder.util.MatchUtils;
import org.ollide.fussifinder.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MatchService {

    /**
     * Teams who only play with 7 or 9 players are usually marked with '7er' or '(9er) at the end of the team name.
     */
    private static final Pattern PATTERN_7_OR_9_PLAYERS = Pattern.compile(".*\\(?[79]'?er\\)?.*");

    private static final Pattern PATTERN_KREISKLASSE = Pattern.compile("([1-9]\\. ?)?((Kreis|Bezirks)(klasse|liga))( \\(?[A-Z]\\)?)?.*");

    private static final String MATCH_SPECIAL_CLASS = "Sonderklasse";

    private static final String MATCH_FUTSAL = "Futsal";

    private static final String MATCH_EFOOT = "eFoot";

    private static final Collection<String> MATCH_CANCELLED = Arrays.asList("Absetzung", "Nichtantritt", "Ausfall");

    private final MatchCrawlService matchCrawlService;
    private final ParseService parseService;
    private final ZipService zipService;

    @Autowired
    public MatchService(MatchCrawlService matchCrawlService, ParseService parseService, ZipService zipService) {
        this.matchCrawlService = matchCrawlService;
        this.parseService = parseService;
        this.zipService = zipService;
    }

    public List<Match> getMatches(Region region, @Nullable LocalDate date) {
        List<String> zips = zipService.getZipsForRegion(region);
        return getMatches(zips, date);
    }

    public List<Match> getMatches(Collection<String> zips, @Nullable LocalDate date) {
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

        return zips.stream()
                .map(zip5 -> zip5.substring(0, 3)).distinct()
                .map(zip3 -> matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip3))
                .map(parseService::parseZipsWithMatches)
                .flatMap(Collection::stream)
                .filter(z -> zips.contains(z) || zips.stream().anyMatch(z::startsWith))
                .map(zip5 -> matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip5))
                .map(parseService::parseMatchesForZip)
                .flatMap(Collection::stream)
                // Run required filters
                .filter(MatchService::isNotSpecialClass)
                .filter(MatchService::isNotFutsal)
                .filter(MatchService::isNotEFoot)
                .filter(MatchService::isNotCancelled)
                .filter(MatchService::isNotIndoor)
                // Beautify/shorten some things
                .map(this::shortenLeague)
                .map(this::shortenTeamNames)
                // Set team & league keys
                .map(this::applyFilterKeys)
                // sort and collect
                .sorted()
                .collect(Collectors.toList());
    }

    Match shortenLeague(Match match) {
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

                if (match.getLeague().contains("Oberliga")) {
                    shortenedLeague = "Oberliga";
                    break;
                }

                Matcher kreisKlKkMatcher = PATTERN_KREISKLASSE.matcher(shortenedLeague);
                if (kreisKlKkMatcher.matches()) {
                    shortenedLeague = kreisKlKkMatcher.group(2);
                }
        }
        match.setLeague(shortenedLeague);
        return match;
    }

    Match shortenTeamNames(Match match) {
        // (A1) (J2) etc.
        String youthYear = " ?\\([AJ][1-9]\\)";
        match.setClubHome(match.getClubHome().replaceAll(youthYear, ""));
        match.setClubAway(match.getClubAway().replaceAll(youthYear, ""));
        return match;
    }

    private Match applyFilterKeys(Match match) {
        match.setLeagueKey(MatchUtils.getLeagueKey(match.getLeague()));
        match.setTeamTypeKey(MatchUtils.getTeamKey(match.getTeamType()));
        return match;
    }

    static boolean isNotSpecialClass(Match match) {
        String home = match.getClubHome();
        String away = match.getClubAway();
        return !MATCH_SPECIAL_CLASS.equals(match.getLeague())
                && !(PATTERN_7_OR_9_PLAYERS.matcher(home).matches() || PATTERN_7_OR_9_PLAYERS.matcher(away).matches());
    }

    static boolean isNotFutsal(Match match) {
        return !match.getLeague().contains(MATCH_FUTSAL);
    }

    static boolean isNotEFoot(Match match) {
        return !(match.getClubHome().contains(MATCH_EFOOT) || match.getClubAway().contains(MATCH_EFOOT));
    }

    static boolean isNotCancelled(Match match) {
        String score = match.getScore();
        return MATCH_CANCELLED.stream().noneMatch(score::contains);
    }

    static boolean isNotIndoor(Match match) {
        boolean indoor = StringUtil.containsAllIgnoreCase(match.getClubAway(), "hallen", "turnier")
                || StringUtil.containsAllIgnoreCase(match.getLeague(), "hallen", "turnier");

        return !indoor;
    }

}
