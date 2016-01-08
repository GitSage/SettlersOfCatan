package server.commands.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import server.Server;
import server.commands.Command;
import shared.request.game.ChangeLogLevelRequest;

/**
 * Executes a ChangeLogLevel request
 * Created by benja_000 on 3/13/2015.
 */
public class ChangeLogLevelCommand implements Command {
    private String logLevel;

    public ChangeLogLevelCommand(ChangeLogLevelRequest request){
        this.logLevel = request.getLogLevel();
    }

    /**
     * Executes a ChangeLogLevel request
     * @pre the caller specified a valid logging level, which may include: ALL, SEVERE, WARNING, INFO, CONFIG, FINE, FINER,
     *      FINEST
     * @post the server is using the specified logging level
     */
    public void execute(){
        Logger.getLogger("catanserver").fine("Executing ChangeLogLevelCommand");
    	Level logLevel;
    	switch(this.logLevel) {
    		case "ALL":
    			logLevel = Level.ALL;
    			break;
    		case "SEVERE":
    			logLevel = Level.SEVERE;
    			break;
    		case "WARNING":
    			logLevel = Level.WARNING;
    			break;
    		case "INFO":
    			logLevel = Level.INFO;
    			break;
    		case "CONFIG":
    			logLevel = Level.CONFIG;
    			break;
    		case "FINE":
    			logLevel = Level.FINE;
    			break;
    		case "FINER":
    			logLevel = Level.FINER;
    			break;
    		case "FINEST":
    			logLevel = Level.FINEST;
    			break;
    		default:
    			logLevel = Level.ALL;    				
    	}
    	Server.changeLogLevel(logLevel);
    	return;
    }
}
