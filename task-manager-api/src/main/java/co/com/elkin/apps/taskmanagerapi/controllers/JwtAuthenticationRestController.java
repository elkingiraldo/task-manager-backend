package co.com.elkin.apps.taskmanagerapi.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.com.elkin.apps.taskmanagerapi.dtos.JwtTokenRequestDTO;
import co.com.elkin.apps.taskmanagerapi.dtos.JwtTokenResponseDTO;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceErrorCodes;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;
import co.com.elkin.apps.taskmanagerapi.security.JwtTokenUtil;

@RestController
public class JwtAuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@PostMapping(value = "${jwt.get.token.uri}")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody final JwtTokenRequestDTO authenticationRequest)
			throws APIServiceException {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtTokenResponseDTO(token));
	}

	private void authenticate(final String username, final String password) throws APIServiceException {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (final DisabledException e) {
			throw new APIServiceException(APIServiceErrorCodes.SECURITY_USER_DISABLED_EXCEPTION);
		} catch (final BadCredentialsException e) {
			throw new APIServiceException(APIServiceErrorCodes.SECURITY_INVALID_CREDENTIALS_EXCEPTION);
		}
	}

}
