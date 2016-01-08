package shared.states.playing;

import client.proxy.ProxyFacade;
import shared.locations.EdgeLocation;
import shared.request.move.RoadBuildingCardRequest;

/**
 * Created by jared on 2/28/15.
 */
public class RoadBuildState extends PlayingState {
	private EdgeLocation tempLocation;

	public EdgeLocation getTempLocation() {
		return tempLocation;
	}

	@Override
	public boolean placeRoad(EdgeLocation location) {
		if (tempLocation == null) {
			tempLocation = location;
			return true;
		} else {
			RoadBuildingCardRequest request = new RoadBuildingCardRequest(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), tempLocation, location);
			ProxyFacade.getInstance().roadBuilding(request);
			return false;
		}
	}
}
