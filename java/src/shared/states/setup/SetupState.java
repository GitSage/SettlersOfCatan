package shared.states.setup;

import client.map.MapController;
import shared.locations.HexLocation;
import shared.states.setup.ISetupState;

/**
 * Created by Thomas on 2/25/2015.
 */
public abstract class SetupState implements ISetupState {

	private int stage;
	private MapController map;
	protected HexLocation hexLocation;

	public void advance() { return;	}

	public void advanceFromAi() {
		return;
	}

	public void startGame() { return; }

	@Override
	public void setMap(MapController map) {
		this.map = map;
	}
	
	@Override
	public MapController getMap() {
		return map;
	}

	public String getState() {
		return "WTF";
	}

}
