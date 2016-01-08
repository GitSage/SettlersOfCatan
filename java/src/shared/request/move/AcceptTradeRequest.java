package shared.request.move;

/**
 * AcceptTradeRequest object is used to encapsulate the data sent on a AcceptTrade call to the server.
 *
 */
public class AcceptTradeRequest extends MoveRequest{

	private boolean willAccept;

	public boolean isWillAccept() {
		return willAccept;
	}
	public void setWillAccept(boolean willAccept) {
		this.willAccept = willAccept;
	}

	/**
	 * @param playerIndex
	 * @param willAccept
	 * @pre 0=< playerIndex <=4
	 * @pre willAccept is not null
	 * @post playerIndex and willAccept are stored in this object
	 */
	public AcceptTradeRequest(int playerIndex, boolean willAccept) {
		super("acceptTrade", playerIndex);
		this.willAccept = willAccept;
	}
	
}
