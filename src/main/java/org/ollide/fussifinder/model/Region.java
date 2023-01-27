package org.ollide.fussifinder.model;

import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Region implements Serializable {

    private final RegionType type;
    private final String name;

    public Region(@NonNull RegionType type, @NonNull String name) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return type == region.type && name.equals(region.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }
}
