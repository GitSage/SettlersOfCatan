package shared.model;

/**
 * A model class. Contains the message and source for a MessageLine.
 */
public class MessageLine{
	private String message;
	private String source;
		private boolean ignoreMe = false;

	/**
	 * @return the message as a String.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message String. Sets the message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the source as a String.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source String. Sets the source.
	 */
	public void setSource(String source) {
		this.source = source;
	}

	public void setIgnoreMe(boolean ignore) {
		ignoreMe = ignore;
	}

	public boolean getIgnoreMe() {
		return ignoreMe;
	}
}