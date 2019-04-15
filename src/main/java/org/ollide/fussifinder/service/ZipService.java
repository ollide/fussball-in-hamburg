package org.ollide.fussifinder.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.ollide.fussifinder.model.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class ZipService {

    private static final Logger LOG = LoggerFactory.getLogger(ZipService.class);

    private static final String RESOURCES_CITIES = "/regions/cities/";
    private static final String RESOURCES_DISTRICTS = "/regions/districts/";
    private static final String RESOURCES_SPECIALS = "/regions/specials/";

    private final ObjectReader stringReader;
    private final ObjectReader regionReader;

    public ZipService() {
        CsvMapper csvMapper = new CsvMapper();
        stringReader = csvMapper.readerFor(String.class);
        regionReader = csvMapper.readerFor(Region.class).with(CsvSchema.emptySchema().withHeader());
    }

    @Cacheable(value = "cityZips", key = "#city.toLowerCase()")
    public List<String> getZipsForCity(String city) {
        return readZipsFromResources(RESOURCES_CITIES + city.toLowerCase() + ".txt");
    }

    @Cacheable(value = "districtZips", key = "#district.toLowerCase()")
    public List<String> getZipsForDistrict(String district) {
        return readZipsFromResources(RESOURCES_DISTRICTS + district.toLowerCase() + ".txt");
    }

    @Cacheable(value = "specialZips", key = "#special.toLowerCase()")
    public List<String> getZipsForSpecial(String special) {
        return readZipsFromResources(RESOURCES_SPECIALS + special.toLowerCase() + ".txt");
    }

    protected List<String> readZipsFromResources(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            MappingIterator<String> readValues = stringReader.readValues(resource.getInputStream());
            return readValues.readAll();
        } catch (IOException e) {
            LOG.error("Error occurred while loading ZIPs from " + fileName, e);
            return Collections.emptyList();
        }
    }

    @Cacheable(value = "regionCities")
    public List<Region> getCityOverview() {
        return getOverview(RESOURCES_CITIES);
    }

    @Cacheable(value = "regionDistricts")
    public List<Region> getDistrictOverview() {
        return getOverview(RESOURCES_DISTRICTS);
    }

    @Cacheable(value = "regionSpecials")
    public List<Region> getSpecialOverview() {
        return getOverview(RESOURCES_SPECIALS);
    }

    protected List<Region> getOverview(String type) {
        try {
            ClassPathResource resource = new ClassPathResource(type + "_.txt");
            MappingIterator<Region> readValues = regionReader.readValues(resource.getInputStream());
            return readValues.readAll();
        } catch (IOException e) {
            LOG.error("Error occurred while loading overview from " + type + "_.txt", e);
            return Collections.emptyList();
        }
    }

}
