package server.facade;

import java.util.List;

import server.commands.Command;
import server.model.User;
import shared.model.Game;
import shared.model.GameState;
import shared.request.game.*;
import shared.request.move.*;

public interface IServerFacade {

	/**
	 * Sets the server's log level (ALL, SEVERE, WARNING ,INFO, CONFIG, FINE, FINER, FINEST, OFF)
	 * @param request the object containing the request data
	 * @return true on success, false on failure
	 */
	public boolean changeLogLevel(ChangeLogLevelRequest request);
	
	/**
	 * Returns the current state of the game.
	 * @return
	 */
	public GameState model(int gameId);
	
	/**
	 * Clears out the command history of the current game
	 * @return
	 */
	public GameState reset(int gameId);
	
	/**
	 * Executes the specified command list in the current game.
	 * @param commands the list of commands to execute
	 * @return
	 */
	public GameState commands(int gameId, List<Command> commands);
	
	/**
	 * Returns a list of commands that have been executed in the current game.
	 * @return
	 */
	public List<Command> commands(int gameId);
	
	
	/**
	 * Gets a list of all games in progess.
	 * @return
	 */
	public List<Game> list();
	
	/**
	 * Creates a new game.
	 * @param request the object containing the request data
	 * @return true on success, false on failure
	 */
	public Game create(CreateGameRequest request);
	
	/**
	 * Adds (or re-adds) the player to the specified game, and sets their catan.game HTTP cookie
	 * @param request the object containing the request data
	 * @return
	 */
	public void join(int playerId, JoinGameRequest request);
	
	/**
	 * Saves the current state of the specified game to a file.
	 * @param request the object containing the request data
	 * @return
	 */
	public void save(SaveGameRequest request);
	
	/**
	 * Loads a previously saved game file to restore the state of a game.
	 * @param request the object containing the request data
	 * @return
	 */
	public void load(LoadGameRequest request);
	

	/**
	 * Sends a chat message
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState sendChat(int gameId, SendChatRequest request);
	
	/**
	 * Used to roll a number a the beginning of your turn.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState rollNumber(int gameId, RollNumberRequest request);
	
	/**
	 * Moves the robber, selecting the new robber position and player to rob.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState robPlayer(int gameId, RobPlayerRequest request);
	
	/**
	 * Used to finish your turn.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState finishTurn(int gameId, FinishTurnRequest request);
	
	/**
	 * Used to buy a development card.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState buyDevCard(int gameId, BuyDevCardRequest request);
	
	/**
	 * Plays a 'Year of Plenty' card from your hand to gain the two specified resources.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState yearOfPlenty(int gameId, YearOfPlentyCardRequest request);
	
	/**
	 * Plays a 'Road Building' card from your hand to build two roads at the specified locations.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState roadBuilding(int gameId, RoadBuildingCardRequest request);

	/**
	 * Plays a 'Soldier' from your hand, selecting the new robber position and player to rob.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState soldier(int gameId, SoldierCardRequest request);

	/**
	 * Plays a 'Monopoly' card from your hand to monopolize the specified resource.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState monopoly(int gameId, MonopolyCardRequest request);

	/**
	 * Plays a 'Monument' card from your hand to give you a victory point.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState monument(int gameId, MonumentCardRequest request);
	
	/**
	 * POST /moves/buildRoad Builds a road at the specified location. 
	 * @param request the object containing the request data (Set 'free' to true during initial setup.)
	 * @return
	 */
	public GameState buildRoad(int gameId, BuildRoadRequest request);

	/**
	 * Builds a settlement at the specified location.
	 * @param request the object containing the request data (Set 'free' to true during initial setup.)
	 * @return
	 */
	public GameState buildSettlement(int gameId, BuildSettlementRequest request);

	/**
	 * Builds a city at the specified location.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState buildCity(int gameId, BuildCityRequest request);

	/**
	 * Offers a domestic trade to another player.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState offerTrade(int gameId, OfferTradeRequest request);

	/**
	 * Used to accept or reject a trade offered to you.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState acceptTrade(int gameId, AcceptTradeRequest request);

	/**
	 * Used to execute a maritime trade.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState maritimeTrade(int gameId, MaritimeTradeRequest request);

	/**
	 * Discards the specified resource cards.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState discardCards(int gameId, DiscardRequest request);

	/**
	 * Validates the player's credentials, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
	 * @param request the object containing the request data
	 * @return true if credentials are good, false otherwise
	 */
	public User login(UserRequest request);

	/**
	 * Creates a new player account, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
	 * @param request the object containing the request data
	 * @return true if success, false if username already exists
	 */
	public User register(UserRequest request);
}
