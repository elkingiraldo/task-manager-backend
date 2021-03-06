package co.com.elkin.apps.taskmanagerapi.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.taskmanagerapi.dtos.TaskDTO;
import co.com.elkin.apps.taskmanagerapi.entities.Task;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;
import co.com.elkin.apps.taskmanagerapi.repositories.TaskRepository;
import co.com.elkin.apps.taskmanagerapi.security.JwtUtil;
import co.com.elkin.apps.taskmanagerapi.services.converters.TaskConverterService;
import co.com.elkin.apps.taskmanagerapi.services.validations.TaskValidationService;

/**
 * Service implementation for handling all task services
 * 
 * @author egiraldo
 *
 */
@Service
public class TaskServiceImpl implements ITaskService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private TaskConverterService taskConverterService;
	@Autowired
	private TaskValidationService taskValidationService;

	@Override
	public List<TaskDTO> retrieveTasks(final String username, final HttpServletRequest request, final String requestId)
			throws APIServiceException {

		LOGGER.info("[TaskServiceImpl][retrieveTasks][" + requestId + "] Started.");

		taskValidationService.validateUser(username, jwtUtil.getUsernameFromHeader(request, requestId), requestId);
		final Integer userIdFromHeader = jwtUtil.getUserIdFromHeader(request, requestId);
		final List<TaskDTO> taskList = taskConverterService.toDtos(taskRepository.findByUserId(userIdFromHeader),
				requestId);

		LOGGER.info("[TaskServiceImpl][retrieveTasks][" + requestId + "] Finished.");
		return taskList;
	}

	@Override
	public TaskDTO createTask(final HttpServletRequest request, final TaskDTO task, final String username,
			final String requestId) throws APIServiceException {

		LOGGER.info("[TaskServiceImpl][createTask][" + requestId + "] Started.");

		taskValidationService.validateUser(username, jwtUtil.getUsernameFromHeader(request, requestId), requestId);
		taskValidationService.validateCreation(task, requestId);

		final Integer userIdFromHeader = jwtUtil.getUserIdFromHeader(request, requestId);
		taskValidationService.autocompleteCreation(task, userIdFromHeader, requestId);

		final Task savedTaks = taskRepository.save(taskConverterService.toEntity(task, requestId));

		final TaskDTO taskDto = taskConverterService.toDTO(savedTaks);

		LOGGER.info(
				"[TaskServiceImpl][createTask][" + requestId + "] Finished. Task created ID [" + taskDto.getId() + "]");
		return taskDto;
	}

	@Override
	public List<Task> getAllTask() {
		return taskRepository.findAll();
	}

	@Override
	public void saveAllTask(final List<Task> tasks) {
		taskRepository.saveAll(tasks);
	}

}
