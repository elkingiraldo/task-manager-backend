package co.com.elkin.apps.taskmanagerapi.controllers;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.elkin.apps.taskmanagerapi.dtos.TaskDTO;
import co.com.elkin.apps.taskmanagerapi.services.TaskServiceImpl;

/**
 * Controller for handling all request related to user tasks
 * 
 * @author egiraldo
 *
 */
@RestController
@RequestMapping("/v1.0/task")
public class TaskController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private TaskServiceImpl taskService;

	/**
	 * This method takes GET API request for handling and passing it to correct
	 * service application layer for searching all current tasks for a specific user
	 * 
	 * @param request, HTTP request from user
	 * @return {@link List<TaskDTO>}, a list of tasks found in DB
	 */
	@GetMapping()
	public ResponseEntity<List<TaskDTO>> tasks(final HttpServletRequest request) {

		final String requestId = UUID.randomUUID().toString();
		LOGGER.info("[TaskController][tasks][" + requestId + "] Started.");

		final List<TaskDTO> tasks = taskService.retrieveTasks(request, requestId);

		LOGGER.info("[TaskController][tasks][" + requestId + "] Finished.");
		return new ResponseEntity<List<TaskDTO>>(tasks, HttpStatus.OK);
	}

}
