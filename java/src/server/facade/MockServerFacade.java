package server.facade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import client.data.PlayerInfo;
import server.commands.Command;
import server.model.User;
import shared.model.*;
import shared.request.game.*;
import shared.request.move.*;

public class MockServerFacade implements IServerFacade{
	
	private GameState returnGameState() {
		GameState gameState = new GameState();
		gameState.setBank(new ResourceList(20,20,20,20,20));
		gameState.setChat(new MessageList());
		gameState.setDeck(new DevCardList(5,5,5,20,5));
		gameState.setLog(new MessageList());
		gameState.setMap(new GameMap());
		LinkedList<Player> players = new LinkedList<Player>();
		players.add(new Player());
		gameState.setPlayers(players);
		gameState.setTradeOffer(null);
		gameState.setTurnTracker(new TurnTracker());
		gameState.setVersion(0);
		return gameState;
	}
	
	@Override
	public boolean changeLogLevel(ChangeLogLevelRequest request) {
		return true;
	}

	@Override
	public GameState model(int gameId) {
		return returnGameState();
	}

	@Override
	public GameState reset(int gameId) {
		return returnGameState();
	}

	@Override
	public GameState commands(int gameId, List<Command> commands) {
		return returnGameState();
	}

	@Override
	public List<Command> commands(int gameId) {
		return new ArrayList<>();
	}

	@Override
	public List<Game> list() {
		List<Game> gameList = new LinkedList<Game>();
		Game game = new Game();
		game.setId(1);
		game.setTitle("TEST");
		game.setPlayers(new LinkedList<PlayerInfo>());
		gameList.add(game);
		return gameList;
	}

	@Override
	public Game create(CreateGameRequest request) {
        Game game = new Game();
        game.setId(1);
        game.setTitle(request.getName());
        game.setPlayers(new LinkedList<PlayerInfo>());
        return game;
	}

	@Override
	public void join(int playerId, JoinGameRequest request) {}

	@Override
	public void save(SaveGameRequest request) {}

	@Override
	public void load(LoadGameRequest request) {}

	@Override
	public GameState sendChat(int gameId, SendChatRequest request) {
		return returnGameState();
	}

	@Override
	public GameState rollNumber(int gameId, RollNumberRequest request) {
		return returnGameState();
	}

	@Override
	public GameState robPlayer(int gameId, RobPlayerRequest request) {
		return returnGameState();
	}

	@Override
	public GameState finishTurn(int gameId, FinishTurnRequest request) {
		return returnGameState();
	}

	@Override
	public GameState buyDevCard(int gameId, BuyDevCardRequest request) {
		return returnGameState();
	}

	@Override
	public GameState yearOfPlenty(int gameId, YearOfPlentyCardRequest request) {
		return returnGameState();
	}

	@Override
	public GameState roadBuilding(int gameId, RoadBuildingCardRequest request) {
		return returnGameState();
	}

	@Override
	public GameState soldier(int gameId, SoldierCardRequest request) {
		return returnGameState();
	}

	@Override
	public GameState monopoly(int gameId, MonopolyCardRequest request) {
		return returnGameState();
	}

	@Override
	public GameState monument(int gameId, MonumentCardRequest request) {
		return returnGameState();
	}

	@Override
	public GameState buildRoad(int gameId, BuildRoadRequest request) {
		return returnGameState();
	}

	@Override
	public GameState buildSettlement(int gameId, BuildSettlementRequest request) {
		return returnGameState();
	}

	@Override
	public GameState buildCity(int gameId, BuildCityRequest request) {
		return returnGameState();
	}

	@Override
	public GameState offerTrade(int gameId, OfferTradeRequest request) {
		return returnGameState();
	}

	@Override
	public GameState acceptTrade(int gameId, AcceptTradeRequest request) {
		return returnGameState();
	}

	@Override
	public GameState maritimeTrade(int gameId, MaritimeTradeRequest request) {
		return returnGameState();
	}

	@Override
	public GameState discardCards(int gameId, DiscardRequest request) {
		return returnGameState();
	}

	@Override
	public User login(UserRequest request) {
		return new User("test","test");
	}

	@Override
	public User register(UserRequest request) {
		return new User("test","test");
	}

}
