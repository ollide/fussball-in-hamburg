package org.ollide.fussifinder.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
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

    public ZipService() {
        CsvMapper csvMapper = new CsvMapper();
        stringReader = csvMapper.readerFor(String.class);
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

}
