package com.praj.datasetai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${duckdb.path}")
    private String duckdbUrl;

    // 1. Define Primary DataSource for PostgreSQL (JPA uses this)
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return dataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    // 2. Define Secondary DataSource for DuckDB
    @Bean(name = "duckdbDataSource")
    public DataSource duckdbDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.duckdb.DuckDBDriver");
        dataSource.setUrl(duckdbUrl);
        return dataSource;
    }

    // 3. Define the specific JdbcTemplate for DuckDB
    @Bean(name = "duckdbJdbcTemplate")
    public JdbcTemplate duckdbJdbcTemplate() {
        return new JdbcTemplate(duckdbDataSource());
    }
}