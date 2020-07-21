package org.ollide.fussifinder.model.overpass;

import java.util.List;

public class OverpassResponse {

    private String version;
    private String generator;
    private OSM3S osm3s;
    private List<OverpassElement> elements;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public OSM3S getOsm3s() {
        return osm3s;
    }

    public void setOsm3s(OSM3S osm3s) {
        this.osm3s = osm3s;
    }

    public List<OverpassElement> getElements() {
        return elements;
    }

    public void setElements(List<OverpassElement> elements) {
        this.elements = elements;
    }
}
