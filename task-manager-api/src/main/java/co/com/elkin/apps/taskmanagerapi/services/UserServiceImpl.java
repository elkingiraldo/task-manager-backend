package co.com.elkin.apps.taskmanagerapi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.taskmanagerapi.entities.User;
import co.com.elkin.apps.taskmanagerapi.repositories.UserRepository;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User retrieveUserByName(final String userName) {
		final Optional<User> findByUserName = userRepository.findByUserName(userName);
		return findByUserName.get();
	}

}
