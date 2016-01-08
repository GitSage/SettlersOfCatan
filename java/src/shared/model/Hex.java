package shared.model;

import shared.definitions.HexType;
import shared.definitions.ResourceType;
import shared.locations.HexLocation;

/**
 * A model class for a Hex
 */
public class Hex {
    private HexLocation location;
    private HexType type;
    private int number;

    /**
     * Gets the location
     * @return the HexLocation
     */
    public HexLocation getLocation() {
        return location;
    }

    /**
     * Sets the location
     * @param location a HexLocation
     */
    public void setLocation(HexLocation location) {
        this.location = location;
    }

    /**
     * Gets the type.
     * @return the type of the hex.
     */
    public HexType getType() {
        return type;
    }

	public ResourceType getResourceType(){
		switch(getType()){
			case BRICK:
				return ResourceType.BRICK;
			case WOOD:
				return ResourceType.WOOD;
			case ORE:
				return ResourceType.ORE;
			case SHEEP:
				return ResourceType.SHEEP;
			case WHEAT:
				return ResourceType.WHEAT;
			default: // desert or water
				return null;
		}
	}

    /**
     * Sets the type
     * @param type the type of the Hex.
     */
    public void setType(HexType type) {
        this.type = type;
    }

    /**
     * Gets the number of the hex
     * @return integer number of the hex
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the number of the hex
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }
}
