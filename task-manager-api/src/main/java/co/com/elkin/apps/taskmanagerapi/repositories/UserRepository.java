package co.com.elkin.apps.taskmanagerapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.elkin.apps.taskmanagerapi.entities.User;

/**
 * Repository for {@link User}
 * 
 * @author egiraldo
 *
 */
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserName(String username);

}
