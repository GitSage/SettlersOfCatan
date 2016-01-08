package shared.request.game;

/**
 * ChangeLogLevelRequest object is used to encapsulate the data sent on a ChangeLogLevel call to the server.
 *
 */
public class ChangeLogLevelRequest {

	private String logLevel;

	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * @param logLevel
	 * @pre logLevel is in (ALL,SEVERE,WARNING,INFO,CONFIG,FINE,FINER,FINEST,OFF)
	 * @post logLevel is stored in this object
	 */
	public ChangeLogLevelRequest(String logLevel) {
		super();
		this.logLevel = logLevel;
	}
	
}
