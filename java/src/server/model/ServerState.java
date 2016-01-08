package server.model;

import server.commands.Command;
import shared.model.Game;
import shared.model.GameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ServerState {
	private Map<Integer, List<Command>> commands;
	private Map<Integer, GameState> games;
	private Map<Integer,Game> gameTitles;
	private List<User> users;

	public void saveCommand(int gameId, Command savedCommand) {
	    List<Command> setCommands = commands.get(gameId);
	    if (setCommands == null) {
	        setCommands = new ArrayList<>();
	        commands.put(gameId, setCommands);
	    }
	    setCommands.add(savedCommand);
	}
	
	public ServerState() {
		games = new HashMap<>();
		gameTitles = new HashMap<>();
		users = new ArrayList<>();
		commands = new HashMap<>();
	}

	public int getIdFromTitle(String title) {
		for (Map.Entry<Integer, Game> game : gameTitles.entrySet()) {
			if(game.getValue().getTitle().equals(title)) {
				return game.getKey();
			}
		}
		return -1;
	}

	public Game getGameObject(int id) {
		return gameTitles.get(id);
	}

	public boolean authenticate(User user) {
		Logger.getLogger("catanserver").finest("authenticate called");
		for (User validUser : this.users) {
			if (validUser.equals(user)) {
				Logger.getLogger("catanserver").finest("authenticate returning true");
				return true;
			}
		}
		Logger.getLogger("catanserver").finest("authenticate returning false");
		return false;
	}

	public boolean authenticate(User user, int gameId) {
		return authenticate(user) && userInGame(user, gameId);
	}

	public boolean userInGame(User user, int gameId) {
		GameState gameState = games.get(gameId);
		if (gameState == null) {
			return false;
		} else {
			if (gameState.getPlayerById(user.getUserId()) == null) {
				return false;
			} else {
				return true;
			}
		}
	}

	public Map<Integer,Game> getTitles() {
		return gameTitles;
	}
	
	public String getTitle(int id) {
		return gameTitles.get(id).getTitle();
	}
	
    public GameState getGame(int id) {
        return games.get(id);
    }

	public Map<Integer, GameState> getGames() {
		return games;
	}

	public void setGames(Map<Integer, GameState> games) {
		this.games = games;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Map<Integer, List<Command>> getCommands() {
		return commands;
	}

	public void setCommands(Map<Integer, List<Command>> commands) {
		this.commands = commands;
	}
}
