package org.pdsr;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ExecuteScriptConfig {
	// private final String SQL_SCRIPT = "classpath:my_data_h2.sql";
	
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate datasource;
	
	// @Autowired
	// private ResourceLoader resourceLoader;

	@PostConstruct
	public void executeSQLScript() throws Exception {
//		Resource resource = resourceLoader.getResource(SQL_SCRIPT);
//		ScriptUtils.executeSqlScript(datasource.getDataSource().getConnection(), resource);
	}
}