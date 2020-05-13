package co.com.elkin.apps.taskmanagerapi.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

/**
 * Utilities for JWT
 * 
 * @author egiraldo
 *
 */
@Component
public class JwtUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

	// If we want to add claims
//	private static final String CLAIM_KEY_USERNAME = "sub";
//	private static final String CLAIM_KEY_CREATED = "iat";

	private final Clock clock = DefaultClock.INSTANCE;

	@Value("${jwt.signing.key.secret}")
	private String secret;

	@Value("${jwt.token.expiration.in.seconds}")
	private Long expiration;

	public String getUsernameFromToken(final String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(final String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(final String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Auto-complete request for generating JWT
	 * 
	 * @param userDetails,
	 * @param requestId
	 * @return
	 */
	public String generateToken(final UserDetails userDetails, final String requestId) {

		LOGGER.info("[JwtTokenUtil][generateToken][" + requestId + "] Started.");

		final Map<String, Object> claims = new HashMap<>();
		final String generatedToken = doGenerateToken(claims, userDetails.getUsername(), requestId);

		LOGGER.info("[JwtTokenUtil][generateToken][" + requestId + "] Finished with token [" + generatedToken + "]");
		return generatedToken;
	}

	/**
	 * Method in charge of refreshing token
	 * 
	 * @param token,     JWT to be refreshed
	 * @param requestId, ID for tracking request
	 * @return refreshed JWT
	 */
	public String refreshToken(final String token, final String requestId) {

		LOGGER.info("[JwtTokenUtil][refreshToken][" + requestId + "] Started.");

		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate, requestId);

		final Claims claims = getAllClaimsFromToken(token);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);

		final String refreshedToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret)
				.compact();

		LOGGER.info(
				"[JwtTokenUtil][refreshToken][" + requestId + "] Finished with refreshed JWT [" + refreshedToken + "]");
		return refreshedToken;
	}

	/**
	 * This method validate if the token can be refreshed
	 * 
	 * @param token,     current token
	 * @param requestId, ID for tracking request
	 * @return a boolean indicating if the token can be refreshed
	 */
	public Boolean canTokenBeRefreshed(final String token, final String requestId) {

		LOGGER.info("[JwtTokenUtil][canTokenBeRefreshed][" + requestId + "] Started.");

		final boolean canBeRefreshed = (!isTokenExpired(token, requestId) || ignoreTokenExpiration(token, requestId));

		LOGGER.info("[JwtTokenUtil][canTokenBeRefreshed][" + requestId + "] Finished. Can the token be refreshed? ["
				+ canBeRefreshed + "]");
		return canBeRefreshed;
	}

	/**
	 * This method validate if token is valid
	 * 
	 * @param token,       sent by client
	 * @param userDetails, user details
	 * @param requestId,   ID for tracking request
	 * @return a boolean indicating if the token is valid
	 */
	public Boolean validateToken(final String token, final UserDetails userDetails, final String requestId) {

		LOGGER.info("[JwtTokenUtil][validateToken][" + requestId + "] Started.");

		final String username = getUsernameFromToken(token);
		final boolean validToken = (username.equals(userDetails.getUsername()) && !isTokenExpired(token, requestId));

		LOGGER.info(
				"[JwtTokenUtil][validateToken][" + requestId + "] Finished. Is a valid token? [" + validToken + "]");
		return validToken;
	}

	/**
	 * Method in charge of generating JWT
	 * 
	 * @param claims,    claims set for the user
	 * @param subject,   username for generating JWT
	 * @param requestId, ID for tracking request
	 * @return a generated JWT
	 */
	private String doGenerateToken(final Map<String, Object> claims, final String subject, final String requestId) {

		LOGGER.info("[JwtTokenUtil][doGenerateToken][" + requestId + "] Started.");

		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate, requestId);

		final String jwtGenetated = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();

		LOGGER.info(
				"[JwtTokenUtil][doGenerateToken][" + requestId + "] Finished. JWT generated [" + jwtGenetated + "]");
		return jwtGenetated;
	}

	/**
	 * This method generates a date
	 * 
	 * @param createdDate, date when JWT was created
	 * @param requestId,   ID for tracking request
	 * @return expiration JWT date
	 */
	private Date calculateExpirationDate(final Date createdDate, final String requestId) {

		LOGGER.info("[JwtTokenUtil][calculateExpirationDate][" + requestId + "] Started.");

		final Date date = new Date(createdDate.getTime() + expiration * 1000);

		LOGGER.info(
				"[JwtTokenUtil][calculateExpirationDate][" + requestId + "] Finished. Date generated [" + date + "]");
		return date;
	}

	/**
	 * This method obtain the claims from token
	 * 
	 * @param token,     sent by client
	 * @param requestId, ID for tracking the request
	 * @return claims from token
	 */
	private Claims getAllClaimsFromToken(final String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * This method verifies if the JWT is expired
	 * 
	 * @param token,     sent by client
	 * @param requestId, ID for tracking the request
	 * @return if the JWT is expired
	 */
	private Boolean isTokenExpired(final String token, final String requestId) {

		LOGGER.info("[JwtTokenUtil][isTokenExpired][" + requestId + "] Started.");

		final Date expiration = getExpirationDateFromToken(token);
		final boolean isExpired = expiration.before(clock.now());

		LOGGER.info(
				"[JwtTokenUtil][isTokenExpired][" + requestId + "] Finished. Is the JWT expired? [" + isExpired + "]");
		return isExpired;
	}

	/**
	 * This method ignores the JWT expiration
	 * 
	 * @param token,     sent by client
	 * @param requestId, ID for tracking the request
	 * @return a boolean indicating if need to ignore the refresh
	 */
	private Boolean ignoreTokenExpiration(final String token, final String requestId) {

		LOGGER.info("[JwtTokenUtil][ignoreTokenExpiration][" + requestId + "] Started.");

		final List<String> tokens = new ArrayList<>();
		final boolean containToken = tokens.contains(token);

		LOGGER.info("[JwtTokenUtil][ignoreTokenExpiration][" + requestId + "] Finished. Is the token ignored? ["
				+ containToken + "]");
		return containToken;
	}

}