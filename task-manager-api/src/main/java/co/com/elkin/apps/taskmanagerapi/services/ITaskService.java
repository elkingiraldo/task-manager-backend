package co.com.elkin.apps.taskmanagerapi.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import co.com.elkin.apps.taskmanagerapi.dtos.TaskDTO;

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
	 * @param request,   HTTP request from user
	 * @param requestId, ID for tracking the request
	 * @return {@link TaskDTO} a list of DTO found
	 */
	public List<TaskDTO> retrieveTasks(final HttpServletRequest request, final String requestId);

}
