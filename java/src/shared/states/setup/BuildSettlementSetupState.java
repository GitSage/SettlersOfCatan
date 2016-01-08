package shared.states.setup;

import client.map.MapController;
import client.proxy.ProxyFacade;
import shared.definitions.PieceType;

/**
 * Created by thomas on 2/25/2015.
 */
public class BuildSettlementSetupState extends SetupState {

	private int stage;
	private MapController map;

	public BuildSettlementSetupState(int stage) {
		this.stage = stage;
	}

	public void setMap(MapController map) {
		this.map = map;
	}

	public void advance() {
		map.getView().startDrop(PieceType.SETTLEMENT, ProxyFacade.getInstance().getLocalPlayer().getColor(), false);
		ProxyFacade.getInstance().setSetupState(new SetupWaitingState(stage));
	}
}
