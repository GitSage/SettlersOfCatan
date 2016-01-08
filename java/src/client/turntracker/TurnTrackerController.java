package client.turntracker;

import client.data.PlayerInfo;
import client.proxy.ProxyFacade;
import client.base.*;
import shared.definitions.TurnStatus;
import shared.model.GameState;
import shared.model.Player;
import shared.request.move.FinishTurnRequest;
import shared.states.playing.PlayingState;

import java.util.Observable;
import java.util.Observer;


/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	
	private GameState gameState;
	private boolean[] initialized;
	
	public TurnTrackerController(ITurnTrackerView view) {

		super(view);
		initialized = new boolean[4];
		GameState.get().addObserver(this);
	}

	@Override
	public void update(Observable state, Object obj) {
		gameState = (GameState) state;
		initFromModel();
	}

	@Override
	public ITurnTrackerView getView() {

		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
		int currPlayer = GameState.get().getTurnTracker().getCurrentTurn();
		if(GameState.get().canFinishTurn(currPlayer)){
			ProxyFacade.getInstance().finishTurn(new FinishTurnRequest(currPlayer));
			//Reset the states
			ProxyFacade.getInstance().setPlayingState(new PlayingState());
		}
	}

	private void initFromModel() {
		PlayerInfo localPlayer = ProxyFacade.getInstance().getLocalPlayer();
		getView().setLocalPlayerColor(localPlayer.getColor());
		for ( Player daPlaya : gameState.getPlayers() ) {
			int playerIndex = daPlaya.getPlayerIndex();
			if(!initialized[playerIndex]) {
				getView().initializePlayer(daPlaya.getPlayerIndex(), daPlaya.getName(), daPlaya.getColor());
				initialized[playerIndex] = true;
			}
			boolean largestArmy = gameState.getTurnTracker().getLargestArmy() == daPlaya.getPlayerIndex();
			boolean longestRoad = gameState.getTurnTracker().getLongestRoad() == daPlaya.getPlayerIndex();
			boolean currentTurn = daPlaya.getPlayerIndex() == gameState.getTurnTracker().getCurrentTurn();
			getView().updatePlayer(daPlaya.getPlayerIndex(),daPlaya.getVictoryPoints(), currentTurn, largestArmy, longestRoad);
		}
		if (gameState.isTurnAndStatus(localPlayer.getPlayerIndex(), TurnStatus.PLAYING)) {
			//Only time they can end the turn is when the status is playing
			getView().updateGameState("Finish Turn", true);
		}	else if (gameState.isTurn(localPlayer.getPlayerIndex())) {
			//Switch to handle all of the other statuses
			switch (gameState.getTurnTracker().getStatus()) {
				case FIRSTROUND:
					getView().updateGameState("Setup Phase 1", false);
					break;

				case SECONDROUND:
					getView().updateGameState("Setup Phase 2", false);
					break;

				case ROLLING:
					getView().updateGameState("Rolling Dice", false);
					break;

				case ROBBING:
					getView().updateGameState("Put 'em up (Robbing)", false);
					break;

				case DISCARDING:
					getView().updateGameState("Discarding", false);
					break;
			}
		}	else {
			//Not my turn so just hold on for a bit.
			getView().updateGameState("Waiting for players", false);
		}

	}
}

