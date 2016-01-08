package client.join;

import client.base.*;
import client.main.Catan;
import client.proxy.ProxyFacade;
import shared.model.GameState;
import shared.request.game.AddAiRequest;
import shared.states.setup.AddAiSetupState;

import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController, Observer {

	private int oldPlayerCount;
	private int newPlayerCount;

	public void resetCounts() {
		oldPlayerCount = 0;
		newPlayerCount = 0;
	}
	
	public PlayerWaitingController(IPlayerWaitingView view) {
		super(view);
		GameState.get().addObserver(this);
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {
		GameState.get().updateGameState(ProxyFacade.getInstance().model(9999));
		oldPlayerCount = GameState.get().getPlayerInfoArray().length;
		if (oldPlayerCount < 4) {
			Catan.getPoller().start();
		}
	}

	@Override
	public void addAI() {
		AddAiRequest request = new AddAiRequest(getView().getSelectedAI());
		ProxyFacade.getInstance().addAi(request);
		GameState.get().updateGameState(ProxyFacade.getInstance().model(9999));
	}

	public void update(Observable state, Object obj) {
		nextStep();
	}

	public void nextStep() {
		//Display AI if
		newPlayerCount = GameState.get().getPlayerInfoArray().length;
		if (newPlayerCount < 4) {
			if (newPlayerCount > oldPlayerCount) {
				getView().setPlayers(GameState.get().getPlayerInfoArray());
				//System.out.println("Display AI?");
				List<String> daAiList = ProxyFacade.getInstance().listAi();
				String[] response = daAiList.toArray(new String[daAiList.size()]);
				getView().setAIChoices(response);
				if ( ! ProxyFacade.getInstance().getSetupState().getState().equals("START")) {
					getView().closeModal();
				}
				getView().showModal();
				oldPlayerCount = newPlayerCount;
			}
			ProxyFacade.getInstance().setSetupState(new AddAiSetupState());
		} else {
			if (ProxyFacade.getInstance().getSetupState().getState().equals("ADDAI")) {
				getView().closeModal();
			}
			ProxyFacade.getInstance().getSetupState().advanceFromAi();
		}
	}


}

