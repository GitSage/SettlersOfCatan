package shared.states.playing;

import client.data.RobPlayerInfo;
import client.map.MapController;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

/**
 * Created by thomas on 2/25/2015.
 */
public interface IState {

	public void setRobHex(HexLocation hexLoc);

	public HexLocation getRobHex();

	public boolean advanceFromDiscard();

	public String getState();

	public boolean placeRoad(EdgeLocation location);

	public void sendRobRequest(RobPlayerInfo victim);

}
