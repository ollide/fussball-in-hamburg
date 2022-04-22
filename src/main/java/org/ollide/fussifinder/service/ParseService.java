package org.ollide.fussifinder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ollide.fussifinder.model.Match;
import org.ollide.fussifinder.model.ZIPCode;
import org.ollide.fussifinder.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ParseService {

    private final ObjectMapper objectMapper;

    @Autowired
    public ParseService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<String> parseZipsWithMatches(String html) {
        List<String> zipsWithMatches = new ArrayList<>();

        if (!StringUtils.hasText(html)) {
            return zipsWithMatches;
        }

        Document overview = Jsoup.parse(html);
        Elements districtsWithMatches = overview.select(".match-calendar a");

        for (Element district : districtsWithMatches) {
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

    public List<Match> parseMatchesForZip(String html) {
        if (!StringUtils.hasText(html)) {
            return Collections.emptyList();
        }

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
                    m.setDate(DateUtil.parseLocalDateTime(date));

                    m.setTeamType(matchRow.select("td.column-team").text());
                    m.setLeague(matchRow.select("td.column-league").text());

                    Elements clubElements = matchRow.select("td.column-club");
                    Element clubHomeEl = clubElements.first();
                    if (clubHomeEl != null) {
                        m.setClubHome(clubHomeEl.select("div.club-name").text());
                    }
                    Element clubAwayEl = clubElements.last();
                    if (clubAwayEl != null) {
                        m.setClubAway(clubAwayEl.select("div.club-name").text());
                    }

                    Elements matchScoreLinkTag = matchRow.select("td.column-score a");
                    m.setScore(matchScoreLinkTag.text());

                    String url = matchScoreLinkTag.attr("href");
                    m.setUrl(url);
                    String[] uriPaths = url.split("/");
                    String id = uriPaths[uriPaths.length - 1];
                    m.setId(id);

                    return m;
                })
                .collect(Collectors.toList());
    }
}
