package shared.request.game;

import shared.definitions.CatanColor;

/**
 * JoinGameRequest object is used to encapsulate the data sent on a JoinGame call to the server.
 *
 */
public class JoinGameRequest {

    private int id;
	private CatanColor color;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public CatanColor getColor() {
		return color;
	}
	public void setColor(CatanColor color) {
		this.color = color;
	}
	
	/**
	 * @param id
	 * @param color
	 * @pre id >= 0;
	 * @pre color is in (red,orange,yellow,blue,green,purple,puce,white,brown)
	 * @post id and color are stored in this object
	 */
	public JoinGameRequest(int id, CatanColor color) {
		super();
		this.id = id;
		this.color = color;
	}
	
}
