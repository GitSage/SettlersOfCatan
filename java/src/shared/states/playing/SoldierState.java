package shared.states.playing;

import client.data.RobPlayerInfo;
import client.proxy.ProxyFacade;
import shared.request.move.SoldierCardRequest;

public class SoldierState extends RobState {
	@Override
	public void sendRobRequest(RobPlayerInfo victim) {
		SoldierCardRequest request = new SoldierCardRequest(
                ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(),
				victim.getPlayerIndex(),
				ProxyFacade.getInstance().getPlayingState().getRobHex());
		ProxyFacade.getInstance().soldier(request);
	}
}
