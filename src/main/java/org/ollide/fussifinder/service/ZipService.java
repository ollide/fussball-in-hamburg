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
import java.util.stream.Collectors;

@Service
public class ZipService {

    private static final Logger LOG = LoggerFactory.getLogger(ZipService.class);

    private static final String RESOURCES_ASSOCIATIONS = "/regions/associations/";
    private static final String RESOURCES_CITIES = "/regions/cities/";
    private static final String RESOURCES_DISTRICTS = "/regions/districts/";
    private static final String RESOURCES_SPECIALS = "/regions/specials/";

    private static final Map<RegionType, String> RESOURCE_MAP = new EnumMap<>(RegionType.class);

    private final ObjectReader stringReader;

    public ZipService() {
        CsvMapper csvMapper = new CsvMapper();
        stringReader = csvMapper.readerFor(String.class);

        RESOURCE_MAP.put(RegionType.ASSOCIATION, RESOURCES_ASSOCIATIONS);
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
            List<String> zips = readValues.readAll();
            validateZips(fileName, zips);
            return zips;
        } catch (IOException e) {
            LOG.error("Error occurred while loading ZIPs from " + fileName, e);
            return Collections.emptyList();
        }
    }

    private void validateZips(String fileName, List<String> zips) {
        // 3-digit ZIP
        List<String> zip3 = zips.stream().filter(z -> z.length() == 3).collect(Collectors.toList());
        // Full ZIPs
        List<String> zip4or5 = zips.stream().filter(z -> z.length() > 3).collect(Collectors.toList());

        // Look for matching 3-digit/full ZIP (eg. 315 & 31511)
        zip4or5.stream().filter(z -> zip3.stream().anyMatch(z::startsWith))
                .forEach(z -> LOG.warn("'{}' contains the full ZIP '{}' and a matching 3-digit ZIP.", fileName, z));

        // Look for duplicates
        getDuplicatesFromList(zips).forEach(z -> LOG.warn("'{}' contains the duplicated ZIP '{}'.", fileName, z));
    }

    private Set<String> getDuplicatesFromList(List<String> zips) {
        final Set<String> setWithDuplicates = new HashSet<>();
        final Set<String> testSet = new HashSet<>();

        for (String zip : zips) {
            if (!testSet.add(zip)) {
                setWithDuplicates.add(zip);
            }
        }
        return setWithDuplicates;
    }

}
