package co.com.elkin.apps.taskmanagerapi.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.taskmanagerapi.entities.User;
import co.com.elkin.apps.taskmanagerapi.repositories.UserRepository;

/**
 * Custom User details for validating users in DB
 * 
 * @author egiraldo
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) {

		final Optional<User> optionalUser = userRepository.findByUserName(username);

		if (!optionalUser.isPresent()) {
			throw new BadCredentialsException(username);
		}

		final User user = optionalUser.get();

		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				new ArrayList<>());
	}

}
