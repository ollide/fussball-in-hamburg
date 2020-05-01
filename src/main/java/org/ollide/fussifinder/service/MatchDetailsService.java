package org.ollide.fussifinder.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.ollide.fussifinder.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchDetailsService.class);

    private final MatchCrawlService matchCrawlService;

    @Autowired
    public MatchDetailsService(MatchCrawlService matchCrawlService) {
        this.matchCrawlService = matchCrawlService;
    }

    public MatchDetails getMatchDetails(String id) {
        String text = matchCrawlService.getMatchDetails(id);
        return parseMatchDetails(text);
    }

    protected MatchDetails parseMatchDetails(String text) {
        Document overview = Jsoup.parse(text);
        Elements stage = overview.select("section#stage");

        MatchDetails matchDetails = new MatchDetails();

        String competition = stage.select("a.competition").text().trim();
        matchDetails.setCompetition(competition);

        Elements locationLink = stage.select("a.location");
        if (locationLink != null) {
            String location = locationLink.text();
            matchDetails.setAddress(parseAddress(location));
            matchDetails.setPitch(parsePitch(location));
            matchDetails.setGround(parseGround(location));
        }

        return matchDetails;
    }

    protected String parseAddress(String location) {
        String[] split = location.split(",");
        int length = split.length;
        if (length > 1) {
            String address = String.join(",", split[length - 2], split[length - 1]).trim();

            // str. -> straße
            address = address.replace("str.", "straße");

            return address;
        }
        LOGGER.info("Could not parse 'address' from location: {}", location);
        return null;
    }

    protected Pitch parsePitch(String location) {
        String pitchText = location.split(",")[0];
        pitchText = pitchText.toLowerCase();

        Pitch pitch = null;
        if (pitchText.contains("kunstrasen")) {
            pitch = Pitch.ARTIFICIAL;
        } else if (pitchText.contains("rasen")) {
            pitch = Pitch.GRASS;
        } else if (pitchText.contains("hartplatz")) {
            pitch = Pitch.CLAY;
        } else if (pitchText.contains("halle")) {
            pitch = Pitch.INDOOR;
        }

        if (pitch == null) {
            LOGGER.info("Could not parse 'pitch' from location: {}", location);
        }
        return pitch;
    }

    protected String parseGround(String location) {
        String[] split = location.split(",");
        int length = split.length;
        if (length > 2) {
            String groundText = split[1];

            groundText = groundText.replaceAll("\\(.*\\)", "");
            groundText = groundText.trim();

            // Stad. -> Stadion
            groundText = groundText.replace("Stad.", "Stadion");

            return groundText;
        }
        LOGGER.info("Could not parse 'ground' from location: {}", location);
        return null;
    }

}
