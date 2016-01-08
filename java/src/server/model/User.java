package server.model;

import com.google.gson.annotations.SerializedName;

public class User {
	
	private String authentication;
	@SerializedName("name")
	private String username;
	private String password;
	@SerializedName("playerID")
	private int userId;

	private void createAuthentication() {
		StringBuilder dumboTheElephant = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			int temp = (int) (Math.random()*10);
			dumboTheElephant.append(temp);
		}
		authentication = dumboTheElephant.toString();
	}
	
	public User(int userId, String username, String password) {
		this(username, password);
		this.userId = userId;
		createAuthentication();
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
		createAuthentication();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

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

	@Override
	public boolean equals(Object obj) {

		if ( ! (obj instanceof server.model.User) ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		server.model.User daUser = (server.model.User) obj;
		return (daUser.getUsername().equals(this.getUsername())
						&& daUser.getPassword().equals(this.getPassword())
						&& daUser.getUserId() == this.getUserId());

	}
}
