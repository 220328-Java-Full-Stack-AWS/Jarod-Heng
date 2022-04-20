package com.revature.services;

import com.revature.exceptions.*;
import com.revature.models.User;

import java.util.Objects;
import java.util.Optional;

/**
 * The AuthService should handle login and registration for the ERS application.
 *
 * {@code login} and {@code register} are the minimum methods required; however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Retrieve Currently Logged-in User</li>
 *     <li>Change Password</li>
 *     <li>Logout</li>
 * </ul>
 */
public class AuthService {

    UserService userService;
    User currentUser;

    public AuthService() {
        this.userService = new UserService();
    }

    /**
     * User Login Authorization
     * <ul>
     *     <li>Needs to check for existing users with username/email provided.</li>
     *     <li>Must throw exception if user does not exist.</li>
     *     <li>Must compare password provided and stored password for that user.</li>
     *     <li>Should throw exception if the passwords do not match.</li>
     *     <li>Must return user object if the user logs in successfully.</li>
     * </ul>
     */
    public User Login(String username, String password) throws RuntimeException {
        UserService userService = new UserService();
        Optional<User> user = userService.getByUsername(username);
        if(user.isPresent()) {
            // the username exists, check the passwords
            if (Objects.equals(user.get().getPassword(), password)) {
                currentUser = user.get();
                return currentUser;
            } else {
                System.out.println("Wrong Password for user " + currentUser.getUsername());
                throw new PasswordDoesNotMatch();
            }
        } else {
            // the username does not exist in the database
            throw new UserNotFoundException();
        }
    }

    /**
     * User Registration
     * <ul>
     *     <li>Should ensure that the username/email provided is unique.</li>
     *     <li>Must throw exception if the username/email is not unique.</li>
     *     <li>Should persist the user object upon successful registration.</li>
     *     <li>Must throw exception if registration is unsuccessful.</li>
     *     <li>Must return user object if the user registers successfully.</li>
     *     <li>Must throw exception if provided user has a non-zero ID</li>
     * </ul>
     *
     * Note: userToBeRegistered will have an id=0, additional fields may be null.
     * After registration, the id will be a positive integer.
     */
    public User Register(User userToBeRegistered) throws RuntimeException {
        User newUser;

        // userToBeRegistered.getUsername() check uniqueness through DB
        // if the username isnt unique, throw exception
        if(userService.getByUsername(userToBeRegistered.getUsername()).isPresent()){
            throw new UsernameNotUniqueException();
        }

        // userToBeRegistered.getUsername() check uniqueness through DB
        // if the username isnt unique, throw exception
        if(userService.getByEmailAddress(userToBeRegistered.getEmail()).isPresent()){
            throw new EmailNotUniqueException();
        }

        // if the given initial ID is not 0, throw exception
        if (userToBeRegistered.getId() != 0)
            throw new NewUserHasNonZeroIdException();

        // now we know the username is unique, and we can register the user and get a newID
        // if registration was unsuccessful, throws RegistrationUnsuccessfulException();
        try {
            userService.createUser(userToBeRegistered);
        } catch (RegistrationUnsuccessfulException e) {
            throw e;
        }

        //verify presence in the database
        try {
            newUser = userService.getByUsername(userToBeRegistered.getUsername()).get();
            // after successful registration, return the user object
            return newUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public User changePassword(String newPassword) {
        return userService.changePassword(newPassword, currentUser);
    }

    public User changePassword(String newPassword, User user) {
        return userService.changePassword(newPassword, user);
    }

    /*
    public User changeUsername(String newUsername) {

    }
    */

    /**
     * Retrieves the currently logged-in user.
     * It leverages the Optional type which is a useful interface to handle the
     * possibility of a user being unavailable.
     */
    public Optional<User> RetrieveCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public void logout() {
        this.currentUser = null;
    }


}
