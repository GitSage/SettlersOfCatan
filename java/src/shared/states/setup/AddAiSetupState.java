package shared.states.setup;

import client.main.Catan;
import client.proxy.ProxyFacade;
import shared.model.GameState;

/**
 * Created by Thomas on 2/25/2015.
 */
public class AddAiSetupState extends SetupState {

	@Override
	public void advanceFromAi() {
		Catan.getPoller().stop();
		if (GameState.get().isTurn(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex())) {
			ProxyFacade.getInstance().setSetupState(new BuildRoadSetupState(1));
		} else {
			ProxyFacade.getInstance().setSetupState(new SetupWaitingState(0));
		}
	}

	@Override
	public String getState() {
		return "ADDAI";
	}
}
