package shared.states.setup;

import client.map.MapController;
import shared.locations.HexLocation;

/**
 * Created by thomas on 2/25/2015.
 */
public interface ISetupState {

	public void advance();

	public void advanceFromAi();

	public void setMap(MapController map);

	public void startGame();

	public String getState();
	
	public MapController getMap();
}
