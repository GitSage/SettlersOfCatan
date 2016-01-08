package shared.request.game;

public class RegisterRequest {

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
	 * 
	 * @param username
	 * @param password
	 */
	public RegisterRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

}
