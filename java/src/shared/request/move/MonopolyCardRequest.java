package shared.request.move;

import shared.definitions.ResourceType;

/**
 * MonopolyRequest object is used to encapsulate the data sent on a Monopoly call to the server.
 *
 */
public class MonopolyCardRequest extends MoveRequest {

	private ResourceType resource;

	public ResourceType getResource() {
		return resource;
	}
	public void setResource(ResourceType resource) {
		this.resource = resource;
	}
	
	/**
	 * 
	 * @param playerIndex
	 * @param resource
	 * @pre 0 <= playerIndex <= 4
	 * @pre resource is in (wood,brick,ore,sheep,wheat)
	 * @post playerIndex, resource are stored in this object
	 */
	public MonopolyCardRequest(int playerIndex, ResourceType resource) {
		super("Monopoly", playerIndex);
		this.resource = resource;
	}
	
}
