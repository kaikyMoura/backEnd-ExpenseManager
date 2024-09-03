package com.lab.expenseManager.configuration;

import java.sql.SQLException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataInjection {

	@Bean
	CommandLineRunner initializarDataBase(JdbcTemplate jdbcTemplate) {
		return args -> {
			try {

				Integer countData = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tbroles", Integer.class);

				if (countData == null || countData == 0) {
					jdbcTemplate.update("INSERT INTO tbroles (id, name) VALUES (?, ?)", 1, "ROLE_CUSTOMER");
					jdbcTemplate.update("INSERT INTO tbroles (id, name) VALUES (?, ?)", 2, "ROLE_ADMINISTRATOR");
					jdbcTemplate.update("INSERT INTO tbroles (id, name) VALUES (?, ?)", 3, "ROLE_SELLER");
				}

			} catch (Exception e) {
				throw new SQLException("Esses registros já estão presentes na tabela");
			}
		};
	}
}
