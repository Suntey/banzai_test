package com.banzai.test.repository;

import com.banzai.test.dto.Entry;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EntryRepositoryImpl implements EntryRepository {

    private static final String INSERT_SQL = "INSERT INTO banzai.entry(content, creation_date) VALUES (?, ?)";

    private static final String GET_ENTRY_BY_DATE = "SELECT * FROM banzai.entry WHERE creation_date = ?";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EntryRepositoryImpl(final HikariDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void batchSaveEntries(final Collection<Entry> entries) {
        final List<Object[]> params = getParamsForBatch(entries);
        jdbcTemplate.batchUpdate(INSERT_SQL, params);
    }

    private List<Object[]> getParamsForBatch(final Collection<Entry> entries) {
        if (entries==null || entries.isEmpty()){
            return Collections.emptyList();
        } else {
            return entries.stream()
                    .map(this::getParamsWithId)
                    .collect(Collectors.toList());
        }
    }

    private Object[] getParamsWithId(final Entry entry) {
        final Object[] params = new Object[2];
        params[0] = entry.getContent();
        params[1] = convertStringToDate(entry.getCreationDate());

        return params;
    }

    private LocalDateTime convertStringToDate(final String date) {
        return LocalDateTime.parse(date, formatter);
    }

    public Collection<Entry> getEntriesByCreationDate(final String date) {
        return jdbcTemplate.query(GET_ENTRY_BY_DATE, getEntryRowMapper(), convertStringToDate(date));
    }

    private RowMapper<Entry> getEntryRowMapper() {
        return (rs, rowNum) -> Entry.builder()
                .content(rs.getString("content"))
                .creationDate(rs.getString("creation_date"))
                .build();
    }
}
