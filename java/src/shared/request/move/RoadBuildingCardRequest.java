package shared.request.move;

import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.*;

/**
 * RoadBuildingCardRequest object is used to encapsulate the data sent on a RoadBuildingCard call to the server.
 *
 */
public class RoadBuildingCardRequest extends MoveRequest {
	
	private TranslatedEdgeLocation spot1;
	private TranslatedEdgeLocation spot2;
	
	private EdgeLocation untranslate(TranslatedEdgeLocation roadLocation) {
		return new EdgeLocation(new HexLocation(roadLocation.getX(),roadLocation.getY()),roadLocation.getDirection());
	}
	
	public EdgeLocation getSpot1() {
		return untranslate(spot1);
	}
	public void setSpot1(EdgeLocation spot1) {
		this.spot1 = new TranslatedEdgeLocation(spot1);
	}

	public EdgeLocation getSpot2() {
		return untranslate(spot2);
	}
	public void setSpot2(EdgeLocation spot2) {
		this.spot2 = new TranslatedEdgeLocation(spot2);
	}

	/**
	 * 
	 * @param playerIndex
	 * @param spot1
	 * @param spot2
	 * @pre 0 <= playerIndex <= 4
	 * @pre spot1 and spot2 are not null
	 * @post playerIndex, spot1, spot2 are stored in this object
	 */
	public RoadBuildingCardRequest(int playerIndex, EdgeLocation spot1, EdgeLocation spot2) {
		super("Road_Building", playerIndex);
		this.spot1 = new TranslatedEdgeLocation(spot1);
		this.spot2 = new TranslatedEdgeLocation(spot2);
	}
	
}
