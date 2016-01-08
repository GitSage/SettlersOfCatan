package shared.request.move;

/**
 * MonumentRequest object is used to encapsulate the data sent on a Monument call to the server.
 *
 */
public class MonumentCardRequest extends MoveRequest {

	/**
	 * 
	 * @param playerIndex
	 * @pre 0 <= playerIndex <= 4
	 */
	public MonumentCardRequest(int playerIndex) {
		super("Monument", playerIndex);
	}
}
