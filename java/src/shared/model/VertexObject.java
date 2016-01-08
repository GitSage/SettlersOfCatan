package shared.model;

import shared.locations.VertexLocation;

/**
 * Model class for a VertexObject
 */
public class VertexObject {
    private int owner;
    private VertexLocation location;

    public VertexObject() {
    }

    public VertexObject(int owner, VertexLocation location) {
        this.owner = owner;
        this.location = location;
    }

    /**
     * Gets the index of the owner
     * @return the index of the owner
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Sets the index of the owner
     * @param owner the index of the owner
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * Gets the location
     * @return an EdgeLocation
     */
    public VertexLocation getLocation() {
        return location;
    }

    /**
     * Sets the location
     * @param location the HexLocation
     */
    public void setLocation(VertexLocation location) {
        this.location = location;
    }
}
