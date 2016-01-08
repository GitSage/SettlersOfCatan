package shared.request.move;

/**
 * SendChatRequest object is used to encapsulate the data sent on a SendChat call to the server.
 *
 */
public class SendChatRequest extends MoveRequest {

	private String content;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 
	 * @param playerIndex
	 * @param content
	 * @pre 0 <= playerIndex <= 4
	 * @post playerIndex and content are stored in this object
	 */
	public SendChatRequest(int playerIndex, String content) {
		super("sendChat", playerIndex);
		this.content = content;
	}
	
}
