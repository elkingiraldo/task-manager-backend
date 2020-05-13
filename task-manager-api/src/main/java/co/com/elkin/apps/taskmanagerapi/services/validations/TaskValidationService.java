package co.com.elkin.apps.taskmanagerapi.services.validations;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.taskmanagerapi.dtos.TaskDTO;
import co.com.elkin.apps.taskmanagerapi.enums.TaskStatus;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceErrorCodes;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;

@Service
public class TaskValidationService extends GeneralValidation {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskValidationService.class);
	private static final String EDC = "edc";
	private static final String DESCRIPTION = "description";

	public void validateCreation(final TaskDTO task, final String requestId) throws APIServiceException {
		LOGGER.info("[TaskValidationService][validateCreation][" + requestId + "] Started.");
		validateObjectNotNull(task, TaskDTO.class.toString());
		validateAttributeNotNull(task.getDescription(), DESCRIPTION);
		validateAttributeNotNull(task.getEdc(), EDC);
		validateEdc(task.getEdc());
		LOGGER.info("[TaskValidationService][validateCreation][" + requestId + "] finished.");
	}

	private void validateEdc(final Date edc) throws APIServiceException {
		if (edc.before(new Date())) {
			throw new APIServiceException(APIServiceErrorCodes.TASK_EDC_BEFORE_EXCEPTION);
		}
	}

	public void autocompleteCreation(final TaskDTO task, final Integer userId, final String requestId) {
		task.setStatus(TaskStatus.PENDING);
		task.setUserId(userId);
	}

}
