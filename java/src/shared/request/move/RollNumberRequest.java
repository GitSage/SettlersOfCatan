package shared.request.move;

/**
 * RollNumberRequest object is used to encapsulate the data sent on a RollNumber call to the server.
 *
 */
public class RollNumberRequest extends MoveRequest {

	private int number;

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * 
	 * @param playerIndex
	 * @param number
	 * @pre 0 <= playerIndex <= 4
	 * @pre 1 <= number <= 12
	 * @post playerIndex and number are stored in this object
	 */
	public RollNumberRequest(int playerIndex, int number) {
		super("rollNumber", playerIndex);
		if(number > 12) {
			this.number = -1;
		}
		else {
			this.number = number;
		}
	}
}
