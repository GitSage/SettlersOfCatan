package client.proxy;

import client.data.GameInfo;
import client.data.PlayerInfo;
import shared.model.*;
import shared.request.game.*;
import shared.request.move.*;
import shared.states.playing.PlayingState;
import shared.states.setup.ISetupState;
import shared.states.playing.IState;
import shared.states.setup.StartSetupState;

import java.util.List;

/**
 * This class serves as a large wrapper class.
 * The client will only use this proxy which will call all the other proxy classes methods
 */
public class ProxyFacade {

	private PlayerInfo localPlayer;
	private ISetupState setupState;
	private IState playingState;
	private static ProxyFacade instance;
	private GameProxy gameProxy;
	private GamesProxy gamesProxy;
	private MovesProxy movesProxy;
	private Proxy proxy;
	private UtilProxy utilProxy;
	private UserProxy userProxy;
	
	public static void setInstance(ProxyFacade instance) {
		ProxyFacade.instance = instance;
	}

	public static void nuke() {
		getInstance().getProxy().deleteGameCookie();
		getInstance().getSetupState().getMap().resetView();
		getInstance().setSetupState(new StartSetupState());

		getInstance().setPlayingState(new PlayingState());
		getInstance().setGameProxy(new GameProxy(getInstance().getProxy()));
		getInstance().setGamesProxy(new GamesProxy(getInstance().getProxy()));
		getInstance().setMovesProxy(new MovesProxy(getInstance().getProxy()));
	}
	
	public void setGameProxy(GameProxy gameProxy) {
		this.gameProxy = gameProxy;
	}

	public void setGamesProxy(GamesProxy gamesProxy) {
		this.gamesProxy = gamesProxy;
	}

