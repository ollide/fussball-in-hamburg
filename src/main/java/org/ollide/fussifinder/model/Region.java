package org.ollide.fussifinder.model;

import java.io.Serializable;

public class Region implements Serializable {

    private final RegionType type;
    private final String name;

    public Region(RegionType type, String name) {
        this.type = type;
        this.name = name;
    }

    public RegionType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Region{" +
                "type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
