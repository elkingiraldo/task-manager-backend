package co.com.elkin.apps.taskmanagerapi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.elkin.apps.taskmanagerapi.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
	
	public List<Task> findByUserId(Integer id);

}
