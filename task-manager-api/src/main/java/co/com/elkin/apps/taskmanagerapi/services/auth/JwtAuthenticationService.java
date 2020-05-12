package co.com.elkin.apps.taskmanagerapi.services.auth;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.taskmanagerapi.dtos.JwtTokenRequestDTO;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceErrorCodes;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;
import co.com.elkin.apps.taskmanagerapi.security.JwtTokenUtil;
import co.com.elkin.apps.taskmanagerapi.services.CustomUserDetailsService;

@Service
public class JwtAuthenticationService {

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	public String createAuthenticationToken(@Valid final JwtTokenRequestDTO authenticationRequest)
			throws APIServiceException {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		return jwtTokenUtil.generateToken(userDetails);
	}

	private void authenticate(final String username, final String password) throws APIServiceException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (final DisabledException e) {
			throw new APIServiceException(APIServiceErrorCodes.SECURITY_USER_DISABLED_EXCEPTION);
		} catch (final BadCredentialsException e) {
			throw new APIServiceException(APIServiceErrorCodes.SECURITY_INVALID_CREDENTIALS_EXCEPTION);
		} catch (final InternalAuthenticationServiceException e) {
			throw new APIServiceException(APIServiceErrorCodes.SECURITY_INVALID_CREDENTIALS_EXCEPTION);
		}
	}

	public String refreshAndGetAuthenticationToken(final HttpServletRequest request) {

		final String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		final String username = jwtTokenUtil.getUsernameFromToken(token);
		userDetailsService.loadUserByUsername(username);

		if (!jwtTokenUtil.canTokenBeRefreshed(token)) {
			return "";
		}

		return jwtTokenUtil.refreshToken(token);
	}

}
