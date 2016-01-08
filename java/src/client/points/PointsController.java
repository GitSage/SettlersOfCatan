package client.points;

import client.base.*;
import client.main.Catan;
import client.proxy.ProxyFacade;
import shared.model.GameState;
import shared.model.Player;

import java.util.Observable;
import java.util.Observer;


/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController, Observer {

	private GameState gameState;
	private IGameFinishedView finishedView;
	private boolean modalShown;
	
	/**
	 * PointsController constructor
	 * 
	 * @param view Points view
	 * @param finishedView Game finished view, which is displayed when the game is over
	 */
	public PointsController(IPointsView view, IGameFinishedView finishedView) {
		
		super(view);
		modalShown = false;
		GameState.get().addObserver(this);
		setFinishedView(finishedView);
	}

	public void update(Observable state, Object obj) {
		gameState = (GameState) state;
		initFromModel();
	}
	
	public IPointsView getPointsView() {
		
		return (IPointsView)super.getView();
	}
	
	public IGameFinishedView getFinishedView() {
		return finishedView;
	}
	public void setFinishedView(IGameFinishedView finishedView) {
		this.finishedView = finishedView;
	}

	private void initFromModel() {
		getPointsView().setPoints(GameState.get().getPlayer(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex()).getVictoryPoints());
		
//		if(modalShown) {
//			//TODO
//			System.out.println("am I here");
////			GameState.get().nuke();
////			ProxyFacade.getInstance().nuke();
//			Catan.getJoinController().start();
//		}
		
		if (gameState.getWinner() > -1) {
			Player winner = gameState.getPlayerById(gameState.getWinner());
			boolean isLocal = (gameState.getWinner() == ProxyFacade.getInstance().getLocalPlayer().getId());
			getFinishedView().setWinner(winner.getName(), isLocal);
			getFinishedView().showModal();
			modalShown = true;
		}
		
	}
	
}

