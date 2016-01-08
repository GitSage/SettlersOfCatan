package shared.request.move;

/**
 * FinishTurnRequest object is used to encapsulate the data sent on a FinishTurn call to the server.
 *
 */
public class FinishTurnRequest extends MoveRequest {

	/**
	 * 
	 * @param playerIndex
	 * @pre 0 <= playerIndext <= 4
	 * @post playerIndex is stored in this object
	 */
	public FinishTurnRequest(int playerIndex) {
		super("finishTurn", playerIndex);
	}
	
}
