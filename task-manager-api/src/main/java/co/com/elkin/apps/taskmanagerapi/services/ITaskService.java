package co.com.elkin.apps.taskmanagerapi.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import co.com.elkin.apps.taskmanagerapi.dtos.TaskDTO;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;

/**
 * Interface in charge of handling all task requests
 * 
 * @author egiraldo
 *
 */
public interface ITaskService {

	/**
	 * This method will call all classes and methods required to build the correct
	 * answer with the list of all current user's tasks
	 * 
	 * @param username,  user logged in
	 * @param request,   HTTP request from user
	 * @param requestId, ID for tracking the request
	 * @return {@link TaskDTO} a list of DTO found
	 * @throws APIServiceException when token doesn't match with username
	 */
	public List<TaskDTO> retrieveTasks(final String username, final HttpServletRequest request, final String requestId)
			throws APIServiceException;

	public TaskDTO createTask(final HttpServletRequest request, final TaskDTO task, final String username,
			final String requestId) throws APIServiceException;

}
