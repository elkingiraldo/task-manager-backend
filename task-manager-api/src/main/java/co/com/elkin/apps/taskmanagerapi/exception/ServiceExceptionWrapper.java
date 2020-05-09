package co.com.elkin.apps.taskmanagerapi.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Wrapper for error messages
 * 
 * @author egiraldo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceExceptionWrapper {

	private String errorCode;
	private String errorMessage;
	private String errorDetail;
	private Integer httpStatus;
	private String errorOrigin;

	public ServiceExceptionWrapper() {
	}

	public ServiceExceptionWrapper(final String errorCode, final String errorMessage, final String errorDetail,
			final Integer httpStatus) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorDetail = errorDetail;
		this.httpStatus = httpStatus;
	}

	public ServiceExceptionWrapper(final String errorCode, final String errorMessage, final String errorDetail) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorDetail = errorDetail;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(final String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(final String errorDetail) {
		this.errorDetail = errorDetail;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(final Integer httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getErrorOrigin() {
		return errorOrigin;
	}

	public void setErrorOrigin(final String errorOrigin) {
		this.errorOrigin = errorOrigin;
	}

	@Override
	public String toString() {
		return "ServiceExceptionWrapper [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", errorDetail="
				+ errorDetail + "]";
	}

}
