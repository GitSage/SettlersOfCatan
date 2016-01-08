package shared.model;

import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;

/**
 * Only for use in request object so they serialize to JSON easily, but can be passed regular model classes.
 *
 */
public class TranslatedEdgeLocation {
	
	private int x;
	private int y;
	private EdgeDirection direction;
	
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

	public EdgeDirection getDirection() {
		return direction;
	}
	public void setDirection(EdgeDirection direction) {
		this.direction = direction;
	}

	public TranslatedEdgeLocation(EdgeLocation roadLocation) {
		super();
		this.x = roadLocation.getHexLoc().getX();
		this.y = roadLocation.getHexLoc().getY();
		this.direction = roadLocation.getDirection();
	}
	
}
