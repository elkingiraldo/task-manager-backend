package co.com.elkin.apps.taskmanagerapi.controllers;

import java.util.Locale;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import co.com.elkin.apps.taskmanagerapi.constants.Constant;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceErrorCodes;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;
import co.com.elkin.apps.taskmanagerapi.exception.ServiceExceptionWrapper;

/**
 * Class in charge of managing exceptions for communicating to the client
 * 
 * @author egiraldo
 *
 */
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	private static final Function<String, String> ERROR_MESSAGE = msg -> "Task Manager API service -> exception occurred"
			+ msg;

	@Value("${spring.application.name}")
	private static String applicationName;

	@Autowired
	private ReloadableResourceBundleMessageSource messageSource;

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<ServiceExceptionWrapper> handleUnknownException(final Exception ex,
			final WebRequest request) {
		LOGGER.error(ERROR_MESSAGE.apply(ex.getMessage()), ex);
		ex.printStackTrace();

		final String code = APIServiceErrorCodes.GENERAL_EXCEPTION.getMessage();

		final Locale locale = getLocale(request);

		final String errorMessage = messageSource.getMessage(code, null, locale);

		final ServiceExceptionWrapper exception = new ServiceExceptionWrapper(code, errorMessage, null);

		return new ResponseEntity<ServiceExceptionWrapper>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { APIServiceException.class })
	protected ResponseEntity<ServiceExceptionWrapper> handleAPIServiceException(final APIServiceException ex,
			final WebRequest request) {
		LOGGER.error(ERROR_MESSAGE.apply(ex.getMessage()), ex);
		ex.printStackTrace();

		final Locale locale = getLocale(request);

		final String errorCode = ex.getCode().getMessage();

		final String errorMessage = messageSource.getMessage(ex.getCode().getMessage(), null, locale);

		String errorDetail = null;

		if (ex.getCode().getErrorDetail() != null) {
			errorDetail = messageSource.getMessage(ex.getCode().getErrorDetail(), null, locale);
		} else {
			errorDetail = ex.getMessage();
		}

		final ServiceExceptionWrapper exception = new ServiceExceptionWrapper(errorCode, errorMessage, errorDetail,
				ex.getCode().getHttpStatus().value());

		exception.setErrorOrigin(applicationName);

		return new ResponseEntity<ServiceExceptionWrapper>(exception, ex.getCode().getHttpStatus());
	}

	private Locale getLocale(final WebRequest request) {
		final String localeString = request.getHeader(Constant.LOCALE_HEADER_NAME);

		Locale locale = null;
		if (localeString != null) {
			locale = new Locale(localeString);
		}

		if (locale == null) {
			locale = LocaleContextHolder.getLocale();
		}
		return locale;
	}

}
