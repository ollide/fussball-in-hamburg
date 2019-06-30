package org.ollide.fussifinder.service;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ZipServiceTest {

    private final ZipService zipService = new ZipService();

    @Test
    public void getZipsForCity() {
        // testcity.txt
        List<String> testCities = zipService.getZipsForCity("Testcity1");
        assertEquals("testcity.txt should contain 2 ZIP codes", 2, testCities.size());
        assertTrue("testcity.txt should contain '11111'", testCities.contains("11111"));
        assertTrue("testcity.txt should contain '22222'", testCities.contains("22222"));

        // unknown city
        List<String> unknown = zipService.getZipsForCity("unknown-city");
        assertTrue(unknown.isEmpty());
    }

    @Test
    public void getZipsForDistrict() {
        // testdistrict2.txt
        List<String> testDistricts = zipService.getZipsForDistrict("testdistrict2");
        assertEquals("testdistrict2.txt should contain 1 ZIP code", 1, testDistricts.size());
        assertTrue("testdistrict2.txt should contain '11111'", testDistricts.contains("11111"));

        // unknown district
        List<String> unknown = zipService.getZipsForDistrict("unknown-district");
        assertTrue(unknown.isEmpty());
    }

}
