package org.ollide.fussifinder.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.ollide.fussifinder.model.Region;
import org.ollide.fussifinder.model.RegionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ZipService {

    private static final Logger LOG = LoggerFactory.getLogger(ZipService.class);

    private static final String RESOURCES_CITIES = "/regions/cities/";
    private static final String RESOURCES_DISTRICTS = "/regions/districts/";
    private static final String RESOURCES_SPECIALS = "/regions/specials/";

    private static final Map<RegionType, String> RESOURCE_MAP = new EnumMap<>(RegionType.class);

    private final ObjectReader stringReader;

    public ZipService() {
        CsvMapper csvMapper = new CsvMapper();
        stringReader = csvMapper.readerFor(String.class);

        RESOURCE_MAP.put(RegionType.CITY, RESOURCES_CITIES);
        RESOURCE_MAP.put(RegionType.DISTRICT, RESOURCES_DISTRICTS);
        RESOURCE_MAP.put(RegionType.SPECIAL, RESOURCES_SPECIALS);
    }

    @Cacheable(value = "zips", key = "#region.toString()", condition = "#region.type.name() != 'ZIP'")
    public List<String> getZipsForRegion(Region region) {
        if (RegionType.ZIP.equals(region.getType())) {
            return Collections.singletonList(region.getName());
        }

        String resourceDir = RESOURCE_MAP.get(region.getType());
        return readZipsFromResources(resourceDir + region.getName().toLowerCase() + ".txt");
    }

    private List<String> readZipsFromResources(String fileName) {
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
