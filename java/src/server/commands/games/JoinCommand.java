package server.commands.games;

import client.data.PlayerInfo;
import server.Server;
import server.commands.Command;
import shared.definitions.CatanColor;
import shared.model.DevCardList;
import shared.model.Game;
import shared.model.Player;
import shared.model.ResourceList;
import shared.request.game.JoinGameRequest;

import java.util.List;

/**
 * Executes a Join request.
 * Created by benja_000 on 3/13/2015.
 */
public class JoinCommand implements Command {

	private int playerId;
	private int gameID;
	private CatanColor color;

	public JoinCommand(int playerId, JoinGameRequest request){
		this.gameID = request.getId();
		this.color = request.getColor();
		this.playerId = playerId;
	}

	/**
	 * Executes a Join request
	 * @pre the user is already logged in (has cookies)
	 * @pre the player is already in the game OR there is space in the game for an additional player
	 * @pre the gameID is valid
	 * @pre the color is valid (red, green, blue, yellow, puce, brown, white, purple, orange)
	 * @post the player is in the game
	 */
	public void execute(){

		Player player = new Player();

		player.setCities(4);
		player.setSettlements(5);
		player.setRoads(15);
		player.setColor(color);
		player.setMonuments(0);
		player.setName(Server.state.getUsers().get(playerId).getUsername());
		player.setDiscarded(false);
		player.setNewDevCards(new DevCardList());
		player.setOldDevCards(new DevCardList());
		player.setPlayedDevCard(false);
		player.setPlayerID(playerId);

		Game game = Server.state.getTitles().get(gameID);
		int playerIndex = 0;
		for(PlayerInfo temp : game.getPlayers()) {
			if(temp != null) {
				if(temp.getId() == playerId) {
					System.out.println("Finished Player already in game so just updating color then returning");
					Server.state.getGame(gameID).getPlayerById(playerId).setColor(color);
					temp.setColor(color);
					return;
				}
				playerIndex++;
			}
		}

		player.setPlayerIndex(playerIndex);
		player.setResources(new ResourceList(0,0,0,0,0));
		player.setSoldiers(0);
		player.setVictoryPoints(0);

		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setColor(color);
		playerInfo.setId(playerId);
		playerInfo.setName(Server.state.getUsers().get(playerId).getUsername());
		playerInfo.setPlayerIndex(playerIndex);

		List<Player> gameStatePlayers = Server.state.getGame(gameID).getPlayers();
		List<PlayerInfo> gameTitlePlayers = Server.state.getTitles().get(gameID).getPlayers();
		gameStatePlayers.add(playerIndex, player);
		gameTitlePlayers.add(playerIndex, playerInfo);
		System.out.println("Finished JoinCommand.execute()");

	}
}
