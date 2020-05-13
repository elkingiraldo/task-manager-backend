package co.com.elkin.apps.taskmanagerapi.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import co.com.elkin.apps.taskmanagerapi.controllers.TaskController;
import co.com.elkin.apps.taskmanagerapi.dtos.TaskDTO;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;
import co.com.elkin.apps.taskmanagerapi.services.TaskServiceImpl;

public class TaskControllerTest {

	@InjectMocks
	private TaskController taskController;
	@Mock
	private TaskServiceImpl taskService;

	private final List<TaskDTO> dtoList = new ArrayList<>();

	private final TaskDTO task01 = new TaskDTO();
	private final TaskDTO task02 = new TaskDTO();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		task01.setDescription("some description 1");
		task01.setEdc(new Date());

		task02.setDescription("some description 2");
		task02.setEdc(new Date());

		dtoList.add(task01);
		dtoList.add(task02);
	}

	@Test
	public void onlyOneCallToTaskServiceRetrieveTasks() {
		taskController.tasks(any(), anyString());
		verify(taskService, times(1)).retrieveTasks(any(), anyString());
	}

	@Test
	public void dtoListNotAffectedInTasksMethod() {
		when(taskService.retrieveTasks(any(), anyString())).thenReturn(dtoList);
		final ResponseEntity<List<TaskDTO>> allTasks = taskController.tasks(any(), anyString());
		assertEquals(dtoList.size(), allTasks.getBody().size());
		assertEquals(dtoList, allTasks.getBody());
	}

	@Test
	public void onlyOneCallToTaskServicePost() throws APIServiceException {
		taskController.create(any(), any(), anyString());
		verify(taskService, times(1)).createTask(any(), any(), anyString());
	}

	@Test
	public void dtoNotAffectedInCreateMethod() throws APIServiceException {
		when(taskService.createTask(any(), any(), anyString())).thenReturn(task02);
		final ResponseEntity<TaskDTO> response = taskController.create(any(), any(), anyString());
		assertEquals(task02, response.getBody());
	}

}
