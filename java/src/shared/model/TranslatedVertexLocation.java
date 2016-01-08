package shared.model;

import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

/**
 * Only for use in request object so they serialize to JSON easily, but can be passed regular model classes.
 *
 */
public class TranslatedVertexLocation {
	
	private int x;
	private int y;
	private VertexDirection direction;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public VertexDirection getDirection() {
		return direction;
	}
	public void setDirection(VertexDirection direction) {
		this.direction = direction;
	}

	public TranslatedVertexLocation(VertexLocation vertexLocation) {
		super();
		this.x = vertexLocation.getHexLoc().getX();
		this.y = vertexLocation.getHexLoc().getY();
		this.direction = vertexLocation.getDir();
	}
	
}