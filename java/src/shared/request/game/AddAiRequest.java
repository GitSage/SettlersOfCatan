package shared.request.game;

/**
 * AddAiRequest object is used to encapsulate the data sent on a AddAi call to the server.
 *
 */
public class AddAiRequest {

	private String AIType;

	public String getAiType() {
		return AIType;
	}
	public void setAiType(String aiType) {
		this.AIType = aiType;
	}
	
	/**
	 * @param aiType
	 * @pre aiType is in (LARGEST_ARMY)
	 * @post aiType is stored in this object
	 */
	public AddAiRequest(String aiType) {
		super();
		this.AIType = aiType;
	}
	
}
