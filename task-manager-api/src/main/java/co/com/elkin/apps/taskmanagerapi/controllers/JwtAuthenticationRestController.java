package co.com.elkin.apps.taskmanagerapi.controllers;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.com.elkin.apps.taskmanagerapi.dtos.JwtRequestDTO;
import co.com.elkin.apps.taskmanagerapi.dtos.JwtResponseDTO;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;
import co.com.elkin.apps.taskmanagerapi.services.auth.JwtAuthenticationService;

/**
 * This controller is in charge of generate/refresh JWT
 * 
 * @author egiraldo
 *
 */
@CrossOrigin(origins={ "${cors.client.origin.baseUrl}" }, allowedHeaders = "*")
@RestController
public class JwtAuthenticationRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationRestController.class);

	@Autowired
	private JwtAuthenticationService jwtAuthenticationService;

	/**
	 * Create a JWT for a specific user if the credentials are valid
	 * 
	 * @param authenticationRequest, DTO required for creating JWT
	 * @return {@link JwtResponseDTO}, DTO with the JWT generated
	 * @throws APIServiceException if any validation fails
	 */
	@PostMapping(value = "${jwt.get.token.uri}")
	public ResponseEntity<JwtResponseDTO> authenticate(
			@Valid @RequestBody final JwtRequestDTO authenticationRequest) throws APIServiceException {

		final String requestId = UUID.randomUUID().toString();
		LOGGER.info("[JwtAuthenticationRestController][authenticate][" + requestId + "] Started.");

		final String token = jwtAuthenticationService.createAuthenticationToken(authenticationRequest, requestId);

		LOGGER.info("[JwtAuthenticationRestController][authenticate][" + requestId + "] Finished and returned token ["
				+ token + "]");
		return ResponseEntity.ok(new JwtResponseDTO(token));
	}

	/**
	 * Create a JWT for a specific user
	 * 
	 * @param request, HTTP request from user
	 * @return a refreshed token
	 */
	@GetMapping(value = "${jwt.refresh.token.uri}")
	public ResponseEntity<?> refresh(final HttpServletRequest request) {

		final String requestId = UUID.randomUUID().toString();
		LOGGER.info("[JwtAuthenticationRestController][refresh][" + requestId + "] Started.");

		final String refreshedToken = jwtAuthenticationService.refreshAndGetAuthenticationToken(request, requestId);

		if (refreshedToken.isEmpty()) {
			LOGGER.info(
					"[JwtAuthenticationRestController][refresh][" + requestId + "] Finished without JWT refreshed.");
			return ResponseEntity.badRequest().body(null);
		}

		LOGGER.info("[JwtAuthenticationRestController][refresh][" + requestId + "] Finished with JWT [" + refreshedToken
				+ "]");
		return ResponseEntity.ok(new JwtResponseDTO(refreshedToken));
	}

}
