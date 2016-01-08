package shared.request.move;

/**
 * Parent class to all 'move' classes - those that encapsulate the data for different player moves (build, buy cards, etc.).
 *
 */
public class MoveRequest {

	private String type;
	private int playerIndex;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public int getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}
	
	/**
	 * 
	 * @param type
	 * @param playerIndex
	 * @pre type is in (Year_of_Plenty,Soldier,sendChat,rollNumber,robPlayer,
	 * 		Road_Build,offerTrade,Monument,Monopoly,finishTurn,discardCards,buyDevCard,
	 * 		buildSettlement,buildRoad,buildCity,maritimeTrade,acceptTrade)
	 * @pre 0 <= playerIndex <= 4
	 * @post type and playerIndex are stored in this object
	 */
	public MoveRequest(String type, int playerIndex) {
		super();
		this.type = type;
		if(playerIndex > 4) {
			this.playerIndex = -1;
		}
		else {
			this.playerIndex = playerIndex;
		}
	}
}
