package co.com.elkin.apps.taskmanagerapi.security;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * Filter for JWT
 * 
 * @author egiraldo
 *
 */
@Component
public class JwtAuthorizationOncePerRequestFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationOncePerRequestFilter.class);

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain chain) throws ServletException, IOException {

		final String requestId = UUID.randomUUID().toString();
		LOGGER.debug(
				"[JwtTokenAuthorizationOncePerRequestFilter][doFilterInternal]['{}'] Started. Authentication Request For '{}'",
				requestId, request.getRequestURL());

		final String requestTokenHeader = request.getHeader(this.tokenHeader);

		String jwtToken = null;
		String username = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (final IllegalArgumentException e) {
				LOGGER.error("JWT_TOKEN_UNABLE_TO_GET_USERNAME", e);
			} catch (final ExpiredJwtException e) {
				LOGGER.warn("JWT_TOKEN_EXPIRED", e);
			}
		} else {
			LOGGER.warn("JWT_TOKEN_DOES_NOT_START_WITH_BEARER_STRING");
		}

		LOGGER.debug("JWT_TOKEN_USERNAME_VALUE '{}'", username);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			final UserDetails userDetails = this.jwtInMemoryUserDetailsService.loadUserByUsername(username);

			if (jwtTokenUtil.validateToken(jwtToken, userDetails, requestId)) {
				final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}

		chain.doFilter(request, response);
	}

}