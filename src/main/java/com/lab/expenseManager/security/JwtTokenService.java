package com.lab.expenseManager.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lab.expenseManager.user.dataAcess.UserDetailsImpl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

	private static final String SECRET_KEY = "4Z^XrroxR@dWxqf$mTTKwW$!@#qGr4P";

	private static final String ISSUER = "pizzurg-api";

	public String generateToken(UserDetailsImpl user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			return JWT.create().withIssuer(ISSUER).withIssuedAt(creationDate()).withExpiresAt(expirationDate(1))
					.withSubject(user.getEmail()).withClaim("roles", user.getAuthorities().stream()
							.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
					.sign(algorithm);
		} catch (JWTCreationException exception) {
			throw new JWTCreationException("Erro durante a geração do token.", exception);
		}
	}

	public boolean isTokenNearExpiration(String token) {
		DecodedJWT decodedJWT = JWT.decode(token);
		Date expirationDate = decodedJWT.getExpiresAt();
		Date now = new Date();

		long timeUntilExpiration = expirationDate.getTime() - now.getTime();
		long fifteenMinutesInMillis = 10 * 60 * 1000;

		return timeUntilExpiration < fifteenMinutesInMillis;
	}

	public String getSubjectFromToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			return JWT.require(algorithm).withIssuer(ISSUER).build().verify(token).getSubject();
		} catch (JWTVerificationException exception) {
			throw new JWTVerificationException("Token inválido ou expirado.");
		}
	}

	private Instant creationDate() {
		return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant();
	}

	private Instant expirationDate(int hours) {
		return ZonedDateTime.now(ZoneId.of("America/Recife")).plusHours(hours).toInstant();
	}

}
