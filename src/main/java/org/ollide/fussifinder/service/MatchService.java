package org.ollide.fussifinder.service;

import org.ollide.fussifinder.model.*;
import org.ollide.fussifinder.util.DateUtil;
import org.ollide.fussifinder.util.MatchUtils;
import org.ollide.fussifinder.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
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

    public List<MatchDay> getNearbyMatches(String zip, int distance, Period period) {
        List<Match> matches = collect(listener -> streamNearbyMatches(zip, distance, period, listener));
        return MatchUtils.splitIntoMatchDays(matches);
    }

    public List<Match> getMatches(Region region, Period period) {
        return collect(listener -> streamMatches(region, period, listener));
    }

    public void streamMatches(Region region, Period period, MatchStreamListener listener) {
        List<String> zips = zipService.getZipsForRegion(region);
        streamMatchesInternal(zips, zips, period, listener);
    }

    public void streamNearbyMatches(String zip, int distance, Period period, MatchStreamListener listener) {
        List<String> zip5 = zipService.getNearbyZips(zip, distance);
        List<String> zip3 = zip5.stream().map(z -> z.substring(0, 3)).distinct().collect(Collectors.toList());
        streamMatchesInternal(zip3, zip5, period, listener);
    }

    /**
     * Runs a streaming crawl to completion and returns the sorted result. Any crawl error is propagated.
     */
    private List<Match> collect(Consumer<MatchStreamListener> crawl) {
        List<Match> matches = new ArrayList<>();
        crawl.accept(new MatchStreamListener() {
            @Override
            public void onTotal(int total) {
            }

            @Override
            public void onMatches(List<Match> batch) {
                matches.addAll(batch);
            }

            @Override
            public void onZip3Done() {
            }

            @Override
            public void onError(String zip, Exception e) {
                throw e instanceof RuntimeException re ? re : new IllegalStateException(e);
            }

            @Override
            public boolean isCancelled() {
                return false;
            }
        });
        matches.sort(Comparator.naturalOrder());
        return matches;
    }

    private void streamMatchesInternal(Collection<String> lookupZips, Collection<String> allowedZips,
                                       Period period, MatchStreamListener listener) {
        String dateFrom = DateUtil.formatLocalDateForAPI(period.getStart());
        String dateTo   = DateUtil.formatLocalDateForAPI(period.getEnd());

        List<String> zip3s = lookupZips.stream()
                .map(z -> z.substring(0, 3)).distinct()
                .toList();

        listener.onTotal(zip3s.size());

        for (String zip3 : zip3s) {
            if (listener.isCancelled()) {
                return;
            }

            List<String> zip5s;
            try {
                String calHtml = matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip3);
                zip5s = parseService.parseZipsWithMatches(calHtml).stream()
                        .filter(z -> allowedZips.contains(z) || allowedZips.stream().anyMatch(z::startsWith))
                        .distinct()
                        .toList();
            } catch (Exception e) {
                listener.onError(zip3, e);
                listener.onZip3Done();
                continue;
            }

            for (String zip5 : zip5s) {
                if (listener.isCancelled()) {
                    return;
                }
                try {
                    String html = matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip5);
                    List<Match> batch = parseService.parseMatchesForZip(html).stream()
                            .peek(m -> m.setZip(zip5))
                            .filter(MatchService::isNotSpecialClass)
                            .filter(MatchService::isNotFutsal)
                            .filter(MatchService::isNotEFoot)
                            .filter(MatchService::isNotCancelled)
                            .filter(MatchService::isNotIndoor)
                            .map(this::shortenLeague)
                            .map(this::shortenTeamType)
                            .map(this::shortenTeamNames)
                            .map(this::applyFilterKeys)
                            .collect(Collectors.toList());
                    if (!batch.isEmpty()) {
                        listener.onMatches(batch);
                    }
                } catch (Exception e) {
                    listener.onError(zip5, e);
                }
            }
            listener.onZip3Done();
        }
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

                if (match.getLeague().contains("(Freizeit)")) {
                    shortenedLeague = "Freizeitliga";
                } else if (match.getLeague().contains("Oberliga")) {
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

    Match shortenTeamType(Match match) {
        if (Team.HERREN_FREIZEIT.getName().equals(match.getTeamType())) {
            match.setTeamType(Team.HERREN.getName());
        }
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
