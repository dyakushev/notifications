package ru.bia.traffic.notifications.event.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean("quartzDatasourceProperties")
    @ConfigurationProperties(prefix = "app.datasource.quartz")
    public DataSourceProperties quartzDatasourceProperties() {

        return new DataSourceProperties();
    }

    @Primary
    @Bean("quartzDataSource")
    @ConfigurationProperties("app.datasource.quartz.properties")
    public DataSource quartzDataSource() {
        return quartzDatasourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
}
