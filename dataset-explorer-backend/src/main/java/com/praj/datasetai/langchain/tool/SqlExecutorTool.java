package com.praj.datasetai.langchain.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class SqlExecutorTool {

    private final JdbcTemplate jdbcTemplate;

    @Tool("Get the list of tables and their columns to understand the database schema")
    public String getSchema() {
        return jdbcTemplate.queryForList("SELECT table_name, column_name, data_type FROM information_schema.columns").toString();
    }

    @Tool("Execute a SQL SELECT query on the database")
    public String executeQuery(@P("The valid SQL query to run") String sql) {
        try {
            log.info("AI is executing SQL: {}", sql);
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            return results.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}