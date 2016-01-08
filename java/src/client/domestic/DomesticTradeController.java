package client.domestic;

import client.data.PlayerInfo;
import client.proxy.ProxyFacade;
import shared.definitions.*;
import client.base.*;
import client.misc.*;
import shared.model.GameState;
import shared.model.ResourceList;
import shared.request.move.AcceptTradeRequest;
import shared.request.move.OfferTradeRequest;
import shared.model.*;
import shared.states.playing.PlayingState;
import shared.states.playing.TradeWaitingState;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController, Observer {

	private IDomesticTradeOverlay tradeOverlay;
	private IWaitView waitOverlay;
	private IAcceptTradeOverlay acceptOverlay;
	private int playerToTradeWith;
	private Map<ResourceType, Integer> resourcesToSend;
	private Map<ResourceType, Integer> resourcesToReceive;
	private GameState gameState;

	/**
	 * DomesticTradeController constructor
	 *
	 * @param tradeView	 Domestic trade view (i.e., view that contains the "Domestic Trade" button)
	 * @param tradeOverlay  Domestic trade overlay (i.e., view that lets the user propose a domestic trade)
	 * @param waitOverlay   Wait overlay used to notify the user they are waiting for another player to accept a trade
	 * @param acceptOverlay Accept trade overlay which lets the user accept or reject a proposed trade
	 */
	public DomesticTradeController(IDomesticTradeView tradeView, IDomesticTradeOverlay tradeOverlay,
								   IWaitView waitOverlay, IAcceptTradeOverlay acceptOverlay) {

		super(tradeView);

		GameState.get().addObserver(this);
		setTradeOverlay(tradeOverlay);
		setWaitOverlay(waitOverlay);
		setAcceptOverlay(acceptOverlay);
		resourcesToSend = new HashMap<>();
		resourcesToReceive = new HashMap<>();
		playerToTradeWith = -1;
	}


	private void reset() {
		resourcesToSend = new HashMap<>();
		resourcesToReceive = new HashMap<>();
		playerToTradeWith = -1;
	}
	public IDomesticTradeView getTradeView() {

		return (IDomesticTradeView) super.getView();
	}

	@Override
	public void update(Observable o, Object arg) {
		
		gameState = (GameState) o;
		
		//the button is only enabled on the players turn
		if(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex() == gameState.getTurnTracker().getCurrentTurn()) {
			getTradeView().enableDomesticTrade(true);
		}
		else {
			getTradeView().enableDomesticTrade(false);
		}
		
		TradeOffer offer = gameState.getTradeOffer();
		if (ProxyFacade.getInstance().getPlayingState().getState().equals("TRADEWAITING") && offer == null) {
			ProxyFacade.getInstance().setPlayingState(new PlayingState());
			System.out.println("Closing trade waiting");
			getWaitOverlay().closeModal();
		}

		if (gameState.getTurnTracker().getStatus() == TurnStatus.PLAYING
						&& offer != null
						&& offer.getReceiver() == ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex()) {

			getAcceptOverlay().reset();
			ResourceList resources = offer.getOffer();
			boolean canAccept = true;
			Player receiver = gameState.getPlayer(offer.getReceiver());

			int brick = resources.getBrick();
			if(brick > 0) {
				this.getAcceptOverlay().addGetResource(ResourceType.BRICK, brick);
			}
			else if(brick < 0) {
				this.getAcceptOverlay().addGiveResource(ResourceType.BRICK, brick * -1);
				canAccept = (receiver.getResources().getBrick() + brick > -1) && canAccept;
			}

			int sheep = resources.getSheep();
			if(sheep > 0) {
				this.getAcceptOverlay().addGetResource(ResourceType.SHEEP, sheep);
			}
			else if(sheep < 0) {
				this.getAcceptOverlay().addGiveResource(ResourceType.SHEEP, sheep * -1);
				canAccept = (receiver.getResources().getSheep() + sheep > -1) && canAccept;
			}
			int ore = resources.getOre();
			if(ore > 0) {
				this.getAcceptOverlay().addGetResource(ResourceType.ORE, ore);
			}
			else if(ore < 0) {
				this.getAcceptOverlay().addGiveResource(ResourceType.ORE, ore * -1);
				canAccept = (receiver.getResources().getOre() + ore > -1) && canAccept;
			}
			int wheat = resources.getWheat();
			if(wheat > 0) {
				this.getAcceptOverlay().addGetResource(ResourceType.WHEAT, wheat);
			}
			else if(wheat < 0) {
				this.getAcceptOverlay().addGiveResource(ResourceType.WHEAT, wheat * -1);
				canAccept = (receiver.getResources().getWheat() + wheat > -1) && canAccept;
			}
			int wood = resources.getWood();
			if(wood > 0) {
				this.getAcceptOverlay().addGetResource(ResourceType.WOOD, wood);
			}
			else if(wood < 0) {
				this.getAcceptOverlay().addGiveResource(ResourceType.WOOD, wood * -1);
				canAccept = (receiver.getResources().getWood() + wood > -1) && canAccept;
			}
			String senderName = gameState.getPlayers().get(offer.getSender()).getName();
			this.getAcceptOverlay().setPlayerName(senderName);
			this.getAcceptOverlay().setAcceptEnabled(canAccept);
			this.getAcceptOverlay().showModal();
		}

	}

	public IDomesticTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IDomesticTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	public IWaitView getWaitOverlay() {
		return waitOverlay;
	}

	public void setWaitOverlay(IWaitView waitView) {
		this.waitOverlay = waitView;
	}

	public IAcceptTradeOverlay getAcceptOverlay() {
		return acceptOverlay;
	}

	public void setAcceptOverlay(IAcceptTradeOverlay acceptOverlay) {
		this.acceptOverlay = acceptOverlay;
	}

	@Override
	public void startTrade() {
		if ( ! GameState.get().isTurnAndStatus(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), TurnStatus.PLAYING)) {
			return;
		}
		reset();
		PlayerInfo[] players = new PlayerInfo[3];
		int playersIndex = 0;
		PlayerInfo[] gamePlayers = GameState.get().getPlayerInfoArray();
		for (int i = 0; i < 4; i++) {
			if (i != ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex()) {
				players[playersIndex] = gamePlayers[i];
				playersIndex++;
			}
		}
		getTradeOverlay().setPlayers(players);
		System.out.println("Show trade modal");
		getTradeOverlay().showModal();
	}

	@Override
	public void decreaseResourceAmount(ResourceType resource) {
		changeResourceAmount(resource, -1);
		if (resourcesToSend.containsKey(resource) &&
				resourcesToSend.get(resource) == 0){
			getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
		}
		else if(resourcesToReceive.containsKey(resource) &&
				resourcesToReceive.get(resource) == 0){
			getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
		}
		//System.out.println(resourcesToSend);
		//System.out.println(resourcesToReceive);
	}

	@Override
	public void increaseResourceAmount(ResourceType resource) {
		changeResourceAmount(resource, 1);
		if (resourcesToSend.containsKey(resource) && getPlayerResource(resource) == resourcesToSend.get(resource) ){
			getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
		}
		if(resourcesToReceive.containsKey(resource)){
			getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
		}
		//System.out.println(resourcesToSend);
		//System.out.println(resourcesToReceive);
	}

	private void changeResourceAmount(ResourceType resource, int inc) {
		if (inc != 1 && inc != -1) {
			//System.out.println("GO KILL YOURSELF");
			return;
		}
		if (resourcesToSend.containsKey(resource)) {
			resourcesToSend.put(resource, resourcesToSend.get(resource) + inc);
		} else if (resourcesToReceive.containsKey(resource)) {
			resourcesToReceive.put(resource, resourcesToReceive.get(resource) + inc);
		}

		if (resourcesToSend.containsKey(resource) &&
				resourcesToSend.get(resource) < getPlayerResource(resource) &&
				resourcesToSend.get(resource) > 0){
			getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
		}

		checkEnableButton();
	}

	@Override
	public void sendTradeOffer() {
		ResourceList resourceList = new ResourceList(0, 0, 0, 0, 0);

		for (ResourceType key : resourcesToSend.keySet()) {
			resourceList.setResource(key, resourcesToSend.get(key));
		}
		for (ResourceType key : resourcesToReceive.keySet()) {
			resourceList.setResource(key, resourcesToReceive.get(key) * -1);
		}

		OfferTradeRequest request = new OfferTradeRequest(
				ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(),
				resourceList,
				playerToTradeWith);
		ProxyFacade.getInstance().offerTrade(request);
		System.out.println("Closing Trade Offer Modal");
		getTradeOverlay().closeModal();
		getTradeOverlay().reset();
		reset();
		ProxyFacade.getInstance().setPlayingState(new TradeWaitingState());
		System.out.println("Opening Trade Waiting Modal");
		getWaitOverlay().showModal();
	}

	@Override
	public void setPlayerToTradeWith(int playerIndex) {
		playerToTradeWith = playerIndex;
		checkEnableButton();
		//System.out.println(playerIndex);
	}

	@Override
	public void setResourceToReceive(ResourceType resource) {
		resourcesToSend.remove(resource);
		resourcesToReceive.put(resource, 0);
		getTradeOverlay().setResourceAmount(resource, "0");
		getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
		checkEnableButton();
		//System.out.println(resourcesToSend);
		//System.out.println(resourcesToReceive);
	}

	@Override
	public void setResourceToSend(ResourceType resource) {
		resourcesToReceive.remove(resource);
		resourcesToSend.put(resource, 0);
		getTradeOverlay().setResourceAmount(resource, "0");
		if(getPlayerResource(resource) == 0)
			getTradeOverlay().setResourceAmountChangeEnabled(resource, false, false);
		else{
			getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
		}
		checkEnableButton();
		//System.out.println(resourcesToSend);
		//System.out.println(resourcesToReceive);
	}

	@Override
	public void unsetResource(ResourceType resource) {
		resourcesToReceive.remove(resource);
		resourcesToSend.remove(resource);
		checkEnableButton();
		//System.out.println(resourcesToSend);
		//System.out.println(resourcesToReceive);
	}

	@Override
	public void cancelTrade() {
		getTradeOverlay().reset();
		reset();
		System.out.println("Offer trade closing");
		getTradeOverlay().closeModal();
	}

	@Override
	public void acceptTrade(boolean willAccept) {
		AcceptTradeRequest request = new AcceptTradeRequest(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), willAccept);
		ProxyFacade.getInstance().acceptTrade(request);
		//Closing accept modal
		getAcceptOverlay().closeModal();
	}

	private int getPlayerResource(ResourceType resource){
		return GameState.get().getPlayer(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex()).getResources().getResource(resource);
	}

	private void checkEnableButton(){
		boolean hasToSend = false;
		boolean hasToReceive = false;
		boolean hasChosenReceiver;
		for(ResourceType resource : resourcesToSend.keySet()){
			if(resourcesToSend.get(resource) > 0){
				hasToSend = true;
			}
		}
		for(ResourceType resource : resourcesToReceive.keySet()){
			if(resourcesToReceive.get(resource) > 0){
				hasToReceive = true;
			}
		}

		hasChosenReceiver = this.playerToTradeWith != -1;

		if (hasToReceive && hasToSend && hasChosenReceiver) {
			getTradeOverlay().setStateMessage("Trade!");
			getTradeOverlay().setTradeEnabled(true);
		} else {
			getTradeOverlay().setStateMessage("Select Resources to Trade");
			getTradeOverlay().setTradeEnabled(false);

		}
	}
}

