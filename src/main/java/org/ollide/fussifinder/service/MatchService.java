package org.ollide.fussifinder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.ZIPCode;
import org.ollide.fussifinder.util.DateUtil;
import org.ollide.fussifinder.zip.Hamburg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final MatchCrawlService matchCrawlService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MatchService(MatchCrawlService matchCrawlService, ObjectMapper objectMapper) {
        this.matchCrawlService = matchCrawlService;
        this.objectMapper = objectMapper;
    }

    public List<Match> getMatches() {
        LocalDate now = LocalDate.now();
        String dateFrom = DATE_TIME_FORMATTER.format(now);
        String dateTo = DATE_TIME_FORMATTER.format(now.plusDays(6));

        return Hamburg.getAllZIP3().stream()
                .map((zip3) -> matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip3))
                .map(this::parseZipsWithMatches)
                .flatMap(Collection::stream)
                .map((zip5) -> matchCrawlService.getMatchCalendar(dateFrom, dateTo, zip5))
                .map(this::parseMatchesForZip)
                .flatMap(Collection::stream)
                .sorted()
                .collect(Collectors.toList());
    }

    protected List<String> parseZipsWithMatches(String html) {
        Document overview = Jsoup.parse(html);
        Elements districtsWithMatches = overview.select(".match-calendar a");

        List<String> zipsWithMatches = new ArrayList<>();

        for (Element district : districtsWithMatches) {
            // {'plz':'20251'}
            String plzJson = district.attr("data-ajax-forced");
            ZIPCode zipCode;
            try {
                zipCode = objectMapper.readValue(plzJson, ZIPCode.class);
                zipsWithMatches.add(zipCode.getPlz());
            } catch (IOException e) {
                // ignore
            }
        }
        return zipsWithMatches;
    }

    protected List<Match> parseMatchesForZip(String html) {
        Document matches = Jsoup.parse(html);
        Elements matchTrs = matches.select("div.match-calendar tbody tr");

        AtomicReference<String> lastDate = new AtomicReference<>();

        return matchTrs.stream()
                .filter(it -> !it.hasClass("row-headline"))
                .map(matchRow -> {
                    Match m = new Match();

                    String date = matchRow.select("td.column-date").text();
                    date = DateUtil.normalizeDate(date, lastDate.get());
                    lastDate.set(date);
                    m.setDate(date);

                    m.setTeamType(matchRow.select("td.column-team").text());
                    m.setLeague(matchRow.select("td.column-league").text());
                    m.setClubHome(matchRow.select("td.column-club div.club-name").text());
                    m.setClubHome(matchRow.select("td.column-club").first().select("div.club-name").text());
                    m.setClubAway(matchRow.select("td.column-club").last().select("div.club-name").text());
                    m.setUrl(matchRow.select("td.column-detail a").first().attr("href"));
                    return m;
                })
                .collect(Collectors.toList());

    }

}
