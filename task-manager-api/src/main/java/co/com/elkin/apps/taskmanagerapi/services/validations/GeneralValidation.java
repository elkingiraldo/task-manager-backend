package co.com.elkin.apps.taskmanagerapi.services.validations;

import java.util.Objects;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import co.com.elkin.apps.taskmanagerapi.exception.APIServiceErrorCodes;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;

/**
 * General validations for the API
 * 
 * @author egiraldo
 *
 */
@Service
public class GeneralValidation {

	protected static final String EMPTY_STRING = "";
	private static final Function<String, String> DTO_NAME = name -> "DTO: " + name;

	/**
	 * Validate if an object is null or not
	 * 
	 * @param obj
	 * @param className
	 * @throws APIServiceException
	 */
	protected void validateObjectNotNull(final Object obj, final String className) throws APIServiceException {
		if (Objects.isNull(obj)) {
			throw new APIServiceException(DTO_NAME.apply(className),
					APIServiceErrorCodes.GENERAL_DTO_CANT_BE_NULL_EXCEPTION);
		}
	}

	/**
	 * Validate if an attribute is null or not
	 * 
	 * @param atribute,      Object to validate if it's null
	 * @param attributeName, String with the name of the attribute
	 * @throws APIServiceException
	 */
	protected void validateAttributeNotNull(final Object atribute, final String attributeName)
			throws APIServiceException {
		if (Objects.isNull(atribute)) {
			throw new APIServiceException(attributeName, APIServiceErrorCodes.GENERAL_ATTRIBUTE_REQUIRED_EXCEPTION);
		}
	}

	/**
	 * Validate if a string is empty
	 * 
	 * @param attribute,     String to validate if it's empty
	 * @param attributeName, String with the name of the attribute
	 * @throws APIServiceException
	 */
	protected void validateEmptyString(final String attribute, final String attributeName) throws APIServiceException {
		if (EMPTY_STRING.equals(attribute.trim())) {
			throw new APIServiceException(attributeName, APIServiceErrorCodes.TASK_DESCRIPTION_CANT_BE_EMPTY_EXCEPTION);
		}
	}

	/**
	 * Validate if an object is present into the attributes
	 * 
	 * @param obj
	 * @param attributeName
	 * @throws APIServiceException
	 */
	protected void valitadeObjectIsPresentInAttributes(final Object obj, final String attributeName)
			throws APIServiceException {
		if (Objects.isNull(obj)) {
			throw new APIServiceException(DTO_NAME.apply(attributeName),
					APIServiceErrorCodes.GENERAL_ATTRIBUTE_REQUIRED_EXCEPTION);
		}
	}

}
