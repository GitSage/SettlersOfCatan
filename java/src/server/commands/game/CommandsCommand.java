package server.commands.game;

import java.util.List;

import server.commands.Command;
import shared.request.move.MoveRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Gets a list of commands that have been executed in the current game
 * Created by benja_000 on 3/13/2015.
 */
public class CommandsCommand implements Command{

    private int gameID;
    private List<Command> commands;

    private void executePostCommands() {
		// TODO Auto-generated method stub
		
	}

	private void executeGetCommands() {
		// TODO Auto-generated method stub
		
	}
    
    public CommandsCommand(int gameID, List<Command> commands){
        this.gameID = gameID;
        this.commands = commands;
    }


    /**
     * Gets a list of commands that have been executed in the current game
     * @pre the caller has previously logged in to the server and joined a game (they have valid catan.user and
     *      catan.game cookies)
     * @post the server returns the data with a successful response
     */
    public void execute(){
  
    	if(commands == null) {
    		executeGetCommands();
    	}
    	else {
    		executePostCommands();
    	}
    	
        throw new NotImplementedException();
    }

}