	public void setMovesProxy(MovesProxy movesProxy) {
		this.movesProxy = movesProxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	public void setUtilProxy(UtilProxy utilProxy) {
		this.utilProxy = utilProxy;
	}

	public void setUserProxy(UserProxy userProxy) {
		this.userProxy = userProxy;
	}

	public void setLocalPlayer(PlayerInfo localPlayer) {
		this.localPlayer = localPlayer;
	}

	public Proxy getProxy() {
		return proxy;
	}
	
	public static void init(Proxy instanceProxy) {
		instance = new ProxyFacade(instanceProxy);
	}

	public static ProxyFacade getInstance() {
		return instance;
	}

	/**
	 * Constructor class
	 * Should initialize the Proxy
	 */
	public ProxyFacade(Proxy proxy) {
		this.proxy = proxy;
		this.userProxy = new UserProxy(proxy);
		this.gamesProxy = new GamesProxy(proxy);
		this.gameProxy = new GameProxy(proxy);
		this.movesProxy = new MovesProxy(proxy);
		this.utilProxy = new UtilProxy(proxy);
		this.setupState = new StartSetupState();
		this.playingState = new PlayingState();
	}

	/**************************************/
	/******Proxy Facade Classes************/
	/**************************************/
	public PlayerInfo getLocalPlayer() { return localPlayer; }

	public ISetupState getSetupState() {
		return setupState;
	}

	public void setSetupState(ISetupState state) {
		this.setupState = state;
	}

	public IState getPlayingState() {
		return playingState;
	}

	public void setPlayingState(IState state) {
		this.playingState = state;
	}

	/**************************************/
	/**************USERPROXY***************/
	/**************************************/

	/**
	 * Wrapper class for UserProxy
	 * @param request
	 * @return
	 */
	public int login(UserRequest request) {
		int result = this.userProxy.login(request);
   		this.localPlayer = this.proxy.getPlayerCookie();
		return result;
	}

	/**
	 * Wrapper class for UserProxy
	 * @param request
	 * @return
	 */
	public int register(UserRequest request) {
		int result = this.userProxy.register(request);
   		this.localPlayer = this.proxy.getPlayerCookie();
		return result;
	}

	/**************************************/
	/***********END USERPROXY**************/
	/**************************************/


	/**************************************/
	/**********GAMEPROXY*******************/
	/**************************************/

	public GameState model() {
			return this.model(GameState.get().getVersion());
	}

	/**
	 * Wrapper for GameProxy
	 * @return current game state from server
	 */
	public GameState model(int version) {
		return gameProxy.model(version);
	}



	/**
	 * Wrapper for GameProxy
	 * Returns the game back to right before the team placement phase
	 * @return GameState after it's been reset
	 */
	public GameState reset() {
		return  this.gameProxy.reset();
	}

	/**
	 * Wrapper for GameProxy
	 * Takes a list of commands and executes them on the server
	 * @param commandList
	 * @return GameState after commands have been executed
	 */
	public GameState commands(List<GameCommand> commandList) {
		return this.gameProxy.commands(commandList);
	}

	/**
	 * Wrapper for GameProxy
	 * Returns a list of all available commands from the server
	 * @return GameCommand
	 */
	public List<GameCommand> commands() {
		return this.gameProxy.commands();
	}

	/**
	 * Wrapper for GameProxy
	 * Adds an ai to the game
	 * @param request
	 * @return -1 for fail and 1 for success
	 */
	public int addAi(AddAiRequest request) {
		return this.gameProxy.addAi(request);
	}

	/**
	 * Wrapper for GameProxy
	 * @return list of AI types
	 */
	public List<String> listAi() {
		return this.gameProxy.listAi();
	}

	/**************************************/
	/******END GAMEPROXY*******************/
	/**************************************/


	/**************************************/
	/******GAMESPROXY**********************/
	/**************************************/

	/**
	 * Wrapper for GamesProxy
	 * Sends a request to the server and gets all games running on server
	 * @return an array of Games
	 */
	public List<GameInfo> list() {
		return this.gamesProxy.list();
	}

	/**
	 * Wrapper for GamesProxy
	 * Sends a request to server and creates a game
	 * @param request
	 * @return null if failed or a GameInfo Object
	 */
	public GameInfo create(CreateGameRequest request) {
		return this.gamesProxy.create(request);
	}

	/**
	 * Wrapper for GamesProxy
	 * Joins the player to the designated game
	 * @param request
	 * @return -1 if failed
	 * @return 1 if succeeded in joining
	 */
	public int join(JoinGameRequest request) {
		return this.gamesProxy.join(request);
	}

	/**
	 * Wrapper for GamesProxy
	 * Saves the game for debugging purposes.
	 * Allows the dev to reinstate the game from the buggy state
	 * @param request
	 * @return -1 if failed
	 * @return 1 if save succeeded
	 */
	public int save(SaveGameRequest request) {
		return this.gamesProxy.save(request);
	}

	/**
	 * Wrapper for GamesProxy
	 * Loads a previously saved game file
	 * This is for debug purposes only
	 * @param request
	 * @return -1 if game failed to load
	 * @return 1 if game loaded
	 */
	public int load(LoadGameRequest request) {
		return this.gamesProxy.load(request);
	}

	/**************************************/
	/******END GAMESPROXY******************/
	/**************************************/

	/**************************************/
	/******MOVESPROXY**********************/
	/**************************************/

	/**
	 * Wrapper for MovesProxy
	 * Sends a chat object to server and returns the state of the game
	 * @param chat
	 * @return GameState after chat is sent
	 */
	public GameState sendChat(SendChatRequest chat) {
		return this.movesProxy.sendChat(chat);
	}

	/**
	 * Wrapper for MovesProxy
	 * Sends a roll to the server
	 * @param roll
	 * @return GameState after roll
	 */
	public GameState rollNumber(RollNumberRequest roll) {
		return this.movesProxy.rollNumber(roll);
	}

	/**
	 * Wrapper for MovesProxy
	 * Sends a request to server to rob player and then returns resulting game state
	 * @param robbery
	 * @return GameState after robbery takes place
	 */
	public GameState robPlayer(RobPlayerRequest robbery) {
		return this.movesProxy.robPlayer(robbery);
	}

	/**
	 * Wrapper for MovesProxy
	 * Finishes the player turn and sends request to server
	 * @param finish
	 * @return GameState after the turn has ended
	 */
	public GameState finishTurn(FinishTurnRequest finish) {
		return this.movesProxy.finishTurn(finish);
	}

	/**
	 * Wrapper for MovesProxy
	 * Purchases a dev card and updates server with request
	 * @param purchase
	 * @return GameState after purchase
	 */
	public GameState buyDevCard(BuyDevCardRequest purchase) {
		return this.movesProxy.buyDevCard(purchase);
	}

	/**
	 * Wrapper for MovesProxy
	 * Plays the year of plenty card for player
	 * @param card
	 * @return GameState after the card has been played
	 */
	public GameState yearOfPlenty(YearOfPlentyCardRequest card) {
		return this.movesProxy.yearOfPlenty(card);
	}

	/**
	 * Wrapper for MovesProxy
	 * Plays the RoadBuilding card for the player
	 * @param card
	 * @return GameState after card is played
	 */
	public GameState roadBuilding(RoadBuildingCardRequest card) {
		return this.movesProxy.roadBuilding(card);
	}

	/**
	 * Wrapper for MovesProxy
	 * Plays the Soldier Card for the player
	 * @param card
	 * @return GameState after card is played
	 */
	public GameState soldier(SoldierCardRequest card) {
		return this.movesProxy.soldier(card);
	}

	/**
	 * Wrapper for MovesProxy
	 * Plays the monopoly card for player
	 * @param card
	 * @return GameState after card is played
	 */
	public GameState monopoly(MonopolyCardRequest card) {
		return this.movesProxy.monopoly(card);
	}

	/**
	 * Wrapper for MovesProxy
	 * Plays Monument card for player
	 * @param card
	 * @return GameState after card played
	 */
	public GameState monument(MonumentCardRequest card) {
		return this.movesProxy.monument(card);
	}

	/**
	 * Wrapper for MovesProxy
	 * Builds a road for the player
	 * @param request
	 * @return GameState after road built
	 */
	public GameState buildRoad(BuildRoadRequest request) {
		return this.movesProxy.buildRoad(request);
	}

	/**
	 * Wrapper for MovesProxy
	 * Builds a settlement for the player
	 * @param request
	 * @return GameState after settlement built
	 */
	public GameState buildSettlement(BuildSettlementRequest request) {
		return this.movesProxy.buildSettlement(request);
	}

	/**
	 * Wrapper for MovesProxy
	 * Builds a city for the player
	 * @param request
	 * @return GameState after city built
	 */
	public GameState buildCity(BuildCityRequest request) {
		return this.movesProxy.buildCity(request);
	}

	/**
	 * Wrapper for MovesProxy
	 * Offers a trade to other players
	 * @param request
	 * @return GameState after trade request
	 */
	public GameState offerTrade(OfferTradeRequest request) {
		return this.movesProxy.offerTrade(request);
	}

	/**
	 * Wrapper for MovesProxy
	 * Accept/Decline an offer from another player
	 * @param request
	 * @return GameState after accepting request
	 */
	public GameState acceptTrade(AcceptTradeRequest request) {
		return this.movesProxy.acceptTrade(request);
	}

	/**
	 * Wrapper for MovesProxy
	 * Transacts a maritime trade for player
	 * @param request
	 * @return GameState after trade
	 */
	public GameState maritimeTrade(MaritimeTradeRequest request) {
		return this.movesProxy.maritimeTrade(request);
	}

	/**
	 * Wrapper for MovesProxy
	 * Discards cards for player
	 * @param request
	 * @return GameState after discard
	 */
	public GameState discardCards(DiscardRequest request) {
		return this.movesProxy.discardCards(request);
	}

	/**************************************/
	/******END MOVESPROXY******************/
	/**************************************/




	/**************************************/
	/**********UTILPROXY*******************/
	/**************************************/

	/**
	 * Wrapper for utilProxy
	 * @param logLevel
	 * @return -1 if failure
	 * @return 1 if success
	 */
	public int ChangeLogLevelRequest(ChangeLogLevelRequest logLevel) {
		return this.utilProxy.changeLogLevel(logLevel);
	}

	/**************************************/
	/******END UTILPROXY*******************/
	/**************************************/
}