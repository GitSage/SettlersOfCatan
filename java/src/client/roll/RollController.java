package client.roll;

import client.data.PlayerInfo;
import shared.definitions.TurnStatus;
import shared.model.GameState;
import shared.request.move.RollNumberRequest;
import client.base.*;
import client.proxy.ProxyFacade;
import shared.states.playing.PlayingState;

import java.util.Observable;
import java.util.Observer;


/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController, Observer {

	private IRollResultView resultView;
	private GameState gameState;
	/**
	 * RollController constructor
	 * 
	 * @param view Roll view
	 * @param resultView Roll result view
	 */
	public RollController(IRollView view, IRollResultView resultView) {

		super(view);
		GameState.get().addObserver(this);
		setResultView(resultView);

	}
	
	public IRollResultView getResultView() {
		return resultView;
	}
	public void setResultView(IRollResultView resultView) {
		this.resultView = resultView;
	}

	public IRollView getRollView() {
		return (IRollView)getView();
	}
	
	@Override
	public void rollDice() {

		//simulate rolling dice
		int die1 = (int )(Math.random() * 6 + 1);//random number from 1-6
		int die2 = (int )(Math.random() * 6 + 1);//random number from 1-6
		int numberRolled = die1 + die2;
		
		//send the request to the server and save then update GameState
		RollNumberRequest request = new RollNumberRequest(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(),numberRolled);
		GameState newState = ProxyFacade.getInstance().rollNumber(request);
		GameState.get().updateGameState(newState);
		getResultView().setRollValue(numberRolled);
		getResultView().showModal();
		//My turn set the state
		ProxyFacade.getInstance().setPlayingState(new PlayingState());
	}

	public void update(Observable state, Object obj) {
		gameState = (GameState) state;
		PlayerInfo localPlayer = ProxyFacade.getInstance().getLocalPlayer();
		if ( gameState.isTurnAndStatus(localPlayer.getPlayerIndex(), TurnStatus.ROLLING)) {
			getRollView().showModal();
		}

	}

}

