package shared.states.playing;

import client.proxy.ProxyFacade;

/**
 * Created by Thomas on 2/28/2015.
 */
public class DiscardWaitingState extends State {

	@Override
	public boolean advanceFromDiscard() {
		ProxyFacade.getInstance().setPlayingState(new PlayingState());
		return true;
	}

	@Override
	public String getState() {
		return "DISCARDWAITING";
	}
}
