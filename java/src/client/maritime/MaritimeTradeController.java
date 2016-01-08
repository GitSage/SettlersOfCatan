package client.maritime;

import client.proxy.ProxyFacade;
import shared.definitions.*;
import client.base.*;
import shared.locations.*;
import shared.model.*;
import shared.request.move.MaritimeTradeRequest;

import java.util.*;


/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController, Observer {

	private IMaritimeTradeOverlay tradeOverlay;
    private ResourceType getResource;
    private ResourceType giveResource;
	
	public MaritimeTradeController(IMaritimeTradeView tradeView, IMaritimeTradeOverlay tradeOverlay) {
		
		super(tradeView);

		GameState.get().addObserver(this);
		
		setTradeOverlay(tradeOverlay);
	}
	
	public IMaritimeTradeView getTradeView() {
		
		return (IMaritimeTradeView)super.getView();
	}
	
	public IMaritimeTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IMaritimeTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	@Override
	public void startTrade() {
		if ( ! GameState.get().isTurnAndStatus(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), TurnStatus.PLAYING)) {
			return;
		}
		//reset overlay options
		getTradeOverlay().hideGetOptions();
		getTradeOverlay().setTradeEnabled(false);
		this.getResource = null;
		this.giveResource = null;
		getTradeOverlay().showModal();
        getTradeOverlay().showGiveOptions(getEnabledResources());
    }

	@Override
	public void makeTrade() {
        MaritimeTradeRequest request = new MaritimeTradeRequest(
                ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(),
                getRatio(giveResource),
                giveResource,
                getResource);
        GameState.get().updateGameState(ProxyFacade.getInstance().maritimeTrade(request));
		getTradeOverlay().closeModal();
	}

	@Override
	public void cancelTrade() {
		getTradeOverlay().closeModal();
	}

	@Override
	public void setGetResource(ResourceType resource) {
        this.getResource = resource;
        getTradeOverlay().selectGetOption(resource, 1);
        if(this.giveResource != null && this.getResource != null) {
        	getTradeOverlay().setTradeEnabled(true);
        }
        else {
        	getTradeOverlay().setTradeEnabled(false);
        }
	}

	@Override
	public void setGiveResource(ResourceType resource) {
        this.giveResource = resource;
        getTradeOverlay().showGetOptions(getResourcesAvailableInBank());
        getTradeOverlay().selectGiveOption(resource, getRatio(resource));
        if(this.giveResource != null && this.getResource != null) {
        	getTradeOverlay().setTradeEnabled(true);
        }
        else {
        	getTradeOverlay().setTradeEnabled(false);
        }
	}

	@Override
	public void unsetGetValue() {
        this.getResource = null;
        getTradeOverlay().setTradeEnabled(false);
        getTradeOverlay().showGetOptions(getResourcesAvailableInBank());
	}

	@Override
	public void unsetGiveValue() {
        this.giveResource = null;
        getTradeOverlay().setTradeEnabled(false);
        getTradeOverlay().showGiveOptions(getEnabledResources());
	}

    private ResourceType[] getResourcesAvailableInBank(){
        ResourceType[] enabledResources = new ResourceType[5];
        if(GameState.get().getBank().getWheat() > 0)
            enabledResources[0] = ResourceType.WHEAT;
        if(GameState.get().getBank().getOre() > 0)
            enabledResources[1] = ResourceType.ORE;
        if(GameState.get().getBank().getSheep() > 0)
            enabledResources[2] = ResourceType.SHEEP;
        if(GameState.get().getBank().getWood() > 0)
            enabledResources[3] = ResourceType.WOOD;
        if(GameState.get().getBank().getBrick() > 0)
            enabledResources[4] = ResourceType.BRICK;

        return enabledResources;
    }

    private int getRatio(ResourceType resource){
        Set<PortType> ownedPorts = getOwnedPorts();
        PortType port = null;
        int ratio = 4;

        if(ownedPorts.contains(PortType.THREE))
            ratio = 3;

        switch(resource){
            case WOOD:
                port = PortType.WOOD;
                break;
            case BRICK:
                port = PortType.BRICK;
                break;
            case WHEAT:
                port = PortType.WHEAT;
                break;
            case ORE:
                port = PortType.ORE;
                break;
            case SHEEP:
                port = PortType.SHEEP;
                break;
        }

        if(ownedPorts.contains(port)){
            ratio = 2;
        }

        return ratio;
    }

    /**
     * Gets all resources that the player is able to maritime trade.
     * This operates on the player whose turn it currently is (TurnTracker.getCurrentTurn())
     * @return a ResourceType[] object containing the players whose turn it currently is.
     */
    private ResourceType[] getEnabledResources(){
        int playerInd = GameState.get().getTurnTracker().getCurrentTurn();
        Player player = GameState.get().getPlayer(playerInd);
        ResourceList resources = player.getResources();

        Set<PortType> ownedPorts = getOwnedPorts();
        ResourceType[] enabledResources = new ResourceType[5];

        // I apologize for the following code.
        // It's COMPLETELY DUMB that I need to use an array instead of a set.
        // It's just asking for a bug, too.
        // The function I need to call, which was NOT written by me, needs data in this format.
        // Array indices here are completely arbitrary and don't actually mean anything significant.

        // wood
        if(resources.getWood() >= 4 ||
                resources.getWood() >= 3 && ownedPorts.contains(PortType.THREE) ||
                resources.getWood() >= 2 &&  ownedPorts.contains(PortType.WOOD) ) {
            enabledResources[0] = ResourceType.WOOD;
        }

        // wheat
        if(resources.getWheat() >= 4 ||
                resources.getWheat() >= 3 && ownedPorts.contains(PortType.THREE) ||
                resources.getWheat() >= 2 &&  ownedPorts.contains(PortType.WHEAT) ) {
            enabledResources[1] = ResourceType.WHEAT;
        }

        // sheep
        if(resources.getSheep() >= 4 ||
                resources.getSheep() >= 3 && ownedPorts.contains(PortType.THREE) ||
                resources.getSheep() >= 2 && ownedPorts.contains(PortType.SHEEP) ) {
            enabledResources[2] = ResourceType.SHEEP;
        }

        // brick
        if(resources.getBrick() >= 4 ||
                resources.getBrick() >= 3 && ownedPorts.contains(PortType.THREE) ||
                resources.getBrick() >= 2 && ownedPorts.contains(PortType.BRICK) ) {
            enabledResources[3] = ResourceType.BRICK;
        }

        // ore
        if(resources.getOre() >= 4 ||
                resources.getOre() >= 3 && ownedPorts.contains(PortType.THREE) ||
                resources.getOre() >= 2 && ownedPorts.contains(PortType.ORE) ) {
            enabledResources[4] = ResourceType.ORE;
        }

        return enabledResources;
    }

    /**
     * Gets a Set of all the types of ports owned by the player.
     * The player will be the player whose turn it currently is.
     * TODO: Move this method to GameMap, GameState, or Player (?)
     * @return Set<PortType>
     */
    private Set<PortType> getOwnedPorts(){
        return GameState.get().getPlayerPorts();
    }

	@Override
	public void update(Observable o, Object arg) {
		
		GameState gameState = (GameState) o;
		
		//the button is only enabled on the players turn
		if(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex() == gameState.getTurnTracker().getCurrentTurn()) {
			getTradeView().enableMaritimeTrade(true);
		}
		else {
			getTradeView().enableMaritimeTrade(false);
		}
	}

}

