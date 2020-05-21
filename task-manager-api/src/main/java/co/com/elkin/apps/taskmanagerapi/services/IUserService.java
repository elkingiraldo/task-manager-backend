package co.com.elkin.apps.taskmanagerapi.services;

import co.com.elkin.apps.taskmanagerapi.entities.User;

public interface IUserService {

	public User retrieveUserByName(final String userName);

}
