package server.commands.game;

import server.commands.Command;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Executes a Model command
 * Created by benja_000 on 3/13/2015.
 */
public class ModelCommand implements Command{

    private int playerID;
    private int gameID;
    private int versionNumber;

    public ModelCommand(int playerID, int gameID, int versionNumber) {
        this.playerID = playerID;
        this.gameID = gameID;
        this.versionNumber = versionNumber;
    }

    /**
     * Returns the current state of the game in JSON format.
     * @pre the caller has previously logged in to the server and joined a game (they have valid cookies)
     * @pre if specified, the version number is included
     */
    public void execute(){
        throw new NotImplementedException();
    }
}
