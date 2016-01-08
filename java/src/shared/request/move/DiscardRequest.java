package shared.request.move;

import shared.model.*;
/**
 * DiscardCardsRequest object is used to encapsulate the data sent on a DiscardCards call to the server.
 *
 */
public class DiscardRequest extends MoveRequest {
	
	private ResourceList discardedCards;

	public ResourceList getDiscardCard() {
		return discardedCards;
	}
	public void setDiscardCard(ResourceList discardCard) {
		this.discardedCards = discardCard;
	}
	
	/**
	 * 
	 * @param playerIndex
	 * @param discardCard
	 * @pre 0 <= playerIndex <= 4
	 * @pre discardCards is not null
	 * @post playerIndext, discardCards are stored in this object
	 */
	public DiscardRequest(int playerIndex, ResourceList discardCard) {
		super("discardCards", playerIndex);
		this.discardedCards = discardCard;
	}
	
}
