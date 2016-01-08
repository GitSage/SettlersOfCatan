package client.resources;

import java.util.*;

import client.base.*;
import client.proxy.ProxyFacade;
import shared.model.GameState;
import shared.model.Player;


/**
 * Implementation for the resource bar controller
 */
public class ResourceBarController extends Controller implements IResourceBarController, Observer {

	private Map<ResourceBarElement, IAction> elementActions;
	private GameState gameState;
	public ResourceBarController(IResourceBarView view) {

		super(view);
		
		elementActions = new HashMap<ResourceBarElement, IAction>();
		GameState.get().addObserver(this);
	}

	@Override
	public IResourceBarView getView() {
		return (IResourceBarView)super.getView();
	}

	/**
	 * Sets the action to be executed when the specified resource bar element is clicked by the user
	 * 
	 * @param element The resource bar element with which the action is associated
	 * @param action The action to be executed
	 */
	public void setElementAction(ResourceBarElement element, IAction action) {

		elementActions.put(element, action);
	}

	@Override
	public void buildRoad() {
		executeElementAction(ResourceBarElement.ROAD);
	}

	@Override
	public void buildSettlement() {
		executeElementAction(ResourceBarElement.SETTLEMENT);
	}

	@Override
	public void buildCity() {
		executeElementAction(ResourceBarElement.CITY);
	}

	@Override
	public void buyCard() {
		executeElementAction(ResourceBarElement.BUY_CARD);
	}

	@Override
	public void playCard() {
		executeElementAction(ResourceBarElement.PLAY_CARD);
	}
	
	private void executeElementAction(ResourceBarElement element) {
		
		if (elementActions.containsKey(element)) {
			
			IAction action = elementActions.get(element);
			action.execute();
		}
	}

	@Override
	public void update(Observable state, Object obj) {
		gameState = (GameState) state;
		Player player = GameState.get().getPlayer(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex());

		//Set the resources
		getView().setElementAmount(ResourceBarElement.BRICK, player.getResources().getBrick());
		getView().setElementAmount(ResourceBarElement.SHEEP, player.getResources().getSheep());
		getView().setElementAmount(ResourceBarElement.ORE, player.getResources().getOre());
		getView().setElementAmount(ResourceBarElement.WOOD, player.getResources().getWood());
		getView().setElementAmount(ResourceBarElement.WHEAT, player.getResources().getWheat());

		//Set the build elements
		getView().setElementAmount(ResourceBarElement.ROAD, player.getRoads());
		getView().setElementAmount(ResourceBarElement.SETTLEMENT, player.getSettlements());
		getView().setElementAmount(ResourceBarElement.CITY, player.getCities());

		//Set Soldiers
		getView().setElementAmount(ResourceBarElement.SOLDIERS, player.getSoldiers());

		//Disable stuff
		getView().setElementEnabled(ResourceBarElement.ROAD, player.canBuildRoad());
		getView().setElementEnabled(ResourceBarElement.SETTLEMENT, player.canBuildSettlement());
		getView().setElementEnabled(ResourceBarElement.CITY, player.canBuildCity());
		getView().setElementEnabled(ResourceBarElement.BUY_CARD, gameState.canBuyDevCard(player.getPlayerIndex()));
		getView().setElementEnabled(ResourceBarElement.PLAY_CARD, player.canPlayDevCard());
	}

}

