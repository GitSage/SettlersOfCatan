package shared.request.game;

/**
 * LoadGameRequest object is used to encapsulate the data sent on a LoadGame call to the server.
 *
 */
public class LoadGameRequest {

	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param name
	 * @pre name is alphanumeric
	 * @post name is stored in this object
	 */
	public LoadGameRequest(String name) {
		super();
		this.name = name;
	}
	
}
