package co.com.elkin.apps.taskmanagerapi.dtos;

import java.io.Serializable;

public class JwtTokenResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String token;

	public JwtTokenResponseDTO(final String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

}