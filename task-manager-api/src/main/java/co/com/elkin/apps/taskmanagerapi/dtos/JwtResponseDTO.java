package co.com.elkin.apps.taskmanagerapi.dtos;

import java.io.Serializable;

public class JwtResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String token;

	public JwtResponseDTO(final String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}

	@Override
	public String toString() {
		return "JwtTokenResponseDTO [token=" + token + "]";
	}

}