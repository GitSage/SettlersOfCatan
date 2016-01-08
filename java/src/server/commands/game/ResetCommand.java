package server.commands.game;

import server.Server;
import server.commands.Command;

/**
 * Executes a Reset request
 * Created by benja_000 on 3/13/2015.
 */
public class ResetCommand implements Command {

	private int gameID;

	public ResetCommand(int gameID) {
		this.gameID = gameID;
	}

	/**
	 * Clears out the command history of the current game.
	 * @pre the user is logged in (has valid cookies)
	 * @post the game's command history has been cleared out
	 * @post the game's players have NOT been cleared out
	 */
	public void execute(){
		Server.state.getGame(gameID).reset();
	}
}
