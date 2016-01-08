package server.facade;

import java.util.List;
import java.util.logging.Logger;

import server.Server;
import server.commands.Command;
import server.commands.game.*;
import shared.model.GameState;
import shared.request.move.MoveRequest;

/**
 * This class is the go between for the server game handlers and the command classes.
 * The server handlers call this class and this class creates command classes, executes the methods, and then returns the info.
 */
public class GameFacade {

	private boolean validateGameId(int gameId) {
		return !(Server.state.getGame(gameId) == null);
	}
	
	/**
	 * Returns the current state of the game.
	 * @return
	 */
	public GameState model(int gameId) {
		Logger.getLogger("catanserver").finer("In game facade model");
		if(!validateGameId(gameId)) {
			Logger.getLogger("catanserver").finer("game id not valid, return null");
			return null;
		}
		Logger.getLogger("catanserver").finer("game id is good so return model");
		return Server.state.getGame(gameId);
	}
	
	/**
	 * Clears out the command history of the current game
	 * @return
	 */
	public GameState reset(int gameId) {
		Logger.getLogger("catanserver").finer("In game facade reset");
		
		if(!validateGameId(gameId)) {
			return null;
		}
		
		ResetCommand command = new ResetCommand(gameId);
		command.execute();
		return Server.state.getGame(gameId);
	}
	
	/**
	 * Executes the specified command list in the current game.
	 * @param commands the list of commands to execute
	 * @return
	 */
	public GameState commands(int gameId, List<Command> commands) {
		Logger.getLogger("catanserver").finer("In game facade commands (execute)");
		
		if(!validateGameId(gameId)) {
			return null;
		}
		
		CommandsCommand command = new CommandsCommand(gameId,commands);
		command.execute();
		return Server.state.getGame(gameId);
	}
	
	/**
	 * Returns a list of commands that have been executed in the current game.
	 * @return
	 */
	public List<Command> commands(int gameId) {
		Logger.getLogger("catanserver").finer("In game facade commands (get)");
		
		if(!validateGameId(gameId)) {
			return null;
		}

		return Server.state.getCommands().get(gameId);
	}
	
	//Don't need to do add ai or list ai this phase
}
