package co.com.elkin.apps.taskmanagerapi.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.com.elkin.apps.taskmanagerapi.entities.Task;
import co.com.elkin.apps.taskmanagerapi.entities.User;
import co.com.elkin.apps.taskmanagerapi.enums.TaskStatus;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureEmbeddedDatabase
public class FlywayDbMigrationTest {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("Given that Flyway migrates all initial data, When TaskRepository is called by Task ID, Then the data is stored OK")
	public void callClientRepositorybyId() {
		final Optional<Task> optionalTask = taskRepository.findById(2);
		assertThat(optionalTask).hasValueSatisfying(task -> {
			assertThat(task.getId()).isNotNull();
			assertThat(task.getDescription()).isEqualTo("I need to do somenthing");
			assertThat(task.getStatus()).isEqualTo(TaskStatus.DONE);
			assertThat(task.getUserId()).isEqualTo(1);
			assertThat(task.getEdc()).isBefore(new Date());
		});
	}

	@Test
	@DisplayName("Given that Flyway migrates all initial data, When UserRepository is called, Then the data is all stored")
	public void callIDRepository() {
		final List<User> userList = userRepository.findAll();
		assertThat(userList).hasSize(3);
		userList.forEach(user -> {
			assertThat(user.getId()).isNotNull();
			assertThat(user.getUserName()).isNotNull();
			assertThat(user.getPassword()).isNotNull();
		});
	}

}
