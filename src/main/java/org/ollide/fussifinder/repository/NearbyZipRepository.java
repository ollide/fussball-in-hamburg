package org.ollide.fussifinder.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectReader;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collections;
import java.util.List;

@Repository
public class NearbyZipRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JsonMapper jsonMapper;
    private final ObjectReader zipReader;

    public NearbyZipRepository(JdbcTemplate jdbcTemplate, JsonMapper jsonMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jsonMapper = jsonMapper;
        this.zipReader = jsonMapper.readerForListOf(String.class);
    }

    public void saveZipEntries(String zip, int distance, List<String> zips) {
        String zipJson;
        try {
            zipJson = jsonMapper.writeValueAsString(zips);
        } catch (JacksonException e) {
            return;
        }
        final String finalZipJson = zipJson;

        jdbcTemplate.execute("INSERT INTO zips_nearby VALUES (?, ?)", (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, zip + distance);
            ps.setString(2, finalZipJson);
            return ps.execute();
        });
    }

    @NonNull
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
        } catch (JacksonException e) {
            return Collections.emptyList();
        }
    }

}
