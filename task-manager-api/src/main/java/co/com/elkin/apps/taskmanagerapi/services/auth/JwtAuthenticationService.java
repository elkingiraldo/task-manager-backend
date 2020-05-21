package co.com.elkin.apps.taskmanagerapi.services.auth;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.taskmanagerapi.dtos.JwtRequestDTO;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceErrorCodes;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;
import co.com.elkin.apps.taskmanagerapi.security.JwtUtil;
import co.com.elkin.apps.taskmanagerapi.services.CustomUserDetailsService;

@Service
public class JwtAuthenticationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationService.class);

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtTokenUtil;

	/**
	 * This method authenticates and creating new JWT for a specific user
	 * 
	 * @param authenticationRequest, required for authentication
	 * @param requestId,             ID for tracking request
	 * @return the JWT generated
	 * @throws APIServiceException if any authentication validation fails
	 */
	public String createAuthenticationToken(@Valid final JwtRequestDTO authenticationRequest, final String requestId)
			throws APIServiceException {

		LOGGER.info("[JwtAuthenticationService][createAuthenticationToken][" + requestId + "] Started.");

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword(), requestId);

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String generatedToken = jwtTokenUtil.generateToken(userDetails, requestId);

		LOGGER.info("[JwtAuthenticationService][createAuthenticationToken][" + requestId + "] Finished. Generated JWT ["
				+ generatedToken + "]");
		return generatedToken;
	}

	/**
	 * This method reviews new JWT and refreshing it to a new one
	 * 
	 * @param request,   HTTP request from client
	 * @param requestId, ID for tracking request
	 * @return a refreshed JWT
	 */
	public String refreshAndGetAuthenticationToken(final HttpServletRequest request, final String requestId) {

		LOGGER.info("[JwtAuthenticationService][refreshAndGetAuthenticationToken][" + requestId + "] Started.");

		final String token = jwtTokenUtil.getTokenFromHeader(request, requestId);
		final String username = jwtTokenUtil.getUsernameFromToken(token);
		userDetailsService.loadUserByUsername(username);

		if (!jwtTokenUtil.canTokenBeRefreshed(token, requestId)) {
			LOGGER.info("[JwtAuthenticationService][refreshAndGetAuthenticationToken][" + requestId
					+ "] Finished without a JWT.");
			return "";
		}

		final String refreshedToken = jwtTokenUtil.refreshToken(token, requestId);

		LOGGER.info("[JwtAuthenticationService][refreshAndGetAuthenticationToken][" + requestId
				+ "] Finished with JWT [" + refreshedToken + "]");
		return refreshedToken;
	}

	/**
	 * This method verify if the user is into the DB and verifying his credentials
	 * 
	 * @param username,  user to validate
	 * @param password,  password of the user
	 * @param requestId, ID for tracking request
	 * @throws APIServiceException if any validation fails
	 */
	private void authenticate(final String username, final String password, final String requestId)
			throws APIServiceException {
		LOGGER.info("[JwtAuthenticationService][authenticate][" + requestId + "] Started.");
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			LOGGER.info("[JwtAuthenticationService][authenticate][" + requestId
					+ "] Finished. User authenticated correctly.");
		} catch (final DisabledException e) {
			LOGGER.error("[JwtAuthenticationService][authenticate][" + requestId + "] Finished with error: ["
					+ APIServiceErrorCodes.SECURITY_USER_DISABLED_EXCEPTION + "]");
			throw new APIServiceException(APIServiceErrorCodes.SECURITY_USER_DISABLED_EXCEPTION);
		} catch (final BadCredentialsException e) {
			LOGGER.error("[JwtAuthenticationService][authenticate][" + requestId
					+ "] Finished with error in Spring Security validations");
			throw new APIServiceException(APIServiceErrorCodes.SECURITY_INVALID_CREDENTIALS_EXCEPTION);
		} catch (final InternalAuthenticationServiceException e) {
			LOGGER.error("[JwtAuthenticationService][authenticate][" + requestId
					+ "] Finished. The user is not found in DB.");
			throw new APIServiceException(APIServiceErrorCodes.SECURITY_INVALID_CREDENTIALS_EXCEPTION);
		}
	}

}
