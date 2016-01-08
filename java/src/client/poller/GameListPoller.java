package client.poller;

import client.data.GameInfo;
import client.join.IJoinGameView;
import client.proxy.ProxyFacade;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by jared on 3/2/15.
 */
public class GameListPoller extends TimerTask {
	private IJoinGameView joinGameView;

	public GameListPoller(IJoinGameView joinGameView) {
		this.joinGameView = joinGameView;
	}

	@Override
	public void run() {
		List<GameInfo> gameList = ProxyFacade.getInstance().list();
		joinGameView.setGames(gameList.toArray(new GameInfo[gameList.size()]), ProxyFacade.getInstance().getLocalPlayer());
//		this.joinGameView.showModal();
	}
}
