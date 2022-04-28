package com.revature.services;

import java.util.Optional;

import com.revature.exceptions.RegistrationUnsuccessfulException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

/**
 * The UserService should handle the processing and retrieval of Users for the ERS application.
 *
 * {@code getByUsername} is the only method required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create User</li>
 *     <li>Update User Information</li>
 *     <li>Get Users by ID</li>
 *     <li>Get Users by Email</li>
 *     <li>Get All Users</li>
 * </ul>
 */
public class UserService {

	public UserDAO ud;

	public UserService() {
		ud = new UserDAO();
	}

	public UserService(UserDAO ud) {
		this.ud = ud;
	}

	/**
	 *  Create User
	 */

	public User createUser(User userToBeRegistered) throws RuntimeException {
		User createdUser;
		try {
			createdUser = ud.create(userToBeRegistered);
		} catch (RegistrationUnsuccessfulException e) {
			throw e;
		}
		return createdUser;
	}

	/**
	 * Change Things
	 */
	public User changePassword(String newPassword, User user) throws RuntimeException {
		Optional<User> changedUser;

		changedUser = ud.updatePassword(newPassword, user);
		if (changedUser.isPresent())
			return changedUser.get();
		else
			throw new RuntimeException("Changing password of user " + user.getUsername() + " failed.");
	}

	public User changeRole(Role newRole, User user) throws RuntimeException {
		Optional<User> changedUser;

		changedUser = ud.updateRole(newRole, user);
		if (changedUser.isPresent())
			return changedUser.get();
		else
			throw new RuntimeException();
	}


	/**
	 *     Should retrieve a User with the corresponding username or an empty optional if there is no match.
     */
	public Optional<User> getByUsername(String username) {
		return ud.getByUsername(username);
	}

	public Optional<User> getByID(int id) {
		return ud.getByID(id);
	}

	public Optional<User> getByEmailAddress(String emailAddress) {
		return ud.getByEmail(emailAddress);
	}

	public User getByUser(User user) {
		return ud.getByID(user.getId()).get();
	}


}
