package br.ufsc.silq.config.datasource;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import br.ufsc.silq.config.Profiles;
import br.ufsc.silq.config.JHipsterProperties;

@Configuration
@Profile(Profiles.PRODUCTION)
public class ProductionDatabaseConfig {

	private final Logger log = LoggerFactory.getLogger(ProductionDatabaseConfig.class);

	@Bean
	public DataSource dataSource(DataSourceProperties dataSourceProperties, JHipsterProperties jHipsterProperties) {
		this.log.debug("Configuring Production Datasource");
		
		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName(dataSourceProperties.getDriverClassName());
		config.addDataSourceProperty("url", this.getEnvOrFail("SILQ_DATABASE_URL"));
		config.addDataSourceProperty("user", this.getEnvOrFail("SILQ_DATABASE_USER"));
		config.addDataSourceProperty("password", this.getEnvOrFail("SILQ_DATABASE_PASSWORD"));
		return new HikariDataSource(config);
	}
	
	private String getEnvOrFail(String env) {
		String value = System.getenv(env);
		if (value == null) {
			throw new ApplicationContextException(String.format("System env %s must be defined", env));
		}
		return value;
	}
}
