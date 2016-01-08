package server.facade;

import java.util.List;
import java.util.logging.Logger;

import server.commands.Command;
import server.model.User;
import shared.model.Game;
import shared.model.GameState;
import shared.request.game.ChangeLogLevelRequest;
import shared.request.game.CreateGameRequest;
import shared.request.game.JoinGameRequest;
import shared.request.game.LoadGameRequest;
import shared.request.game.SaveGameRequest;
import shared.request.game.UserRequest;
import shared.request.move.AcceptTradeRequest;
import shared.request.move.BuildCityRequest;
import shared.request.move.BuildRoadRequest;
import shared.request.move.BuildSettlementRequest;
import shared.request.move.BuyDevCardRequest;
import shared.request.move.DiscardRequest;
import shared.request.move.FinishTurnRequest;
import shared.request.move.MaritimeTradeRequest;
import shared.request.move.MonopolyCardRequest;
import shared.request.move.MonumentCardRequest;
import shared.request.move.MoveRequest;
import shared.request.move.OfferTradeRequest;
import shared.request.move.RoadBuildingCardRequest;
import shared.request.move.RobPlayerRequest;
import shared.request.move.RollNumberRequest;
import shared.request.move.SendChatRequest;
import shared.request.move.SoldierCardRequest;
import shared.request.move.YearOfPlentyCardRequest;

/**
 * Go between for the server handlers and the facade classes so that we can have a mock implementation. 
 */
public class ServerFacade implements IServerFacade {

	private GameFacade game;
	private GamesFacade games;
	private MovesFacade moves;
	private UserFacade user;
	private UtilFacade util;
	
	public ServerFacade(GameFacade game, GamesFacade games, MovesFacade moves, UserFacade user, UtilFacade util) {
		super();
		Logger.getLogger("catanserver").finest("In server facade constructor");
		this.game = game;
		this.games = games;
		this.moves = moves;
		this.user = user;
		this.util = util;
	}

	@Override
	public boolean changeLogLevel(ChangeLogLevelRequest request) {
		Logger.getLogger("catanserver").fine("In server facade change log level method");
		return util.changeLogLevel(request);
	}

	@Override
	public GameState model(int gameId) {
		Logger.getLogger("catanserver").fine("In server facade model method");
		return game.model(gameId);
	}

	@Override
	public GameState reset(int gameId) {
		Logger.getLogger("catanserver").fine("In server facade reset method");
		return game.reset(gameId);
	}

	@Override
	public GameState commands(int gameId, List<Command> commands) {
		Logger.getLogger("catanserver").fine("In server facade commands (execute) method");
		return game.commands(gameId,commands);
	}

	@Override
	public List<Command> commands(int gameId) {
		Logger.getLogger("catanserver").fine("In server facade commands (get) method");
		return game.commands(gameId);
	}

	@Override
	public List<Game> list() {
		Logger.getLogger("catanserver").fine("In server facade list method");
		return games.list();
	}

	@Override
	public Game create(CreateGameRequest request) {
		Logger.getLogger("catanserver").fine("In server facade create method");
		return games.create(request);
	}

	@Override
	public void join(int playerId, JoinGameRequest request) {
		Logger.getLogger("catanserver").fine("In server facade join method");
		games.join(playerId, request);
	}

	@Override
	public void save(SaveGameRequest request) {
		Logger.getLogger("catanserver").fine("In server facade save method");
		games.save(request);
	}

	@Override
	public void load(LoadGameRequest request) {
		Logger.getLogger("catanserver").fine("In server facade load method");
		games.load(request);
	}

	@Override
	public GameState sendChat(int gameId, SendChatRequest request) {
		Logger.getLogger("catanserver").fine("In server facade send chat method");
		return moves.sendChat(gameId, request);
	}

	@Override
	public GameState rollNumber(int gameId, RollNumberRequest request) {
		Logger.getLogger("catanserver").fine("In server facade roll number method");
		return moves.rollNumber(gameId, request);
	}

	@Override
	public GameState robPlayer(int gameId, RobPlayerRequest request) {
		Logger.getLogger("catanserver").fine("In server facade rob player method");
		return moves.robPlayer(gameId, request);
	}

	@Override
	public GameState finishTurn(int gameId, FinishTurnRequest request) {
		Logger.getLogger("catanserver").fine("In server facade finish turn method");
		return moves.finishTurn(gameId, request);
	}

	@Override
	public GameState buyDevCard(int gameId, BuyDevCardRequest request) {
		Logger.getLogger("catanserver").fine("In server facade buy dev card method");
		return moves.buyDevCard(gameId, request);
	}

	@Override
	public GameState yearOfPlenty(int gameId, YearOfPlentyCardRequest request) {
		Logger.getLogger("catanserver").fine("In server facade year of plenty method");
		return moves.yearOfPlenty(gameId, request);
	}

	@Override
	public GameState roadBuilding(int gameId, RoadBuildingCardRequest request) {
		Logger.getLogger("catanserver").fine("In server facade road building method");
		return moves.roadBuilding(gameId, request);
	}

	@Override
	public GameState soldier(int gameId, SoldierCardRequest request) {
		Logger.getLogger("catanserver").fine("In server facade soldie method");
		return moves.soldier(gameId, request);
	}

	@Override
	public GameState monopoly(int gameId, MonopolyCardRequest request) {
		Logger.getLogger("catanserver").fine("In server facade monopoly method");
		return moves.monopoly(gameId, request);
	}

	@Override
	public GameState monument(int gameId, MonumentCardRequest request) {
		Logger.getLogger("catanserver").fine("In server facade monument method");
		return moves.monument(gameId, request);
	}

	@Override
	public GameState buildRoad(int gameId, BuildRoadRequest request) {
		Logger.getLogger("catanserver").fine("In server facade build road method");
		return moves.buildRoad(gameId, request);
	}

	@Override
	public GameState buildSettlement(int gameId, BuildSettlementRequest request) {
		Logger.getLogger("catanserver").fine("In server facade build settlement method");
		return moves.buildSettlement(gameId, request);
	}

	@Override
	public GameState buildCity(int gameId, BuildCityRequest request) {
		Logger.getLogger("catanserver").fine("In server facade build city method");
		return moves.buildCity(gameId, request);
	}

	@Override
	public GameState offerTrade(int gameId, OfferTradeRequest request) {
		Logger.getLogger("catanserver").fine("In server facade offer trade method");
		return moves.offerTrade(gameId, request);
	}

	@Override
	public GameState acceptTrade(int gameId, AcceptTradeRequest request) {
		Logger.getLogger("catanserver").fine("In server facade accept trade method");
		return moves.acceptTrade(gameId, request);
	}

	@Override
	public GameState maritimeTrade(int gameId, MaritimeTradeRequest request) {
		Logger.getLogger("catanserver").fine("In server facade maritime trade method");
		return moves.maritimeTrade(gameId, request);
	}

	@Override
	public GameState discardCards(int gameId, DiscardRequest request) {
		Logger.getLogger("catanserver").fine("In server facade discard cards method");
		return moves.discardCard(gameId, request);
	}

	@Override
	public User login(UserRequest request) {
		Logger.getLogger("catanserver").fine("In server facade login method");
		return user.login(request);
	}

	@Override
	public User register(UserRequest request) {
		Logger.getLogger("catanserver").fine("In server facade register method");
		return user.register(request);
	}
	
}
