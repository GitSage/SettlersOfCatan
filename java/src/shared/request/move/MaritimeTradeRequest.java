package shared.request.move;

import shared.definitions.ResourceType;

/**
 * MaritimeTradeRequest object is used to encapsulate the data sent on a MaritimeTrade call to the server.
 *
 */
public class MaritimeTradeRequest extends MoveRequest {

	private int ratio;
	private ResourceType inputResource;
	private ResourceType outputResource;
	
	public int getRatio() {
		return ratio;
	}
	public void setRatio(int ratio) {
		this.ratio = ratio;
	}
	
	public ResourceType getInputResource() {
		return inputResource;
	}
	public void setInputResource(ResourceType inputResource) {
		this.inputResource = inputResource;
	}
	
	public ResourceType getOutputResource() {
		return outputResource;
	}
	public void setOutputResource(ResourceType outputResource) {
		this.outputResource = outputResource;
	}
	
	/**
	 * 
	 * @param playerIndex
	 * @param ratio
	 * @param inputResource
	 * @param outputResource
	 * @pre 0 <= playerIndex <= 4
	 * @pre ratio > 0
	 * @pre inputResource is in (wood,brick,ore,sheep,wheat)
	 * @pre outputResource is in (wood,brick,ore,sheep,wheat)
	 * @post playerIndex, ratio, inputResource, outputResource are stored in this object
	 */
	public MaritimeTradeRequest(int playerIndex, int ratio, ResourceType inputResource, ResourceType outputResource) {
		super("maritimeTrade", playerIndex);
		this.ratio = ratio;
		this.inputResource = inputResource;
		this.outputResource = outputResource;
	}
	
}
