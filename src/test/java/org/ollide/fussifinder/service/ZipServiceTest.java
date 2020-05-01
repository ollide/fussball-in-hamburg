package org.ollide.fussifinder.service;

import org.junit.Test;
import org.ollide.fussifinder.model.Region;
import org.ollide.fussifinder.model.RegionType;

import java.util.List;

import static org.junit.Assert.*;

public class ZipServiceTest {

    private final ZipService zipService = new ZipService();

    @Test
    public void getZipsForCity() {
        // testcity.txt
        Region region = new Region(RegionType.CITY, "Testcity1");
        List<String> testCities = zipService.getZipsForRegion(region);
        assertEquals("testcity.txt should contain 2 ZIP codes", 2, testCities.size());
        assertTrue("testcity.txt should contain '11111'", testCities.contains("11111"));
        assertTrue("testcity.txt should contain '22222'", testCities.contains("22222"));

        // unknown city
        List<String> unknown = zipService.getZipsForRegion(new Region(RegionType.CITY, "unknown-city"));
        assertTrue(unknown.isEmpty());
    }

    @Test
    public void getZipsForDistrict() {
        // testdistrict2.txt
        Region region = new Region(RegionType.DISTRICT, "testdistrict2");
        List<String> testDistricts = zipService.getZipsForRegion(region);
        assertEquals("testdistrict2.txt should contain 1 ZIP code", 1, testDistricts.size());
        assertTrue("testdistrict2.txt should contain '11111'", testDistricts.contains("11111"));

        // unknown district
        List<String> unknown = zipService.getZipsForRegion(new Region(RegionType.DISTRICT, "unknown-district"));
        assertTrue(unknown.isEmpty());
    }

    @Test
    public void getZipsForRegionZip() {
        Region region = new Region(RegionType.ZIP, "20359");
        List<String> zipsForRegion = zipService.getZipsForRegion(region);

        assertEquals(1, zipsForRegion.size());
        assertEquals("20359", zipsForRegion.get(0));
    }

}
