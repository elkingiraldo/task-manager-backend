package co.com.elkin.apps.taskmanagerapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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

import co.com.elkin.apps.taskmanagerapi.dtos.TaskDTO;
import co.com.elkin.apps.taskmanagerapi.entities.Task;
import co.com.elkin.apps.taskmanagerapi.exception.APIServiceException;
import co.com.elkin.apps.taskmanagerapi.repositories.TaskRepository;
import co.com.elkin.apps.taskmanagerapi.security.JwtUtil;
import co.com.elkin.apps.taskmanagerapi.services.converters.TaskConverterService;
import co.com.elkin.apps.taskmanagerapi.services.validations.TaskValidationService;

public class TaskServiceTest {

	private static final String ANY_USER_NAME = "anyusername";
	private static final String ANY_REQUEST_ID = "anyusername";

	@InjectMocks
	private TaskServiceImpl taskService;
	@Mock
	private TaskRepository taskRepository;
	@Mock
	private TaskConverterService taskConverterService;
	@Mock
	private TaskValidationService taskValidationService;
	@Mock
	private JwtUtil jwtUtil;

	private final List<Task> taskList = new ArrayList<>();
	private final Task task01 = new Task();
	private final Task task02 = new Task();

	private final List<TaskDTO> taskListDto = new ArrayList<>();
	private final TaskDTO taskDTO01 = new TaskDTO();
	private final TaskDTO taskDTO02 = new TaskDTO();

	private final Date date01 = new Date();
	private final Date date02 = new Date();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		task01.setId(1);
		task01.setDescription("Some Description 1");
		task01.setEdc(date01);

		task02.setId(2);
		task02.setDescription("Some Description 2");
		task02.setEdc(date02);

		taskList.add(task01);
		taskList.add(task02);

		taskListDto.add(taskDTO01);
		taskListDto.add(taskDTO02);
	}

	@Test
	public void onlyOneCallToTaskRepositoryTaskConverterAndJwtUtilOnRetrieveTasks() throws APIServiceException {
		when(jwtUtil.getUsernameFromHeader(any(), anyString())).thenReturn(ANY_USER_NAME);
		doNothing().when(taskValidationService).validateUser(anyString(), anyString(), anyString());
		taskService.retrieveTasks(ANY_USER_NAME, any(), anyString());
		verify(jwtUtil, times(1)).getUsernameFromHeader(any(), anyString());
		verify(taskValidationService, times(1)).validateUser(anyString(), anyString(), anyString());
		verify(jwtUtil, times(1)).getUserIdFromHeader(any(), anyString());
		verify(taskRepository, times(1)).findByUserId(any());
		verify(taskConverterService, times(1)).toDtos(any(), anyString());
	}

	@Test
	public void shouldReturnAllTasksRelatedToUserId() throws APIServiceException {
		when(taskRepository.findByUserId(any())).thenReturn(taskList);
		when(taskConverterService.toDtos(any(), anyString())).thenReturn(taskListDto);
		final List<TaskDTO> result = taskService.retrieveTasks(ANY_USER_NAME, any(), anyString());
		assertEquals(taskListDto, result);
	}

	@Test
	public void shouldCreateTaskSuccessfully() throws APIServiceException {
		doNothing().when(taskValidationService).validateUser(anyString(), anyString(), anyString());
		doNothing().when(taskValidationService).validateCreation(any(), anyString());
		doNothing().when(taskValidationService).autocompleteCreation(any(), any(), anyString());
		doNothing().when(taskValidationService).validateUser(anyString(), anyString(), anyString());

		when(jwtUtil.getUsernameFromHeader(any(), anyString())).thenReturn(ANY_USER_NAME);
		when(taskConverterService.toEntity(any(), anyString())).thenReturn(task01);
		when(taskRepository.save(any())).thenReturn(task01);
		when(taskConverterService.toDTO(any())).thenReturn(taskDTO01);

		final TaskDTO createdTask = taskService.createTask(any(), any(), ANY_USER_NAME, ANY_REQUEST_ID);
		assertEquals(taskDTO01, createdTask);
	}

}
