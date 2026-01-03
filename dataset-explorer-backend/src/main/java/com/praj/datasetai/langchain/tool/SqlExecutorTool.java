package com.praj.datasetai.langchain.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SqlExecutorTool {

    private final JdbcTemplate jdbcTemplate;

    // Explicitly inject the DuckDB JdbcTemplate
    public SqlExecutorTool(@Qualifier("duckdbJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Tool("Get the list of tables and their columns to understand the database schema")
    public String getSchema() {
        return jdbcTemplate.queryForList("SELECT table_name, column_name, data_type FROM information_schema.columns").toString();
    }

    @Tool("Execute a SQL SELECT query on the analytical database")
    public String executeQuery(@P("The valid SQL query to run") String sql) {
        try {
            log.info("AI is executing SQL: {}", sql);
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            return results.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Tool("Get a preview of the first 5 rows of a table to see sample data")
    public String getPreview(@P("The table name to preview") String tableName) {
        String sql = String.format("SELECT * FROM \"%s\" LIMIT 5", tableName);
        log.info("AI requesting preview for table: {}", tableName);
        try {
            return jdbcTemplate.queryForList(sql).toString();
        } catch (Exception e) {
            return "Error fetching preview: " + e.getMessage();
        }
    }
}