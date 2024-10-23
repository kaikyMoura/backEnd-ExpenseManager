package com.lab.expenseManager.security;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.lab.expenseManager.user.filters.UserAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserAuthenticationFilter userAuthenticationFilter;

	public SecurityConfig(UserAuthenticationFilter userAuthenticationFilter) {
		this.userAuthenticationFilter = userAuthenticationFilter;
	}

	public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = { "/users/login", "/users", "/users/validate-token", "/users/verify-account", "/users/resend-email"};

	public static final String[] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = { "/users/auth/**", "/expense/**", "/expense/create","/expense/list",
			"/expense/list/categories", "/category/**" };

	public static final String[] ENDPOINTS_ADMIN = { "/users/administrator", "/users/administrator/list",
			"/users/administrator/delete/**" };

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.exceptionHandling(exh -> exh.authenticationEntryPoint((request, response, ex) -> {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
		})).cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration corsConfiguration = new CorsConfiguration();
				corsConfiguration.setAllowCredentials(true);
				corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://expense-manager-mocha.vercel.app"));
				corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
				corsConfiguration.setAllowedHeaders(List.of("*"));
				corsConfiguration.setMaxAge(Duration.ofMinutes(5L));
				return corsConfiguration;
			}
		})).csrf(csrf -> csrf.disable())
				.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED)
						.permitAll().requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
						.requestMatchers(ENDPOINTS_ADMIN).hasRole("ADMINISTRATOR").anyRequest().denyAll())
				.addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}