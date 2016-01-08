package client.data;

import shared.definitions.CatanColor;
import shared.model.GameState;

/**
 * Used to pass player information into the rob view<br>
 * <br>
 * PROPERTIES:<br>
 * <ul>
 * <li>Id: Unique player ID</li>
 * <li>PlayerIndex: Player's order in the game [0-3]</li>
 * <li>Name: Player's name (non-empty string)</li>
 * <li>Color: Player's color (cannot be null)</li>
 * <li>NumCards: Number of development cards the player has (>= 0)</li>
 * </ul>
 * 
 */
public class RobPlayerInfo extends PlayerInfo
{
	
	private int numCards;

	public RobPlayerInfo()
	{
		super();
	}

	public RobPlayerInfo(String name, CatanColor color, int numCards) {
		this.setName(name);
		this.setColor(color);
		this.setNumCards(numCards);
	}

    public RobPlayerInfo(PlayerInfo info){
        this.setColor(info.getColor());
        this.setId(info.getId());
        this.setName(info.getName());
        this.setPlayerIndex(info.getPlayerIndex());
        this.setNumCards(GameState.get().getPlayer(this.getPlayerIndex()).getTotalNumCards());
    }

	public int getNumCards()
	{
		return numCards;
	}
	public void setNumCards(int numCards)
	{
		this.numCards = numCards;
	}
	
}

