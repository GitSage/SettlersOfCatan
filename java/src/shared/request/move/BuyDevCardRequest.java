package shared.request.move;

/**
 * BuyDevCardRequest object is used to encapsulate the data sent on a BuyDevCard call to the server.
 *
 */
public class BuyDevCardRequest extends MoveRequest {

	/**
	 * 
	 * @param playerIndex
	 * @pre 0 <= playerIndex <= 4
	 * @post playerIndex is stored in this object
	 */
	public BuyDevCardRequest(int playerIndex) {
		super("buyDevCard", playerIndex);
	}

}
