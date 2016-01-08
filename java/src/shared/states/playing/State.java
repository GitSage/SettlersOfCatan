package shared.states.playing;

import client.data.RobPlayerInfo;
import client.proxy.ProxyFacade;
import shared.definitions.TurnStatus;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.model.GameState;
import shared.request.move.BuildRoadRequest;
import shared.request.move.RobPlayerRequest;
import shared.states.playing.IState;

/**
 * Created by Thomas on 2/25/2015.
 */
public abstract class State implements IState {

	protected HexLocation hexLocation;

	public void setRobHex(HexLocation hexLoc) {
		hexLocation = hexLoc;
	}

	public HexLocation getRobHex() { return hexLocation; }

	public boolean advanceFromDiscard() {
		return false;
	}

	public String getState() {
		return "WTF";
	}

	public boolean placeRoad(EdgeLocation location) {
		TurnStatus turnStatus = GameState.get().getTurnTracker().getStatus();
		boolean isFree = turnStatus == TurnStatus.FIRSTROUND || turnStatus == TurnStatus.SECONDROUND;
		BuildRoadRequest request = new BuildRoadRequest(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), location, isFree);
		GameState.get().updateGameState(ProxyFacade.getInstance().buildRoad(request));
		return false;
	}

	public void sendRobRequest(RobPlayerInfo victim) {
		RobPlayerRequest request = new RobPlayerRequest(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(),
				victim.getPlayerIndex(),
				ProxyFacade.getInstance().getPlayingState().getRobHex());
		ProxyFacade.getInstance().robPlayer(request);
	}

}
