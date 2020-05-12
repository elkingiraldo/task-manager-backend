package co.com.elkin.apps.taskmanagerapi.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.com.elkin.apps.taskmanagerapi.dtos.JwtTokenRequestDTO;
import co.com.elkin.apps.taskmanagerapi.dtos.JwtTokenResponseDTO;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;
import co.com.elkin.apps.taskmanagerapi.services.auth.JwtAuthenticationService;

@RestController
public class JwtAuthenticationRestController {

	@Autowired
	private JwtAuthenticationService jwtAuthenticationService;

	@PostMapping(value = "${jwt.get.token.uri}")
	public ResponseEntity<JwtTokenResponseDTO> authenticate(
			@Valid @RequestBody final JwtTokenRequestDTO authenticationRequest) throws APIServiceException {

		final String token = jwtAuthenticationService.createAuthenticationToken(authenticationRequest);

		return ResponseEntity.ok(new JwtTokenResponseDTO(token));
	}

	@GetMapping(value = "${jwt.refresh.token.uri}")
	public ResponseEntity<?> refresh(final HttpServletRequest request) {

		final String refreshedToken = jwtAuthenticationService.refreshAndGetAuthenticationToken(request);

		if (refreshedToken.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}

		return ResponseEntity.ok(new JwtTokenResponseDTO(refreshedToken));
	}

}
