package co.com.elkin.apps.taskmanagerapi.services.converters;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.taskmanagerapi.dtos.TaskDTO;
import co.com.elkin.apps.taskmanagerapi.entities.Task;

/**
 * Converter service from DTO to entity and From entity to DTO for tasks
 * 
 * @author egiraldo
 *
 */
@Service
public class TaskConverterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskConverterService.class);

	/**
	 * This method transforms {@link List<Task>} to {@link List<TaskDTO>}
	 * 
	 * @param entities
	 * @param requestId
	 * @return {@link List<TaskDTO>}
	 */
	public List<TaskDTO> toDtos(final List<Task> entities, final String requestId) {
		LOGGER.info("[TasksConverterService][toDtos][" + requestId + "] Started. Number of entities found: "
				+ entities.size());
		final List<TaskDTO> dtoList = new ArrayList<TaskDTO>();

		for (final Task Task : entities) {
			final ModelMapper modelMapper = new ModelMapper();
			dtoList.add(modelMapper.map(Task, TaskDTO.class));
		}
		LOGGER.info("[TasksConverterService][toDtos][" + requestId + "] finished.");
		return dtoList;
	}

	/**
	 * This method transforms {@link Task} to {@link TaskDTO}
	 * 
	 * @param entity, Task entity
	 * @return {@link TaskDTO}
	 */
	public TaskDTO toDTO(final Task entity) {
		final ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(entity, TaskDTO.class);
	}

	/**
	 * This method transforms {@link TaskDTO} to {@link Task}
	 * 
	 * @param dto,       Task DTO
	 * @param requestId, unique UUID for tracking request
	 * @return {@link Task}
	 */
	public Task toEntity(final TaskDTO dto, final String requestId) {
		LOGGER.info("[TaskConverterService][toEntity][" + requestId + "] Started.");
		final ModelMapper modelMapper = new ModelMapper();
		final Task entity = modelMapper.map(dto, Task.class);
		LOGGER.info("[TaskConverterService][toEntity][" + requestId + "] Finished.");
		return entity;
	}

}
