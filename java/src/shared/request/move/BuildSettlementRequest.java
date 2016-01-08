package shared.request.move;

import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.*;
/**
 * BuildSettlementRequest object is used to encapsulate the data sent on a BuildSettlementState call to the server.
 *
 */
public class BuildSettlementRequest extends MoveRequest {

	private VertexLocation untranslate() {
		return new VertexLocation(new HexLocation(vertexLocation.getX(),vertexLocation.getY()),vertexLocation.getDirection());
	}
	
	private TranslatedVertexLocation vertexLocation;
	private boolean free;
	
	public VertexLocation getVertexLocation() {
		return untranslate();
	}
	public void setVertexLocation(VertexLocation vertexLocation) {
		this.vertexLocation = new TranslatedVertexLocation(vertexLocation);
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
	 * @param vertexLocation
	 * @param free
	 * @pre 0 <= playerIndex <= 4
	 * @pre vertexLocation is not null
	 * @pre boolean is not null
	 * @post playerIndex, vertexLocation, free are stored in this object
	 */
	public BuildSettlementRequest(int playerIndex, VertexLocation vertexLocation, boolean free) {
		super("buildSettlement", playerIndex);
		this.vertexLocation = new TranslatedVertexLocation(vertexLocation);
		this.free = free;
	}
}
