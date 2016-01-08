package shared.request.move;

import shared.definitions.ResourceType;

/**
 * YearOfPlentyRequest object is used to encapsulate the data sent on a YearOfPlenty call to the server.
 *
 */
public class YearOfPlentyCardRequest extends MoveRequest{

	private ResourceType resource1;
	private ResourceType resource2;
	
	public ResourceType getResource1() {
		return resource1;
	}
	public void setResource1(ResourceType resource1) {
		this.resource1 = resource1;
	}
	
	public ResourceType getResource2() {
		return resource2;
	}
	public void setResource2(ResourceType resource2) {
		this.resource2 = resource2;
	}

	/**
	 * @param playerIndex
	 * @param resource1
	 * @param resource2
	 * @pre resource1, resource2 are in (wood,brick,ore,sheep,wheat)
	 * @pre 0<= playerIndex <= 4
	 * @post resource1, resource2, and playerIndex are stored in this object
	 */
	public YearOfPlentyCardRequest(int playerIndex, ResourceType resource1, ResourceType resource2) {
		super("Year_of_Plenty", playerIndex);
		this.resource1 = resource1;
		this.resource2 = resource2;
	}	
	
}
