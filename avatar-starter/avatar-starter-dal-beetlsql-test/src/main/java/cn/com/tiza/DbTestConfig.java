package cn.com.tiza;

import com.ibeetl.starter.BeetlSqlMutipleDataSourceConfig;
import com.ibeetl.starter.BeetlSqlSingleConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore({BeetlSqlSingleConfig.class, BeetlSqlMutipleDataSourceConfig.class})
public class DbTestConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScripts("classpath:schema.sql")
                .build();
    }
}
