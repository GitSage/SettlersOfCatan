package client.discard;

import java.util.Observable;
import java.util.Observer;

import shared.definitions.*;
import shared.model.GameState;
import shared.model.Player;
import shared.model.ResourceList;
import shared.request.move.DiscardRequest;
import client.base.*;
import client.misc.*;
import client.proxy.ProxyFacade;
import shared.states.playing.DiscardWaitingState;
import shared.states.playing.DiscardedState;
import shared.states.playing.DiscardingState;
import shared.states.playing.PlayingState;


/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController, Observer {

	private IWaitView waitView;
	private GameState gameState;
	
	//to keep track of how many resources they have and how many the need to discard
	private Player player;
	
	//to keep track of what the user wants to discard
	private int brick = 0;
	private int wheat = 0;
	private int ore = 0;
	private int sheep = 0;
	private int wood = 0;
	
	//to keep track of how many they need to discard
	private boolean moreNeeded = true;
	
	private void initializeVariables() {

		brick = 0;
		wood = 0;
		wheat = 0;
		sheep = 0;
		ore = 0;
		
	}
	
	/**
	 * DiscardController constructor
	 * 
	 * @param view View displayed to let the user select cards to discard
	 * @param waitView View displayed to notify the user that they are waiting for other players to discard
	 */
	public DiscardController(IDiscardView view, IWaitView waitView) {
		
		super(view);
		
		GameState.get().addObserver(this);
		
		initializeVariables();
		updatePlayer();
		this.waitView = waitView;
	}

	/**
	 * Gets the local player and saves it
	 */
	private void updatePlayer() {
		if(ProxyFacade.getInstance().getLocalPlayer() != null) {
			player = GameState.get().getPlayer(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex());
		}
	}
	
	public IDiscardView getDiscardView() {
		return (IDiscardView)super.getView();
	}
	
	public IWaitView getWaitView() {

		updatePlayer();
		updateView();
		return waitView;
	}

	/**
	 * used by increaseAmount and decreaseAmount
	 * @param resource
	 * @param amount
	 */
	private void changeAmount(ResourceType resource, int amount) {
		
		player = GameState.get().getPlayer(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex());
		
		switch (resource) {
		case BRICK:
			brick += amount;
			break;
		case WOOD:
			wood += amount;
			break;
		case WHEAT:
			wheat += amount;
			break;
		case SHEEP:
			sheep += amount;
			break;
		case ORE:
			ore += amount;
			break;
		}

		updateDiscardButton();
		updateView();
	}
	
	private void updateDiscardButton() {
		int totalDiscarded = wheat + brick + sheep + ore + wood;
		int needed = ( player.getResources().getBrick() +
			 		   player.getResources().getOre() +
					   player.getResources().getWheat() +
					   player.getResources().getSheep() +
					   player.getResources().getWood() ) / 2;
		if(totalDiscarded < needed) {
			moreNeeded = true;
		}
		else {
			moreNeeded = false;
		}
		
		//update view
		getDiscardView().setStateMessage("" + totalDiscarded + "/" + needed + "");
		getDiscardView().setDiscardButtonEnabled(!moreNeeded);

	}
	
	/**
	 * Updates the following parts of the discard view:
	 * -The number of each type of resource that has been chosen to be discarded
	 * -The number of each type of resource that the player currently has
	 * -The up and down buttons for each resource
	 * -The actual discard button (text and enabled) does this by calling updateDiscardButton
	 * 
	 */
	private void updateView() {
		//update max available numbers
		getDiscardView().setResourceMaxAmount(ResourceType.WHEAT, player.getResources().getWheat());
		getDiscardView().setResourceMaxAmount(ResourceType.WOOD, player.getResources().getWood());
		getDiscardView().setResourceMaxAmount(ResourceType.ORE, player.getResources().getOre());
		getDiscardView().setResourceMaxAmount(ResourceType.SHEEP, player.getResources().getSheep());
		getDiscardView().setResourceMaxAmount(ResourceType.BRICK, player.getResources().getBrick());


		//update amounts discarded
		getDiscardView().setResourceDiscardAmount(ResourceType.BRICK, brick);
		getDiscardView().setResourceDiscardAmount(ResourceType.SHEEP, sheep);
		getDiscardView().setResourceDiscardAmount(ResourceType.ORE, ore);
		getDiscardView().setResourceDiscardAmount(ResourceType.WHEAT, wheat);
		getDiscardView().setResourceDiscardAmount(ResourceType.WOOD, wood);


		//update up and down buttons for each resource
		boolean increase;
		boolean decrease; 
		
		if(wood < player.getResources().getWood() && moreNeeded) {
			increase = true;
		}
		else {
			increase = false;
		}
		if(wood > 0) {
			decrease = true;
		}
		else {
			decrease = false;
		}
		getDiscardView().setResourceAmountChangeEnabled(ResourceType.WOOD, increase, decrease);
		
		if(ore < player.getResources().getOre() && moreNeeded) {
			increase = true;
		}
		else {
			increase = false;
		}
		if(ore > 0) {
			decrease = true;
		}
		else {
			decrease = false;
		}
		getDiscardView().setResourceAmountChangeEnabled(ResourceType.ORE, increase, decrease);
		
		if(wheat < player.getResources().getWheat() && moreNeeded) {
			increase = true;
		}
		else {
			increase = false;
		}
		if(wheat > 0) {
			decrease = true;
		}
		else {
			decrease = false;
		}
		getDiscardView().setResourceAmountChangeEnabled(ResourceType.WHEAT, increase, decrease);
		
		if(brick < player.getResources().getBrick() && moreNeeded) {
			increase = true;
		}
		else {
			increase = false;
		}
		if(brick > 0) {
			decrease = true;
		}
		else {
			decrease = false;
		}
		getDiscardView().setResourceAmountChangeEnabled(ResourceType.BRICK, increase, decrease);
		
		if(sheep < player.getResources().getSheep() && moreNeeded) {
			increase = true;
		}
		else {
			increase = false;
		}
		if(sheep > 0) {
			decrease = true;
		}
		else {
			decrease = false;
		}
		getDiscardView().setResourceAmountChangeEnabled(ResourceType.SHEEP, increase, decrease);
		
		updateDiscardButton();
	}

	@Override
	public void increaseAmount(ResourceType resource) {
		changeAmount(resource,1);
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
		changeAmount(resource,-1);
	}

	@Override
	public void discard() {

		//System.out.println("HERE");
		
		ResourceList resources = new ResourceList(brick,ore,sheep,wheat,wood);
		int playerIndex = ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex();
		DiscardRequest request = new DiscardRequest(playerIndex,resources);
		ProxyFacade.getInstance().setPlayingState(new DiscardedState());
		GameState newState = ProxyFacade.getInstance().discardCards(request);
		if(newState == null) {
			System.out.println("ERROR: dscardCards returned null");
		}
		System.out.println("Closing discard modal");
		getDiscardView().closeModal();
//		GameState.get().updateGameState(newState);
		brick = 0;
		wood = 0;
		wheat = 0;
		sheep = 0;
		ore = 0;
	}

	@Override
	public void update(Observable o, Object arg) {
		gameState = (GameState) o;
		updatePlayer();
		updateView();
		if (gameState.canDiscardCards(player.getPlayerIndex())
						&& ! ProxyFacade.getInstance().getPlayingState().getState().equals("DISCARDED")
						&& ! ProxyFacade.getInstance().getPlayingState().getState().equals("DISCARDWAITING")) {
			if ( ! ProxyFacade.getInstance().getPlayingState().getState().equals("DISCARDING")) {
				//Only open the modal once
				ProxyFacade.getInstance().setPlayingState(new DiscardingState());
				System.out.println("Opening discard modal");
				getDiscardView().showModal();
			}
		} else if (GameState.get().getTurnTracker().getStatus() == TurnStatus.DISCARDING) {
			if ( ! ProxyFacade.getInstance().getPlayingState().getState().equals("DISCARDWAITING")) {
				//Only show it once

				ProxyFacade.getInstance().setPlayingState(new DiscardWaitingState());
				getWaitView().setMessage("Other people are discarding please remain calm.");
				//System.out.println("Opening Discard waiting modal");
				System.out.println("Opening discard wait modal");
				getWaitView().showModal();
			}
		}	else {
			//Only close the modal if we are in the DiscardWaitingState
			if (ProxyFacade.getInstance().getPlayingState().advanceFromDiscard()) {
				System.out.println("Closing Discard waiting modal");
				getWaitView().closeModal();
			}
		}
	}
}

