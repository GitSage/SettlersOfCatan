package shared.model;

import shared.definitions.PortType;
import shared.locations.EdgeDirection;
import shared.locations.HexLocation;

/**
 * A model for a Port
 */
public class Port {
    private PortType type;
    private HexLocation location;
    private EdgeDirection direction;

    public PortType getType() {
        return type;
    }

    public void setType(PortType type) {
        this.type = type;
    }

    /**
     * Gets the hex location
     * @return a HexLocation object
     */
    public HexLocation getLocation() {
        return location;
    }

    /**
     * Sets the hex location
     * @param location a HexLocation object
     */
    public void setLocation(HexLocation location) {
        this.location = location;
    }

    /**
     * Gets the direction
     * @return the direction the port is facing.
     */
    public EdgeDirection getDirection() {
        return direction;
    }

    /**
     * Sets the direction
     * @param direction a String direction
     */
    public void setDirection(EdgeDirection direction) {
        this.direction = direction;
    }

    /**
     * Gets the ratio
     * @return an integer ratio
     */
    public int getRatio() {
        return getType() == PortType.THREE ? 3 : 2;
    }

}
