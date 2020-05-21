package co.com.elkin.apps.taskmanagerapi.services;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import co.com.elkin.apps.taskmanagerapi.constants.Constant;
import co.com.elkin.apps.taskmanagerapi.entities.Task;
import co.com.elkin.apps.taskmanagerapi.enums.TaskStatus;

/**
 * This component will change status from pending to done for old tasks
 * 
 * @author egiraldo
 *
 */
@Component
public class ExpirationTaskService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExpirationTaskService.class);

	@Autowired
	private TaskServiceImpl taskService;

	@Scheduled(cron = Constant.CRON_LEAVING_DATE)
	public void changeTaskStatus() {

		LOGGER.info("[ExpirationTaskService][changeTaskStatus]. Starting scheduler...");

		final List<Task> allTask = taskService.getAllTask();
		final Date currentDate = new Date();

		allTask.forEach(task -> {
			if (task.getEdc().before(currentDate) && task.getStatus().equals(TaskStatus.PENDING)) {
				task.setStatus(TaskStatus.DONE);
			}
		});

		taskService.saveAllTask(allTask);

		LOGGER.info("[ExpirationTaskService][changeTaskStatus]. Finishing scheduler...");
	}

}
