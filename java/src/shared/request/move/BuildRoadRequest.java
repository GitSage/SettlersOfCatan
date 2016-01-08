package shared.request.move;

import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.TranslatedEdgeLocation;

/**
 * BuildRoadRequest object is used to encapsulate the data sent on a BuildRoadState call to the server.
 *
 */
public class BuildRoadRequest extends MoveRequest {
	
	private EdgeLocation untranslate() {
		return new EdgeLocation(new HexLocation(roadLocation.getX(),roadLocation.getY()),roadLocation.getDirection());
	}
	
	private TranslatedEdgeLocation roadLocation;
	private boolean free;
	
	public EdgeLocation getRoadLocation() {
		return untranslate();
	}
	public void setRoadLocation(EdgeLocation roadLocation) {
		this.roadLocation = new TranslatedEdgeLocation(roadLocation);
	}
	
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
	}
	
	/**
	 * 
	 * @param playerIndex
	 * @param roadLocation
	 * @param free
	 * @pre 0 <= playerIndex <= 4
	 * @pre roadLocation is not null
	 * @pre free is not null
	 * @post playerIndex, roadLocation, free are stored in this object
	 */
	public BuildRoadRequest(int playerIndex, EdgeLocation roadLocation, boolean free) {
		super("buildRoad", playerIndex);
		this.roadLocation = new TranslatedEdgeLocation(roadLocation);
		this.free = free;
	}
	
	
	
}
