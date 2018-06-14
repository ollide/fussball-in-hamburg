package org.ollide.fussifinder.model;

import java.util.HashMap;
import java.util.Map;

public class MatchStats {

    private int numberOfMatches = 0;
    private Map<String, Integer> numberOfMatchesPerType = new HashMap<>();

    public int getNumberOfMatchType(String type) {
        return numberOfMatchesPerType.getOrDefault(type, 0);
    }

    public void setType(String type, int number) {
        numberOfMatchesPerType.put(type, number);
    }

    public void setNumberOfMatches(int numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public int getNumberOfMatches() {
        return numberOfMatches;
    }
}
