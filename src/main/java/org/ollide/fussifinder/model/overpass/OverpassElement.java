package org.ollide.fussifinder.model.overpass;

import java.util.HashMap;
import java.util.Map;

public class OverpassElement {

    private String type;
    private long id;

    private Map<String, Object> tags = new HashMap<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public void setTags(Map<String, Object> tags) {
        this.tags = tags;
    }
}
