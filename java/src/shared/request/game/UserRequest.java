package shared.request.game;

/**
 * UserRequest object is used to encapsulate the data sent on a User call to the server.
 *
 */
public class UserRequest {

	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @param username
	 * @param password
	 * @pre username and password are alphanumeric
	 * @post username and password are stored in this object
	 */
	public UserRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
}
