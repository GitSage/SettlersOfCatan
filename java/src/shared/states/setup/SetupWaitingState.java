package shared.states.setup;

import client.main.Catan;
import client.map.MapController;
import client.proxy.ProxyFacade;
import shared.model.GameState;
import shared.states.setup.BuildRoadSetupState;
import shared.states.setup.ISetupState;
import shared.states.setup.FinishedSetupState;
import shared.states.setup.SetupState;

/**
 * Created by thomas on 2/25/2015.
 */
public class SetupWaitingState extends SetupState {

	private int stage;
	private MapController map;

	SetupWaitingState(int stage) {
		this.stage = stage;
	}

	public void advance() {
		if (GameState.get().isTurn(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex())) {
			Catan.getPoller().stop();

			if (stage != 2) {
				//Stop polling
				//Increase the stage
				ISetupState road = new BuildRoadSetupState(stage + 1);
				road.setMap(map);
				ProxyFacade.getInstance().setSetupState(road);
				road.advance();
			} else {
				ProxyFacade.getInstance().setSetupState(new FinishedSetupState());
				Catan.getPoller().start();
			}
		} else {
			Catan.getPoller().start();
		}
	}

	@Override
	public void setMap(MapController map) {
		this.map = map;
	}
}
