package com.praj.datasetai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.nio.file.Path;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataIngestionService {

    private final JdbcTemplate jdbcTemplate;

    public void ingestCsv(String tableName, Path filePath) {
        // DuckDB special syntax to create a table from a CSV file directly
        String sql = String.format(
                "CREATE OR REPLACE TABLE %s AS SELECT * FROM read_csv_auto('%s')",
                tableName, filePath.toString().replace("\\", "/")
        );

        log.info("Ingesting CSV into table: {}", tableName);
        jdbcTemplate.execute(sql);
        log.info("Successfully ingested {}", tableName);
    }
}