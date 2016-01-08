package shared.model;

import server.Server;
import shared.definitions.TurnStatus;

/**
 * A model class for a turn tracker
 */
public class TurnTracker{
	private int currentTurn;
	private TurnStatus status;
	private int longestRoad;
	private int largestArmy;

	public TurnTracker() {
		reset();
	}

	/**
	 * Resets turn tracker
	 */
	public void reset() {
		currentTurn = 0;
		status = TurnStatus.FIRSTROUND;
		longestRoad = -1;
		largestArmy = -1;
	}


	/**
	 * Gets the current turn.
	 * @return int turn
	 */
	public int getCurrentTurn() {
		return currentTurn;
	}

	/**
	 * Sets the current turn.
	 * @param currentTurn int
	 */
	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	/**
	 * Gets the current status.
	 * The potential statuses are:
	 * ROLLING, ROBBING, PLAYING, DISCARDING, FIRSTROUND, SECONDROUND
	 * @return String status
	 */
	public TurnStatus getStatus() {
		return status;
	}

	/**
	 * Sets the current status.
	 * @pre The potential statuses are:
	 *	  ROLLING, ROBBING, PLAYING, DISCARDING, FIRSTROUND, SECONDROUND
	 * @post the status has been set to the appropriate enum.
	 * @param status TurnStatus
	 */
	public void setStatus(TurnStatus status) {
		this.status = status;
	}

	/**
	 * Gets the current longest road
	 * @return int longest road
	 */
	public int getLongestRoad() {
		return longestRoad;
	}

	/**
	 * Sets the current longest road
	 * @param longestRoad int longest road
	 */
	public void setLongestRoad(int longestRoad) {
		this.longestRoad = longestRoad;
	}

	/**
	 * Gets the largest army
	 * @return the largest army
	 */
	public int getLargestArmy() {
		return largestArmy;
	}

	/**
	 * Sets the largest army
	 * @param largestArmy int largest army
	 */
	public void setLargestArmy(int largestArmy) {
		this.largestArmy = largestArmy;
	}

	/**
	 * Deterimines if it is currently a certain player's turn.
	 * @pre player is a player index from 0-3
	 * @post returns true if it is currently the player's turn, false otherwise.
	 * @param player
	 * @return boolean
	 */
	public boolean isPlayerTurn(int player){
		return player == getCurrentTurn();
	}

	public void updateLongestRoad(int gameId){
		int leastRemainingRoads = 11;	//Because we track game piece left they can play, not roads they have played
		Player prevPlayer;
		if(longestRoad != -1){
			prevPlayer = Server.state.getGame(gameId).getPlayer(longestRoad);
			leastRemainingRoads = prevPlayer.getRoads();
			prevPlayer.setVictoryPoints(prevPlayer.getVictoryPoints() - 2);
		}
		
		for(Player p : Server.state.getGame(gameId).getPlayers()){
			if(p.getRoads() < leastRemainingRoads){
				leastRemainingRoads = p.getRoads();
				setLongestRoad(p.getPlayerIndex());
			}
		}
		
		if(longestRoad != -1){
			Player newPlayer = Server.state.getGame(gameId).getPlayer(longestRoad);
			newPlayer.setVictoryPoints(newPlayer.getVictoryPoints() + 2);
		}
	}

	public void updateLargestArmy(int gameId){
		int mostSoldiers = 2;
		Player prevPlayer;
		if(largestArmy != -1) {
			prevPlayer = Server.state.getGame(gameId).getPlayer(largestArmy); 
			mostSoldiers = prevPlayer.getSoldiers();
			prevPlayer.setVictoryPoints(prevPlayer.getVictoryPoints() - 2);
		}
		
		for(Player p : Server.state.getGame(gameId).getPlayers()){
			if(p.getSoldiers() > mostSoldiers){
				setLargestArmy(p.getPlayerIndex());
				mostSoldiers = p.getSoldiers();
			}
		}
		
		if(largestArmy != -1) {
			Player newPlayer = Server.state.getGame(gameId).getPlayer(largestArmy);
			newPlayer.setVictoryPoints(newPlayer.getVictoryPoints() + 2);
		}
	}
}
