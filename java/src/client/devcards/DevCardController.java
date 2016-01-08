package client.devcards;

import client.proxy.ProxyFacade;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import client.base.*;
import shared.model.DevCardList;
import shared.model.GameState;
import shared.model.Player;
import shared.request.move.*;

import java.util.Observable;
import java.util.Observer;


/**
 * "Dev card" controller implementation
 */
public class DevCardController extends Controller implements IDevCardController, Observer {

	private IBuyDevCardView buyCardView;
	private IAction soldierAction;
	private IAction roadAction;
	private GameState gameState;
	private Player player;
	/**
	 * DevCardController constructor
	 * 
	 * @param view "Play dev card" view
	 * @param buyCardView "Buy dev card" view
	 * @param soldierAction Action to be executed when the user plays a soldier card.  It calls "mapController.playSoldierCard()".
	 * @param roadAction Action to be executed when the user plays a road building card.  It calls "mapController.playRoadBuildingCard()".
	 */
	public DevCardController(IPlayDevCardView view, IBuyDevCardView buyCardView, 
								IAction soldierAction, IAction roadAction) {

		super(view);
		
		this.buyCardView = buyCardView;
		this.soldierAction = soldierAction;
		this.roadAction = roadAction;
		GameState.get().addObserver(this);
	}

	public IPlayDevCardView getPlayCardView() {
		return (IPlayDevCardView)super.getView();
	}

	public IBuyDevCardView getBuyCardView() {
		return buyCardView;
	}

	@Override
	public void startBuyCard() {
		
		getBuyCardView().showModal();
	}

	@Override
	public void cancelBuyCard() {
		
		getBuyCardView().closeModal();
	}

	@Override
	public void buyCard() {
		if( gameState.canBuyDevCard(player.getPlayerIndex()) ) {
			BuyDevCardRequest request = new BuyDevCardRequest(player.getPlayerIndex());
			ProxyFacade.getInstance().buyDevCard(request);
		}
		getBuyCardView().closeModal();
	}

	@Override
	public void startPlayCard() {
		getPlayCardView().showModal();
	}

	@Override
	public void cancelPlayCard() {

		getPlayCardView().closeModal();
	}

	@Override
	public void playMonopolyCard(ResourceType resource) {
		if (gameState.canUseMonopoly(player.getPlayerIndex())){
			MonopolyCardRequest request = new MonopolyCardRequest(player.getPlayerIndex(), resource);
			ProxyFacade.getInstance().monopoly(request);
		}
	}

	@Override
	public void playMonumentCard() {
		if (gameState.canUseMonument(player.getPlayerIndex())){
			MonumentCardRequest request = new MonumentCardRequest(player.getPlayerIndex());
			ProxyFacade.getInstance().monument(request);
		}
	}

	@Override
	//TODO
	public void playRoadBuildCard() {
		roadAction.execute();
	}

	@Override
	//TODO
	public void playSoldierCard() {
		if (gameState.canUseSoldier(player.getPlayerIndex())) {
			soldierAction.execute();
		}
	}

	@Override
	public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
		if (gameState.canUseYearOfPlenty(player.getPlayerIndex())){
			YearOfPlentyCardRequest request = new YearOfPlentyCardRequest(player.getPlayerIndex(), resource1, resource2);
			ProxyFacade.getInstance().yearOfPlenty(request);
		}

	}
	public void update(Observable state, Object obj) {
		gameState = (GameState) state;

		player = GameState.get().getPlayer(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex());
		DevCardList newCards = player.getNewDevCards();
		DevCardList oldCards = player.getOldDevCards();

		getPlayCardView().setCardAmount(DevCardType.SOLDIER, newCards.getSoldier() + oldCards.getSoldier());
		getPlayCardView().setCardAmount(DevCardType.YEAR_OF_PLENTY, newCards.getYearOfPlenty() + oldCards.getYearOfPlenty());
		getPlayCardView().setCardAmount(DevCardType.MONOPOLY, newCards.getMonopoly() + oldCards.getMonopoly());
		getPlayCardView().setCardAmount(DevCardType.ROAD_BUILD, newCards.getRoadBuilding() + oldCards.getRoadBuilding());
		getPlayCardView().setCardAmount(DevCardType.MONUMENT, newCards.getMonument() + oldCards.getMonument());

		getPlayCardView().setCardEnabled(DevCardType.SOLDIER, gameState.canUseSoldier(player.getPlayerIndex()));
		getPlayCardView().setCardEnabled(DevCardType.YEAR_OF_PLENTY, gameState.canUseYearOfPlenty(player.getPlayerIndex()));
		getPlayCardView().setCardEnabled(DevCardType.MONOPOLY, gameState.canUseMonopoly(player.getPlayerIndex()));
		getPlayCardView().setCardEnabled(DevCardType.ROAD_BUILD, gameState.canUseRoadBuilder(player.getPlayerIndex()));
		getPlayCardView().setCardEnabled(DevCardType.MONUMENT, gameState.canUseMonument(player.getPlayerIndex()));
	}

}

