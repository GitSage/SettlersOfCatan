package client.join;

import client.poller.GameListPoller;
import client.proxy.ProxyFacade;
import shared.definitions.CatanColor;
import client.base.*;
import client.data.*;
import client.misc.*;
import shared.request.game.CreateGameRequest;
import shared.request.game.JoinGameRequest;

import java.util.List;
import java.util.Timer;


/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	private JoinGameRequest joinGameRequest;
	private GameListPoller poller;

	/**
	 * JoinGameController constructor
	 * 
	 * @param view Join game view
	 * @param newGameView New game view
	 * @param selectColorView Select color view
	 * @param messageView Message view (used to display error messages that occur while the user is joining a game)
	 */
	public JoinGameController(IJoinGameView view, INewGameView newGameView, 
								ISelectColorView selectColorView, IMessageView messageView) {

		super(view);

		setNewGameView(newGameView);
		setSelectColorView(selectColorView);
		setMessageView(messageView);
	}
	
	public IJoinGameView getJoinGameView() {
		
		return (IJoinGameView)super.getView();
	}
	
	/**
	 * Returns the action to be executed when the user joins a game
	 * 
	 * @return The action to be executed when the user joins a game
	 */
	public IAction getJoinAction() {
		
		return joinAction;
	}

	/**
	 * Sets the action to be executed when the user joins a game
	 * 
	 * @param value The action to be executed when the user joins a game
	 */
	public void setJoinAction(IAction value) {	
		
		joinAction = value;
	}
	
	public INewGameView getNewGameView() {
		
		return newGameView;
	}

	public void setNewGameView(INewGameView newGameView) {
		
		this.newGameView = newGameView;
	}
	
	public ISelectColorView getSelectColorView() {
		
		return selectColorView;
	}
	public void setSelectColorView(ISelectColorView selectColorView) {
		
		this.selectColorView = selectColorView;
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	public void setMessageView(IMessageView messageView) {
		
		this.messageView = messageView;
	}

	@Override
	public void start() {
		List<GameInfo> daList = ProxyFacade.getInstance().list();
		getJoinGameView().setGames(daList.toArray(new GameInfo[daList.size()]), ProxyFacade.getInstance().getLocalPlayer());
		getJoinGameView().showModal();
		poller = new GameListPoller(getJoinGameView());
		Timer timer = new Timer();
		timer.schedule(poller, 500, 3000);
	}

	@Override
	public void startCreateNewGame() {
		
		getNewGameView().showModal();
	}

	@Override
	public void cancelCreateNewGame() {
		
		getNewGameView().closeModal();
	}

	@Override
	public void createNewGame() {
		//Creates a game
		String gameName = getNewGameView().getTitle();
		String validName = "^[a-zA-Z0-9_\\-]+$";
		if (gameName.matches(validName)) {
			CreateGameRequest request = new CreateGameRequest(getNewGameView().getRandomlyPlaceHexes(), getNewGameView().getRandomlyPlaceNumbers(), getNewGameView().getUseRandomPorts(), gameName);
			GameInfo game = ProxyFacade.getInstance().create(request);

			//Then joins the game that was just created
			if (game != null) {
				JoinGameRequest joinRequest = new JoinGameRequest(game.getId(), CatanColor.WHITE);
				ProxyFacade.getInstance().join(joinRequest);
			}

			List<GameInfo> daList = ProxyFacade.getInstance().list();
			getJoinGameView().setGames(daList.toArray(new GameInfo[daList.size()]), null);
			getNewGameView().closeModal();
		} else {
			messageView.setTitle("Error");
			messageView.setMessage("The game name must match this regular expression: " + validName);
			messageView.showModal();
		}
	}

	@Override
	public void startJoinGame(GameInfo game) {
		//System.out.println("I'm Joining a game");
		joinGameRequest = new JoinGameRequest(game.getId(), CatanColor.WHITE);
		enableAllColors();
		for (PlayerInfo daPlaya : game.getPlayers()) {
			if (daPlaya.getId() != ProxyFacade.getInstance().getLocalPlayer().getId()) {
				getSelectColorView().setColorEnabled(daPlaya.getColor(), false);
			}
		}
		getSelectColorView().showModal();
	}

	private void enableAllColors() {
		getSelectColorView().setColorEnabled(CatanColor.BLUE, true);
		getSelectColorView().setColorEnabled(CatanColor.BROWN, true);
		getSelectColorView().setColorEnabled(CatanColor.GREEN, true);
		getSelectColorView().setColorEnabled(CatanColor.ORANGE, true);
		getSelectColorView().setColorEnabled(CatanColor.PUCE, true);
		getSelectColorView().setColorEnabled(CatanColor.PURPLE, true);
		getSelectColorView().setColorEnabled(CatanColor.RED, true);
		getSelectColorView().setColorEnabled(CatanColor.WHITE, true);
		getSelectColorView().setColorEnabled(CatanColor.YELLOW, true);
	}

	@Override
	public void cancelJoinGame() {
	
		getJoinGameView().closeModal();
	}

	@Override
	public void joinGame(CatanColor color) {
		//System.out.println(color);
		joinGameRequest.setColor(color);

		ProxyFacade.getInstance().join(joinGameRequest);
		ProxyFacade.getInstance().getLocalPlayer().setColor(color);
		// If join succeeded
		getSelectColorView().closeModal();
		getJoinGameView().closeModal();
		poller.cancel();
		joinAction.execute();
	}

}

