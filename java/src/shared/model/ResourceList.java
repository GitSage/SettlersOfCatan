package shared.model;


import shared.definitions.ResourceType;

/**
 * A  model class for a ResourceList. Contains a field for quantities of each type of resource.
 */
public class ResourceList{
    private int brick;
    private int ore;
    private int sheep;
    private int wheat;
    private int wood;

    public void setResource(ResourceType resource, int amt){
        switch(resource){
            case BRICK:
                setBrick(amt);
                break;
            case ORE:
                setOre(amt);
                break;
            case SHEEP:
                setSheep(amt);
                break;
            case WHEAT:
                setWheat(amt);
                break;
            case WOOD:
                setWood(amt);
                break;
        }
    }

    public int getResource(ResourceType resource){
        switch(resource){
            case BRICK:
                return getBrick();
            case ORE:
                return getOre();
            case SHEEP:
                return getSheep();
            case WHEAT:
                return getWheat();
            case WOOD:
                return getWood();
        }
        return 0;
    }

    public void reduce(int brick, int ore, int sheep, int wheat, int wood){
        setBrick(getBrick() - brick);
        setOre(getOre() - ore);
        setSheep(getSheep() - sheep);
        setWheat(getWheat() - wheat);
        setWood(getWood() - wood);
    }

    public void reduce(TradeOffer offer){
        reduce(offer.getOffer());
    }

    public void reduce(ResourceList resources){
        reduce(resources.getBrick(),
                resources.getOre(),
                resources.getSheep(),
                resources.getWheat(),
                resources.getWood());
    }

    public void increase(TradeOffer offer){
        increase(offer.getOffer());
    }

	public void increase(ResourceList resources){
		setBrick(getBrick() + resources.getBrick());
		setOre(getOre() + resources.getOre());
		setSheep(getSheep() + resources.getSheep());
		setWheat(getWheat() + resources.getWheat());
		setWood(getWood() + resources.getWood());
	}


	/**
     * Gets the number of bricks.
     * @return the number of bricks.
     */
    public int getBrick() {
        return brick;
    }

    /**
     * Sets the number of bricks.
     * @param brick the number of bricks.
     */
    public void setBrick(int brick) {
        this.brick = brick;
    }

    /**
     * Gets the number of ores.
     * @return the number of ores.
     */
    public int getOre() {
        return ore;
    }

    /**
     * Sets the number of bricks.
     * @param ore the number of ores.
     */
    public void setOre(int ore) {
        this.ore = ore;
    }

    /**
     * Gets the number of sheep.
     * @return the number of sheep.
     */
    public int getSheep() {
        return sheep;
    }

    /**
     * Sets the number of bricks.
     * @param sheep the number of sheep.
     */
    public void setSheep(int sheep) {
        this.sheep = sheep;
    }

    /**
     * Gets the number of wheat.
     * @return the number of wheat.
     */
    public int getWheat() {
        return wheat;
    }

    /**
     * Sets the number of bricks.
     * @param wheat the number of wheat.
     */
    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    /**
     * Gets the number of wood.
     * @return the number of wood.
     */
    public int getWood() {
        return wood;
    }

    /**
     * Sets the number of bricks.
     * @param wood the number of wood.
     */
    public void setWood(int wood) {
        this.wood = wood;
    }

	public ResourceList(int brick, int ore, int sheep, int wheat, int wood) {
		super();
		this.brick = brick;
		this.ore = ore;
		this.sheep = sheep;
		this.wheat = wheat;
		this.wood = wood;
	}
}