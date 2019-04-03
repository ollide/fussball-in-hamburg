package org.ollide.fussifinder.model.filter;

import java.util.Arrays;
import java.util.List;

public class JsFilter {

    private static final JsFilter HERREN = new JsFilter("Herren", true);
    private static final JsFilter FRAUEN = new JsFilter("Frauen", true);
    private static final JsFilter A_JUN = new JsFilter("A-Jun.", "A-Junior", true);
    private static final JsFilter B_JUN = new JsFilter("B-Jun.", "B-Junior");

    private String name;
    private String filter;
    private boolean active = false;

    public JsFilter(String name, String filter, boolean active) {
        this.name = name;
        this.filter = filter;
        this.active = active;
    }

    public JsFilter(String name, String filter) {
        this.name = name;
        this.filter = filter;
    }

    public JsFilter(String name, boolean active) {
        this.name = name;
        this.filter = name;
        this.active = active;
    }

    public JsFilter(String name) {
        this.name = name;
        this.filter = name;
    }

    public static List<JsFilter> getTeamFilter() {
        return Arrays.asList(HERREN, FRAUEN, A_JUN, B_JUN);
    }

    public String getName() {
        return name;
    }

    public String getFilter() {
        return filter;
    }

    public boolean isActive() {
        return active;
    }
}
