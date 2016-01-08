package shared.model;

import client.data.PlayerInfo;
import client.proxy.ProxyFacade;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.definitions.TurnStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * Model class for a player
 */
public class Player{
	private int cities;
	private CatanColor color;
	private boolean discarded;
	private int monuments;
	private String name;
	private DevCardList newDevCards;
	private DevCardList oldDevCards;
	private int playerIndex;
	private boolean playedDevCard;
	private int playerID;
	private ResourceList resources;
	private int roads;
	private int settlements;
	private int soldiers;
	private int victoryPoints;

	/**
	 * Sums and returns the total number of cards the player has.
	 * @pre none
	 * @post the total number of cards the player possesses is returned
	 * @return
	 */
	public int getTotalNumCards(){
		int total = newDevCards.getMonopoly();
		total += newDevCards.getMonument();
		total += newDevCards.getRoadBuilding();
		total += newDevCards.getSoldier();
		total += newDevCards.getYearOfPlenty();

		total += resources.getBrick();
		total += resources.getOre();
		total += resources.getSheep();
		total += resources.getWheat();
		total += resources.getWood();


		return total;
	}

	/**
	 * Converts the player to a playerinfo as the method name suggests. So this is redundant
	 * @return PlayerInfo
	 */
	public PlayerInfo toPlayerInfo() {
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setColor(color);
		playerInfo.setId(playerID);
		playerInfo.setName(name);
		playerInfo.setPlayerIndex(playerIndex);
		return playerInfo;
	}

	/**
	 * Gets the cities
	 * @return int cities
	 */
	public int getCities() {
		return cities;
	}

	/**
	 * Sets the cities
	 * @param cities int
	 */
	public void setCities(int cities) {
		this.cities = cities;
	}

	/**
	 * Gets the player's color
	 * @return the player's color String
	 */
	public CatanColor getColor() {
		return color;
	}

	/**
	 * Sets the player's color
	 * @param color the player's color String
	 */
	public void setColor(CatanColor color) {
		this.color = color;
	}

	/**
	 * Whether discarded or not
	 * @return boolean discarded or not
	 */
	public boolean isDiscarded() {
		return discarded;
	}

	/**
	 * Sets the player's discarded status
	 * @param discarded boolean
	 */
	public void setDiscarded(boolean discarded) {
		this.discarded = discarded;
	}

	/**
	 * Gets the monuments
	 * @return int monuments
	 */
	public int getMonuments() {
		return monuments;
	}

	/**
	 * Sets the monuments
	 * @param monuments int
	 */
	public void setMonuments(int monuments) {
		this.monuments = monuments;
	}

	/**
	 * Gets the player's name
	 * @return String the player's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the player's name
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the newDevCards
	 * @return DevCardList
	 */
	public DevCardList getNewDevCards() {
		return newDevCards;
	}

	/**
	 * Sets the newDevCards
	 * @param newDevCards DevCardList
	 */
	public void setNewDevCards(DevCardList newDevCards) {
		this.newDevCards = newDevCards;
	}

	/**
	 * Gets the oldDevCards
	 * @return DevCardList
	 */
	public DevCardList getOldDevCards() {
		return oldDevCards;
	}

	/**
	 * Sets the oldDevCards
	 * @param oldDevCards DevCardList
	 */
	public void setOldDevCards(DevCardList oldDevCards) {
		this.oldDevCards = oldDevCards;
	}

	/**
	 * Gets the player index
	 * @return int
	 */
	public int getPlayerIndex() {
		return playerIndex;
	}

	/**
	 * Sets the player index
	 * @param playerIndex int
	 */
	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	/**
	 * Gets if the player has played a dev card.
	 * @pre none
	 * @post returns true if the player has played a dev card this turn, false otherwise.
	 * @return boolean
	 */
	public boolean hasPlayedDevCard() {
		return playedDevCard;
	}

	/**
	 * Sets if the dev card has been played
	 * @param playedDevCard boolean
	 */
	public void setPlayedDevCard(boolean playedDevCard) {
		this.playedDevCard = playedDevCard;
	}

	/**
	 * Gets the player ID
	 * @return int
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Sets the player ID
	 * @param playerID int
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * Gets the resource list
	 * @return ResourceList
	 */
	public ResourceList getResources() {
		return resources;
	}

    /**
     * Gets a random resource that this player owns. Only returns non-zero resources.
     * Returns null if the player does not have any resources.
     * @return ResourceType
     */
    public ResourceType getRandomResource(){
        List<ResourceType> resources = new ArrayList<>();
        if(getResources().getWheat() != 0){
            resources.add(ResourceType.WHEAT);
        }
        if(getResources().getBrick() != 0){
            resources.add(ResourceType.BRICK);
        }
        if(getResources().getWood() != 0){
            resources.add(ResourceType.WOOD);
        }
        if(getResources().getOre() != 0){
            resources.add(ResourceType.ORE);
        }
        if(getResources().getSheep() != 0){
            resources.add(ResourceType.SHEEP
            );
        }
        
        if(resources.size() == 0) {
            return null;
        }
        else{
            int rand = (int)(Math.random()*resources.size());
            return resources.get(rand);
        }
    }

