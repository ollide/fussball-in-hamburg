package org.ollide.fussifinder.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class NearbyZipRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final ObjectReader zipReader;

    public NearbyZipRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.zipReader = objectMapper.readerForListOf(String.class);
    }

    public void saveZipEntries(String zip, int distance, List<String> zips) {
        String zipJson;
        try {
            zipJson = objectMapper.writeValueAsString(zips);
        } catch (JsonProcessingException e) {
            return;
        }
        final String finalZipJson = zipJson;

        jdbcTemplate.execute("INSERT INTO zips_nearby VALUES (?, ?)", (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, zip + distance);
            ps.setString(2, finalZipJson);
            return ps.execute();
        });
    }

    @NotNull
    public List<String> readZipEntries(String zip, int distance) {
        String json = jdbcTemplate.query("SELECT nearby FROM zips_nearby WHERE id = ?",
                ps -> ps.setString(1, zip + distance),
                rs -> {
                    if (rs.next()) {
                        return rs.getString(1);
                    }  else {
                        return null;
                    }
                });

        if (json == null) {
            return Collections.emptyList();
        }

        try {
            return zipReader.readValue(json);
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

}
