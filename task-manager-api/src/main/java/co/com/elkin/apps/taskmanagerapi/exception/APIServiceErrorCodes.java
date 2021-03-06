package co.com.elkin.apps.taskmanagerapi.exception;

import org.springframework.http.HttpStatus;

public enum APIServiceErrorCodes implements IAPIServiceErrorMsg {

	GENERAL_EXCEPTION("general.exception", HttpStatus.INTERNAL_SERVER_ERROR),
	GENERAL_DTO_CANT_BE_NULL_EXCEPTION("general.dto.cant.be.null.exception", HttpStatus.BAD_REQUEST),
	GENERAL_ATTRIBUTE_REQUIRED_EXCEPTION("general.attribute.required.exception", HttpStatus.BAD_REQUEST),
	
	SECURITY_USER_DISABLED_EXCEPTION("security.user.disabled.exception", HttpStatus.UNAUTHORIZED),
	SECURITY_INVALID_CREDENTIALS_EXCEPTION("security.invalid.credentials.exception", HttpStatus.UNAUTHORIZED),
	SECURITY_INVALID_TOKEN_MATCH_USERNAME_EXCEPTION("security.invalid.token.match.username.exception", HttpStatus.UNAUTHORIZED),
	
	TASK_DESCRIPTION_CANT_BE_EMPTY_EXCEPTION("task.description.cant.be.empty.exception", HttpStatus.BAD_REQUEST),
	TASK_EDC_BEFORE_EXCEPTION("task.edc.before.exception", HttpStatus.BAD_REQUEST);

	private String message;
	private HttpStatus httpStatus;
	private String errorDetail;

	private APIServiceErrorCodes(final String message, final HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	private APIServiceErrorCodes(final String message, final String errorDetail, final HttpStatus httpStatus) {
		this.message = message;
		this.errorDetail = errorDetail;
		this.httpStatus = httpStatus;
	}

	private APIServiceErrorCodes(final String message) {
		this.message = message;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
