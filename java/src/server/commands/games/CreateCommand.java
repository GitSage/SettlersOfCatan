package server.commands.games;

import client.data.PlayerInfo;
import server.Server;
import server.commands.Command;
import shared.model.Game;
import shared.model.GameState;
import shared.model.Player;
import shared.request.game.CreateGameRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Executes a Create request.
 * Created by benja_000 on 3/13/2015.
 */
public class CreateCommand implements Command {

    private boolean randomTiles;
    private boolean randomNumbers;
    private boolean randomPorts;
    private String name;

    public CreateCommand(CreateGameRequest request){
        this.randomTiles = request.isRandomTiles();
        this.randomNumbers = request.isRandomNumbers();
        this.randomPorts = request.isRandomPorts();
        this.name = request.getName();
    }


    /**
     * Executes a Create request
     * @pre name != null
     * @pre randomTiles, randomNumbers, and randomPorts contain valid boolean values
     * @post a new game with the specified properties has been created
     */
    public void execute(){
        Logger.getLogger("catanserver").fine("Executing CreateCommand");
        int newGameId = getNewGameId();
        GameState newGame = new GameState(randomTiles, randomNumbers, randomPorts);
        Server.state.getGames().put(newGameId, newGame);
	    Game gameTitle = new Game();
	    gameTitle.setId(newGameId);
	    gameTitle.setTitle(name);
	    List<PlayerInfo> playerList = new ArrayList<PlayerInfo>();
	    gameTitle.setPlayers(playerList);
	    Server.state.getTitles().put(newGameId, gameTitle);
	    List<Command> gameCommands = new ArrayList<>();
	    Server.state.getCommands().put(newGameId, gameCommands);
    }

    /**
     * Gets a new gameId. Uses the lowest int not currently in use (starting at 0).
     * @return int
     */
    private int getNewGameId(){
        Set<Integer> existingIds = Server.state.getGames().keySet();
        int newId = 0;
        while(existingIds.contains(newId)){
            newId++;
        }
        return newId;
    }
}
