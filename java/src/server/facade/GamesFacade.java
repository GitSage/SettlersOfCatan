package server.facade;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Logger;

import client.data.PlayerInfo;
import server.Server;
import server.commands.games.*;
import shared.model.Game;
import shared.request.game.*;

/**
 * This class is the go between for the server games handlers and the command classes.
 * The server handlers call this class and this class creates command classes, executes the methods, and then returns the info.
 */
public class GamesFacade {
	
	private boolean canJoinGame(int playerId, JoinGameRequest request) {
		
		Logger.getLogger("catanserver").fine("Checking if player can join the game");
		
		if(Server.state.getGame(request.getId()).getPlayerById(playerId) != null) {
			Logger.getLogger("catanserver").fine("Player is already in the game");
			return true;
		}
		
		Game game = Server.state.getTitles().get(request.getId());
		int numPlayers = 0;
		for(PlayerInfo player : game.getPlayers()) {
			if(player != null) {
				numPlayers++;
			}
		}
		Logger.getLogger("catanserver").fine("Number of players in the game: " + numPlayers);
		return numPlayers < 4;
	}
	
	private boolean validateGameId(int gameId) {
		return !(Server.state.getGame(gameId) == null);
	}
	
	private boolean checkNameExists(String name) {
		Collection<Game> gameTitles = Server.state.getTitles().values();
		for(Game title : gameTitles){
			if(title.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets a list of all games in progress.
	 * @return
	 */
	public List<Game> list() {
		Logger.getLogger("catanserver").finer("In games facade list method");
		return new ArrayList<>(Server.state.getTitles().values());
	}
	
	/**
	 * Creates a new game.
	 * @param request the object containing the request data
	 * @return true on success, false on failure (ie bad game name)
	 */
	public Game create(CreateGameRequest request) {
		Logger.getLogger("catanserver").finer("In games facade create method");
		String validName = "^[a-zA-Z0-9_\\-]+$";
		if(!request.getName().matches(validName)) {
			Logger.getLogger("catanserver").fine("In games facade create but returning null because the name is not valid");
			return null;
		}
		
		if(checkNameExists(request.getName())) {
			return null;
		}
		
		CreateCommand command = new CreateCommand(request);
		command.execute();
		return Server.state.getGameObject(Server.state.getTitles().size() -1);
	}

	/**
	 * Adds (or re-adds) the player to the specified game, and sets their catan.game HTTP cookie
	 * @param request the object containing the request data
	 * @return true on success, false on failure
	 */
	public boolean join(int playerId, JoinGameRequest request) {
		Logger.getLogger("catanserver").finer("In games facade join");
		if(!validateGameId(request.getId())) {
			return false;
		}
		
		if(!canJoinGame(playerId, request)) {
			return false;
		}
		
		JoinCommand command = new JoinCommand(playerId, request);
		command.execute();
		return true;
	}

	/**
	 * Saves the current state of the specified game to a file.
	 * @param request the object containing the request data
	 * @return true on success, false on failure
	 */
	public boolean save(SaveGameRequest request) {
		Logger.getLogger("catanserver").finer("In games facade save");
		if(!validateGameId(request.getId())) {
			return false;
		}
		
		SaveCommand command = new SaveCommand(request);
		command.execute();
		return true;
	}
	
	/**
	 * Loads a previously saved game file to restore the state of a game.
	 * @param request the object containing the request data
	 * @return true on success, false on failure
	 */
	public boolean load(LoadGameRequest request) {
		Logger.getLogger("catanserver").finer("In games facade load");		
		
		try{
			Scanner scan = new Scanner(new File("saves"+File.separator+request.getName()));
		} catch(FileNotFoundException e){
			return false;
		}
		
		LoadCommand command = new LoadCommand(request);
		command.execute();
		return true;
	}
}
