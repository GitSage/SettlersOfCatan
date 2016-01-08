package shared.request.move;

import shared.locations.HexLocation;
/**
 * SoldierRequest object is used to encapsulate the data sent on a Soldier call to the server.
 *
 */
public class SoldierCardRequest extends MoveRequest {

	private int victimIndex;
	private HexLocation location;
	
	public int getVictimIndex() {
		return victimIndex;
	}
	public void setVictimIndex(int victimIndex) {
		this.victimIndex = victimIndex;
	}
	
	public HexLocation getLocation() {
		return location;
	}
	public void setLocation(HexLocation location) {
		this.location = location;
	}
	
	/**
	 * 
	 * @param playerIndex
	 * @param victimIndex
	 * @param location
	 * @pre 0 <= playerIndex <= 4
	 * @pre 0 <= victimIndex <= 4
	 * @pre HexLocation is not null
	 * @post playerIndex, victimIndex, HexLocation are stored in this object
	 */
	public SoldierCardRequest(int playerIndex, int victimIndex,	HexLocation location) {
		super("Soldier", playerIndex);
		if(victimIndex > 4) {
			this.victimIndex = -1;
		}
		else {
			this.victimIndex = victimIndex;
		}
		this.location = location;
	}
	
}
