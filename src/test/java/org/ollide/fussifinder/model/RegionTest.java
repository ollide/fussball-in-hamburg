package org.ollide.fussifinder.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegionTest {

    @Test
    void equalsHashCode() {
        String name = "Hamburg";
        Region r1 = new Region(RegionType.CITY, name);

        assertThat(r1)
                .isEqualTo(r1)
                .hasSameHashCodeAs(r1)
                .isNotEqualTo(null);

        Region r1Clone = new Region(RegionType.CITY, name);
        assertThat(r1)
                .isEqualTo(r1Clone)
                .hasSameHashCodeAs(r1Clone);

        Region r2 = new Region(RegionType.CITY, "Flensburg");
        Region r3 = new Region(RegionType.DISTRICT, name);
        assertThat(r1)
                .isNotEqualTo(r2)
                .isNotEqualTo(r3)
                .doesNotHaveSameHashCodeAs(r2)
                .doesNotHaveSameHashCodeAs(r3);
    }
}
