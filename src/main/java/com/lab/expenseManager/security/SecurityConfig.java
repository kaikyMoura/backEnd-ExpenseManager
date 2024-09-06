package com.lab.expenseManager.security;

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

import com.lab.expenseManager.user.filters.UserAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserAuthenticationFilter userAuthenticationFilter;

	public SecurityConfig(UserAuthenticationFilter userAuthenticationFilter) {
		this.userAuthenticationFilter = userAuthenticationFilter;
	}

	public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = { "/users/login", "/users" };

	public static final String[] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = { "/users/auth/user", "/expense/list",
			"/category/**" };

	/* public static final String [] ENDPOINTS_CUSTOMER = {"/users/customer"}; */

	public static final String[] ENDPOINTS_ADMIN = { "/users/administrator", "/users/administrator/list",
			"/users/administrator/delete/**" };

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.exceptionHandling(exh -> exh.authenticationEntryPoint(
				(request, response, ex) -> {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
				}
		  )).csrf(csrf -> csrf.disable())
			.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth.requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED)
					.permitAll().requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
					.requestMatchers(ENDPOINTS_ADMIN).hasRole("ADMINISTRATOR").anyRequest().denyAll())
			.addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
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

//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		return source;
//	}
}