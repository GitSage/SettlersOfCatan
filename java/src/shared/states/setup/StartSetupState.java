package shared.states.setup;


import client.main.Catan;
import client.proxy.ProxyFacade;
import shared.definitions.TurnStatus;
import shared.model.GameState;
import shared.states.setup.BuildRoadSetupState;
import shared.states.setup.FinishedSetupState;
import shared.states.setup.SetupState;
import shared.states.setup.SetupWaitingState;

/**
 * Created by thomas on 2/25/2015.
 */
public class StartSetupState extends SetupState {

	@Override
	public void advanceFromAi() {
		Catan.getPoller().stop();
		if (GameState.get().getTurnTracker().getStatus() == TurnStatus.FIRSTROUND || GameState.get().getTurnTracker().getStatus() == TurnStatus.SECONDROUND) {
			if (GameState.get().isTurn(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex())) {
				ProxyFacade.getInstance().setSetupState(new BuildRoadSetupState(1));
			} else {
				ProxyFacade.getInstance().setSetupState(new SetupWaitingState(0));
			}
		} else {
			Catan.getPoller().start();
			ProxyFacade.getInstance().setSetupState(new FinishedSetupState());
		}
	}

	@Override
	public String getState() {
		return "START";
	}
}
