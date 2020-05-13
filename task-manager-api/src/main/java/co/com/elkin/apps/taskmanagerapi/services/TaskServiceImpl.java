package co.com.elkin.apps.taskmanagerapi.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.taskmanagerapi.dtos.TaskDTO;
import co.com.elkin.apps.taskmanagerapi.entities.User;
import co.com.elkin.apps.taskmanagerapi.repositories.TaskRepository;
import co.com.elkin.apps.taskmanagerapi.security.JwtUtil;
import co.com.elkin.apps.taskmanagerapi.services.converters.TaskConverterService;

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
	private UserServiceImpl userServiceImpl;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private TaskConverterService taskConverterService;

	@Override
	public List<TaskDTO> retrieveTasks(final HttpServletRequest request, final String requestId) {

		LOGGER.info("[TaskServiceImpl][retrieveTasks][" + requestId + "] Started.");

		final String usernameFromHeader = jwtUtil.getUsernameFromHeader(request, requestId);
		final User user = userServiceImpl.retrieveUserByName(usernameFromHeader);
		final List<TaskDTO> taskList = taskConverterService.toDtos(taskRepository.findByUserId(user.getId()),
				requestId);

		LOGGER.info("[TaskServiceImpl][retrieveTasks][" + requestId + "] Finished.");
		return taskList;
	}

}
