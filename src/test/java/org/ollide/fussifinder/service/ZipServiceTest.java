package org.ollide.fussifinder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ollide.fussifinder.ResourceHelper;
import org.ollide.fussifinder.api.OverpassClient;
import org.ollide.fussifinder.model.Region;
import org.ollide.fussifinder.model.RegionType;
import org.ollide.fussifinder.model.overpass.OverpassResponse;
import retrofit2.mock.Calls;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZipServiceTest {

    private OverpassClient overpassClient;
    private ZipService zipService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        overpassClient = mock(OverpassClient.class);
        zipService = new ZipService(overpassClient);
    }

    @Test
    public void getNearbyZips() throws Exception {
        final String zip = "20359";
        final int distance = 10000;

        String responseJson = ResourceHelper.readFile("/overpass/20359_success.json");
        when(overpassClient.query(zipService.buildNearbyZipcodesOverpassQuery(zip, distance)))
                .thenReturn(Calls.response(objectMapper.readValue(responseJson, OverpassResponse.class)));

        List<String> nearbyZips = zipService.getNearbyZips(zip, distance);
        assertEquals(87, nearbyZips.size());
    }

    @Test
    public void getNearbyZipsOverpassTimeout() throws Exception {
        final String zip = "20359";
        final int distance = 50000;

        String responseJson = ResourceHelper.readFile("/overpass/timeout.json");
        when(overpassClient.query(zipService.buildNearbyZipcodesOverpassQuery(zip, distance)))
                .thenReturn(Calls.response(objectMapper.readValue(responseJson, OverpassResponse.class)));

        List<String> nearbyZips = zipService.getNearbyZips(zip, distance);
        assertEquals(0, nearbyZips.size());
    }

    @Test
    public void getZipsForCity() {
        // testcity.txt
        Region region = new Region(RegionType.CITY, "Testcity1");
        List<String> testCities = zipService.getZipsForRegion(region);
        assertEquals(2, testCities.size(), "testcity.txt should contain 2 ZIP codes");
        assertTrue(testCities.contains("11111"), "testcity.txt should contain '11111'");
        assertTrue(testCities.contains("22222"), "testcity.txt should contain '22222'");

        // unknown city
        List<String> unknown = zipService.getZipsForRegion(new Region(RegionType.CITY, "unknown-city"));
        assertTrue(unknown.isEmpty());
    }

    @Test
    public void getZipsForDistrict() {
        // testdistrict2.txt
        Region region = new Region(RegionType.DISTRICT, "testdistrict2");
        List<String> testDistricts = zipService.getZipsForRegion(region);
        assertEquals(1, testDistricts.size(), "testdistrict2.txt should contain 1 ZIP code");
        assertTrue(testDistricts.contains("11111"), "testdistrict2.txt should contain '11111'");

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

    @Test
    public void getZipsWithDuplicates() {
        Region region = new Region(RegionType.CITY, "duplicates");
        List<String> zipsForRegion = zipService.getZipsForRegion(region);

        // We expect all ZIPs to be returned, check logs for information on duplicates
        assertEquals(5, zipsForRegion.size());
    }

}
