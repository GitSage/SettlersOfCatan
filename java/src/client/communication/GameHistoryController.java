package client.communication;

import java.util.*;
import java.util.List;

import client.base.*;
import shared.model.GameState;


/**
 * Game history controller implementation
 */
public class GameHistoryController extends Controller implements IGameHistoryController, Observer {

//	private GameState gameState;

	public GameHistoryController(IGameHistoryView view) {
		
		super(view);
		GameState.get().addObserver(this);
		initFromModel();
	}

	public void update(Observable state, Object obj) {
//		gameState = (GameState) state;
		initFromModel();
	}

	@Override
	public IGameHistoryView getView() {
		
		return (IGameHistoryView)super.getView();
	}
	
	private void initFromModel() {

		List<LogEntry> entries = GameState.get().getGameHistory();
		getView().setEntries(entries);
	}
	
}

