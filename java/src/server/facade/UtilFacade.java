package server.facade;

import java.util.logging.Logger;

import shared.request.game.ChangeLogLevelRequest;

/**
 * This class is the go between for the server util handlers and the command classes.
 * The server handlers call this class and this class creates command classes, executes the methods, and then returns the info.
 */
public class UtilFacade {

	private boolean validateLogLevel(String level) {
		Logger.getLogger("catanserver").finest("In Util Facade validate log level method");
		return level.equals("ALL") || level.equals("SEVERE") || level.equals("WARNING") || 
				level.equals("INFO") || level.equals("CONFIG") || level.equals("FINE") || 
				level.equals("FINER") || level.equals("FINEST") || level.equals("OFF");
	}
	
	public UtilFacade() {
		super();
		Logger.getLogger("catanserver").finest("Util Facade Created");
	}

	/**
	 * Sets the server's log level (ALL, SEVERE, WARNING ,INFO, CONFIG, FINE, FINER, FINEST, OFF)
	 * @param request the object containing the request data
	 * @return
	 */
	public boolean changeLogLevel(ChangeLogLevelRequest request) {
		Logger.getLogger("catanserver").finer("In Util Facade change log level");
		return validateLogLevel(request.getLogLevel());
	}
}
