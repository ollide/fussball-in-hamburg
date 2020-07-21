package org.ollide.fussifinder.model.overpass;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OSM3S {

    @JsonProperty("timestamp_osm_base")
    private String osmTimestamp;

    private String copyright;

    public String getOsmTimestamp() {
        return osmTimestamp;
    }

    public void setOsmTimestamp(String osmTimestamp) {
        this.osmTimestamp = osmTimestamp;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
