package co.com.elkin.apps.taskmanagerapi.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class JwtTokenRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String username;
	@NotNull
	private String password;

	public JwtTokenRequestDTO() {
		super();
	}

	public JwtTokenRequestDTO(final String username, final String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

}