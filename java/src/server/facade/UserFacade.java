package server.facade;

import java.util.List;
import java.util.logging.Logger;

import server.Server;
import server.model.User;
import shared.request.game.UserRequest;

/**
 * This class is the go between for the server user handlers and the command classes.
 * The server handlers call this class and this class creates command classes, executes the methods, and then returns the info.
 */
public class UserFacade {

	public UserFacade() {
		super();
		Logger.getLogger("catanserver").finest("User Facade Created");
	}

	/**
	 * Validates the player's credentials
	 * @param request the object containing the request data
	 * @return the user if found, null if not found
	 */
	public User login(UserRequest request) {
		Logger.getLogger("catanserver").finer("in user facade login method");
		List<User> users = Server.state.getUsers();
		for(User user : users) {
			Logger.getLogger("catanserver").finest("searching for user, looking at: " + user.toString());
			if(user.getUsername().equals(request.getUsername()) && user.getPassword().equals(request.getPassword())) {
				Logger.getLogger("catanserver").finer("user found: " + user.toString());
				return user;
			}
		}
		Logger.getLogger("catanserver").finer("user not found, returning null");
		return null;
	}

	/**
	 * Creates a new player account
	 * @param request the object containing the request data
	 * @return the user if success, null if failure (ie the username already exists)
	 */
	public User register(UserRequest request) {
		Logger.getLogger("catanserver").finer("in user facade register method");
		List<User> users = Server.state.getUsers();
		for(User user : users) {
			Logger.getLogger("catanserver").finest("searching for username, looking at: " + user.toString());
			if(user.getUsername().equals(request.getUsername())) {
				Logger.getLogger("catanserver").finer("username found: " + user.toString() + "\nreturning null");
				return null;
			}
		}
		
		User newUser = new User(request.getUsername(),request.getPassword());
		newUser.setUserId(users.size());
		users.add(newUser);
		Logger.getLogger("catanserver").finer("user not found, user created: " + newUser.toString());
		return newUser;
	}
}
