package shared.request.game;

/**
 * SaveGameRequest object is used to encapsulate the data sent on a SaveGame call to the server.
 *
 */
public class SaveGameRequest {

	private int id;
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param id
	 * @param name
	 * @pre id >= 0
	 * @post id and name are stored in this object
	 */
	public SaveGameRequest(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
