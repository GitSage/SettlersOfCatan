package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.model.GameMap;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by benja_000 on 3/18/2015.
 */
public class MovesCommand {

	protected MovesCommand(int gameId, int playerIndex){
		this.gameId = gameId;
		this.playerIndex = playerIndex;
		this.log = Logger.getLogger("catanserver");
	}

	protected int gameId;
	protected int playerIndex;
	protected transient Logger log;

	protected GameState getGame(){
		return Server.state.getGame(gameId);
	}

	protected GameState getGame(int gameId){
		return Server.state.getGame(gameId);
	}

	protected Player getPlayer(){
		return getGame(gameId).getPlayer(playerIndex);
	}

	protected Player getPlayer(int playerIndex, int gameId){
		return getGame(gameId).getPlayer(playerIndex);
	}

	protected GameMap getMap(){
		return getGame().getMap();
	}

	protected MessageLine getLogLine() {
		return new MessageLine();
	}

	protected void shizThatMakesOtherShizWork() {
		Server.state.saveCommand(gameId, (Command) this);
		getGame().setVersion(getGame().getVersion() + 1);
		getGame().addLog(getLogLine());
		checkForWinner();
	}

	private void checkForWinner() {
		List<Player> players = getGame().getPlayers();
		for(Player player : players) {
			if(player.getVictoryPoints() >= 10) {
				getGame().setWinner(player.getPlayerIndex());
			}
		}
		
	}
}
