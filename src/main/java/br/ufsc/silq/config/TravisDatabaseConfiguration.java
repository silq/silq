package br.ufsc.silq.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@Profile(Constants.SPRING_PROFILE_TRAVIS)
public class TravisDatabaseConfiguration {

	private final Logger log = LoggerFactory.getLogger(TravisDatabaseConfiguration.class);

	@Bean
	public DataSource dataSource(DataSourceProperties dataSourceProperties, JHipsterProperties jHipsterProperties) {
		this.log.debug("Configuring Travis Datasource");

		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName(dataSourceProperties.getDriverClassName());
		config.addDataSourceProperty("url", "jdbc:postgresql://localhost:5432/silq2");
		config.addDataSourceProperty("user", "postgres");
		config.addDataSourceProperty("password", "");
		return new HikariDataSource(config);
	}
}
