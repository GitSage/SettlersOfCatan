package shared.model;

import shared.locations.*;
/**
 * A model for an Road
 */
public class Road {
    private int owner;
    private EdgeLocation location;

    public Road(int owner, EdgeLocation location) {
        this.owner = owner;
        this.location = location;
    }

    public Road() {
    }

    /**
     * Gets the index of the owner.
     * @return the integer index of the owner.
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     * @param owner the integer index of the owner.
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * Gets the location of the Road
     * @return the EdgeLocation object representing the location of the Road.
     */
    public EdgeLocation getLocation() {
        return location;
    }

    /**
     * Sets the location of the Road
     * @param location the EdgeLocation of the value.
     */
    public void setLocation(EdgeLocation location) {
        this.location = location;
    }
}
