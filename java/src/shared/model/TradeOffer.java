package shared.model;

/**
 * A model class for a TradeOffer.
 */
public class TradeOffer {
    private int sender;
    private int receiver;
    private ResourceList offer;

    /**
     * Gets the index of the sender.
     * @return an integer index of the sender.
     */
    public int getSender() {
        return sender;
    }

    /**
     * Sets the sender.
     * @param sender the integer index of the sender.
     */
    public void setSender(int sender) {
        this.sender = sender;
    }

    /**
     * Gets the index of the receiver.
     * @return the integer index of the receiver.
     */
    public int getReceiver() {
        return receiver;
    }

    /**
     * Sets the integer index of the receiver.
     * @param receiver the integer index of the receiver.
     */
    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    /**
     * Gets the Resourcelist offer.
     * @return a ResourceList object.
     */
    public ResourceList getOffer() {
        return offer;
    }

    /**
     * Sets the ResourceList.
     * @param offer the new ResourceList offer.
     */
    public void setOffer(ResourceList offer) {
        this.offer = offer;
    }
}