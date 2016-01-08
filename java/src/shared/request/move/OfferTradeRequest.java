package shared.request.move;

import shared.model.*;

/**
 * OfferTradeRequest object is used to encapsulate the data sent on a OfferTrade call to the server.
 *
 */
public class OfferTradeRequest extends MoveRequest {

	private ResourceList offer;
	private int receiver;
	
	public ResourceList getOffer() {
		return offer;
	}
	public void setOffer(ResourceList offer) {
		this.offer = offer;
	}
	
	public int getReceiver() {
		return receiver;
	}
	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}

	/**
	 * 
	 * @param playerIndex
	 * @param offer
	 * @param receiver
	 * @pre 0 <= playerIndex <= 4
	 * @pre offer is not null
	 * @pre 0 <= receiver <= 4
	 * @post playerIndex, offer, receiver are stored in this object
	 */
	public OfferTradeRequest(int playerIndex, ResourceList offer, int receiver) {
		super("offerTrade", playerIndex);
		this.offer = offer;
		if(receiver > 4) {
			this.receiver = -1;
		}
		else {
			this.receiver = receiver;
		}
	}
	
	
}