    /**
     * Robs the specified player. Takes one random resource from them.
     * @param victim
     */
    public void robPlayer(Player victim){
    	if(victim == null) {
    		return;
    	}
        ResourceType toSwap = victim.getRandomResource();
        if(toSwap == null) {
        	return;
        }
        victim.getResources().setResource(toSwap, victim.getResources().getResource(toSwap)-1);
        this.getResources().setResource(toSwap, this.getResources().getResource(toSwap)+1);
    }

	/**
	 * Sets the resource list
	 * @param resources ResourceList
	 */
	public void setResources(ResourceList resources) {
		this.resources = resources;
	}

	/**
	 * Gets the roads
	 * @return int
	 */
	public int getRoads() {
		return roads;
	}

	/**
	 * Sets the roads
	 * @param roads int
	 */
	public void setRoads(int roads) {
		this.roads = roads;
	}

	/**
	 * Gets the settlements
	 * @return the settlements
	 */
	public int getSettlements() {
		return settlements;
	}

	/**
	 * Sets the settlements
	 * @param settlements the settlements
	 */
	public void setSettlements(int settlements) {
		this.settlements = settlements;
	}

	/**
	 * Gets the number of soldiers
	 * @return the number of soldiers
	 */
	public int getSoldiers() {
		return soldiers;
	}

	/**
	 * Sets the number of soldiers
	 * @param soldiers int
	 */
	public void setSoldiers(int soldiers) {
		this.soldiers = soldiers;
	}

	/**
	 * Gets the number of victory points
	 * @return int
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}

	/**
	 * Sets the number of victory points
	 * @param victoryPoints int
	 */
	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}


	/********************************************/
	/******Player can dos************************/
	/********************************************/

	/**
	 * Checks resources
	 * Build conditions:
	 * 1 road
	 * 1 brick
	 * 1 wood
	 * @pre none
	 * @post Returns a true if can else false
	 */
	public boolean canBuildRoad() {
		if(roads >= 1 && resources.getBrick() >= 1 && resources.getWood() >= 1 && GameState.get().isTurnAndStatus(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), TurnStatus.PLAYING)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks resources
	 * Build Conditions:
	 * 1 Settlement
	 * 1 Brick
	 * 1 Wood
	 * 1 Wheat
	 * 1 Sheep
	 * @pre none
	 * @post returns true if can else false
	 */
	public boolean canBuildSettlement() {
		if(settlements >= 1 && resources.getBrick() >= 1 && resources.getWood() >= 1 &&
						resources.getWheat() >= 1 && resources.getSheep() >= 1
						&& GameState.get().isTurnAndStatus(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), TurnStatus.PLAYING)) {
			return true;
		}
		return false;
	}
	/**
	 * Checks resources
	 * Build Conditions:
	 * 1 City
	 * 2 Wheat
	 * 3 Ore
	 * @pre none
	 * @post returns true if can else false
	 */
	public boolean canBuildCity() {
		if(cities >= 1 && resources.getWheat() >= 2 && resources.getOre() >= 3
						&& GameState.get().isTurnAndStatus(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), TurnStatus.PLAYING)) {
			return true;
		}
		return false;
	}
	/**
	 * Checks resources
	 * Build conditions:
	 * 1 Sheep
	 * 1 Wheat
	 * 1 Ore
	 * @pre none
	 * @post returns true if can else false
	 */
	public boolean canBuyDevCard(){
		if(resources.getSheep() >= 1 && resources.getWheat() >= 1 && resources.getOre() >= 1
						&& GameState.get().isTurnAndStatus(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), TurnStatus.PLAYING) && ! GameState.get().getDeck().isEmpty()){
			return true;
		}
		return false;
	}

	/**
	 *
	 * Conditions:
	 * 1 devcard
	 * @post returns true if can else false
	 */
	public boolean canPlayDevCard() {
		if (!oldDevCards.isEmpty() && GameState.get().isTurnAndStatus(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), TurnStatus.PLAYING)) {
			return true;
		}
		return false;
	}

	public void reset() {
		setResources(new ResourceList(0,0,0,0,0));
		setRoads(15);
		setSettlements(5);
		setCities(4);
		setSoldiers(0);
		setVictoryPoints(0);
		discarded = false;
		monuments = 0;
		newDevCards = new DevCardList(0,0,0,0,0);
		oldDevCards = new DevCardList(0,0,0,0,0);
		
	}

}