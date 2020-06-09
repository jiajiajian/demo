package cn.com.tiza.config;

import com.ibeetl.starter.BeetlSqlMutipleDataSourceConfig;
import com.ibeetl.starter.BeetlSqlSingleConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author tiza
 */
@Slf4j
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore({BeetlSqlSingleConfig.class, BeetlSqlMutipleDataSourceConfig.class})
public class DatabaseConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public DatabaseParam databaseConfig(DataSourceProperties properties) {
        return new DatabaseParam(properties.getDriverClassName(), properties.getUrl(),
                properties.getUsername(), properties.getPassword());
    }

}
