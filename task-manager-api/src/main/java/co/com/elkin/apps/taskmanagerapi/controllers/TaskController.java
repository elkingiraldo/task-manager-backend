package co.com.elkin.apps.taskmanagerapi.controllers;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.elkin.apps.taskmanagerapi.dtos.TaskDTO;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;
import co.com.elkin.apps.taskmanagerapi.services.TaskServiceImpl;

/**
 * Controller for handling all request related to user tasks
 * 
 * @author egiraldo
 *
 */
@CrossOrigin(origins={ "${client.application.baseUrl}" })
@RestController
@RequestMapping("/v1.0/tasks")
public class TaskController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private TaskServiceImpl taskService;

	/**
	 * This method takes GET API request for handling and passing it to correct
	 * service application layer for searching all current tasks for a specific user
	 * 
	 * @param request, HTTP request from user
	 * @param locale,  language the client wants to use.
	 * @return {@link List<TaskDTO>}, a list of tasks found in DB
	 */
	@GetMapping()
	public ResponseEntity<List<TaskDTO>> tasks(final HttpServletRequest request,
			@RequestHeader(value = "locale", required = false) final String locale) {

		final String requestId = UUID.randomUUID().toString();
		LOGGER.info("[TaskController][tasks][" + requestId + "] Started.");

		final List<TaskDTO> tasks = taskService.retrieveTasks(request, requestId);

		LOGGER.info("[TaskController][tasks][" + requestId + "] Finished. Returned this number of task [" + tasks.size()
				+ "]");
		return new ResponseEntity<List<TaskDTO>>(tasks, HttpStatus.OK);
	}

	/**
	 * This method takes POST API request for handling and passing it to correct
	 * service application layer for creating a new task.
	 * 
	 * @param task,   task to be created
	 * @param locale, language the client wants to use.
	 * @return {@link TaskDTO}, task saved in DB.
	 * @throws APIServiceException when something was wrong during the process
	 */
	@PostMapping()
	public ResponseEntity<TaskDTO> create(final HttpServletRequest request, @RequestBody final TaskDTO task,
			@RequestHeader(value = "locale", required = false) final String locale) throws APIServiceException {

		final String requestId = UUID.randomUUID().toString();
		LOGGER.info("[TaskController][create][" + requestId + "] Started.");

		final TaskDTO taskCreated = taskService.createTask(request, task, requestId);

		LOGGER.info("[TaskController][create][" + requestId + "] Finished.");
		return new ResponseEntity<TaskDTO>(taskCreated, HttpStatus.CREATED);
	}

}
