package shared.model;

import client.communication.LogEntry;
import client.data.PlayerInfo;
import client.data.RobPlayerInfo;
import client.proxy.ProxyFacade;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.TurnStatus;
import shared.locations.*;
import shared.states.playing.RoadBuildState;
import shared.states.playing.IState;

import java.util.*;

/**
 * The model for a Client.
 */
public class GameState extends Observable implements Runnable{
	private static GameState gameState;
	private ResourceList bank;
    private MessageList chat;
	private MessageList log;
	private GameMap map;
	private List<Player> players;
	private TradeOffer tradeOffer;
	private TurnTracker turnTracker;
	private DevCardList deck;
	private int version = -1;
	private int winner = -1;
	private HashMap<String, Player> playerMap;
	private List<LogEntry> gameHistory;

	public static void nuke() {
		get().setBank(null);
		get().setChat(null);
		get().setLog(new MessageList());
		get().setMap(null);;
		get().setPlayers(new LinkedList<Player>());	//should reset players and playerMap
		get().setTradeOffer(null);
		get().setTurnTracker(null);
		get().setDeck(null);
		get().setVersion(-1);
		get().setWinner(-1);
		get().setGameHistory(new ArrayList<LogEntry>());
	}


	public void reset() {
		bank = new ResourceList(19,19,19,19,19);
		chat = new MessageList();
		log = new MessageList();
		tradeOffer = null;
		version = -1;
		winner = -1;
		gameHistory = new ArrayList<>();
		deck = new DevCardList(2,5,2,14,2);

		for(Player player : getPlayers()) {
			player.reset();
		}
		
		//Have their own reset methods
		turnTracker.reset();
		map.reset();
	}



	private void setGameHistory(ArrayList<LogEntry> newGameHistory) {
		gameHistory = newGameHistory;
	}

	public static GameState get() {
		if (gameState == null) {
			gameState = new GameState();
		}
		return gameState;
	}

		public synchronized void run() {
			//System.out.println("Polled");
			if (ProxyFacade.getInstance().getSetupState().getState().equals("ADDAI")) {
				//System.out.println("addai poll");
				GameState newState = ProxyFacade.getInstance().model(999999);
				GameState.get().updateGameState(newState);
			} else {
				GameState newState = ProxyFacade.getInstance().model();
				if (newState.getVersion() > this.getVersion()) {
					GameState.get().updateGameState(newState);
				}
			}
		}

	public GameState(ResourceList bank, MessageList chat, MessageList log, GameMap map, List<Player> players,
					 TradeOffer tradeOffer, TurnTracker turnTracker, DevCardList deck, int version, int winner) {
		this.bank = bank;
		this.chat = chat;
		this.log = log;
		this.map = map;
		this.players = players;
		this.tradeOffer = tradeOffer;
		this.turnTracker = turnTracker;
		this.deck = deck;
		this.version = version;
		this.winner = winner;
	}

	public GameState(){
		this.gameHistory = new ArrayList<LogEntry>();
	}

	public GameState(boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
		this.bank = new ResourceList(19, 19, 19, 19, 19);
		this.chat = new MessageList();
		this.log = new MessageList();
		this.map = new GameMap(randomTiles, randomNumbers, randomPorts);
		this.players = new ArrayList<>();
		this.tradeOffer = null;
		this.turnTracker = new TurnTracker();
		this.deck = new DevCardList(2, 5, 2, 14, 2);
		this.version = 0;
		this.winner = -1;
		this.playerMap = new HashMap<>();
		this.gameHistory = new ArrayList<LogEntry>();
	}

	public PlayerInfo[] getPlayerInfoArray() {
		PlayerInfo[] playerInfoArray = new PlayerInfo[players.size()];
		for (Player daPlaya : players) {
			playerInfoArray[daPlaya.getPlayerIndex()] = daPlaya.toPlayerInfo();
		}
		return playerInfoArray;
	}

	/**
	 * Updates the game state if the new version is newer.
	 * @pre a new game state exists
	 * @post the members of this game state have been set to the members of the new game state.
	 * @param newGameState the new game state.
	 */
	public synchronized void updateGameState(GameState newGameState){
		if(newGameState == null) {// || newGameState.getVersion() <= getVersion()){
			return;
		}

		//System.out.println("Game State Updating");
		setBank(newGameState.getBank());
		setChat(newGameState.getChat());
		setPlayers(newGameState.getPlayers());
		setLog(newGameState.getLog());
		setMap(newGameState.getMap());
		setTradeOffer(newGameState.getTradeOffer());
		setTurnTracker(newGameState.getTurnTracker());
		setDeck(newGameState.getDeck());
		setVersion(newGameState.getVersion());
		setWinner(newGameState.getWinner());
		setChanged();
		
		//update local player color
		Player player = newGameState.getPlayerById(ProxyFacade.getInstance().getLocalPlayer().getId());	//get current local player (so we know their id)
		ProxyFacade.getInstance().getLocalPlayer().setColor(this.getPlayer(player.getPlayerIndex()).getColor()); // set the local player's color to the color of the player with the same index
		
		//controllers are observers
		notifyObservers();
	}

	/**
	 * Decides if a player can discard cards or not.
	 * @pre none
	 * @post The player can return cards if the following conditions are met: <br />
	 * <ol>
	 *	 <li>The current Status is DISCARDING.</li>
	 *	 <li>The player has more than 7 cards (i.e., 8 or more).</li>
	 * </ol>
	 * @param player a player's index
	 * @return true if the player can discard cards, false otherwise.
	 */
	public boolean canDiscardCards(int player){
		if (turnTracker.getStatus() != TurnStatus.DISCARDING) {
			return false;
		}
		if(getPlayer(player).getTotalNumCards()<8) {
			return false;
		}

		return true;
	}

	/**
	 * Gets a random dev card from the bank. Decrements that dev card.
	 * @return
	 */
	public DevCardType getRandomDevCardFromBank(){
		int monopolyRange = deck.getMonopoly();
		int yearOfPlentyRange = deck.getYearOfPlenty() + monopolyRange;
		int roadBuildingRange = deck.getRoadBuilding() + yearOfPlentyRange;
		int soldierRange = deck.getSoldier() + roadBuildingRange;
		int monumentRange = deck.getMonument() + soldierRange;

		int rand = (int)(Math.random()*monumentRange);
		if(rand < monopolyRange) {
			deck.setMonopoly(deck.getMonopoly() - 1);
			return DevCardType.MONOPOLY;
		}
		else if(rand < yearOfPlentyRange){
			deck.setYearOfPlenty(deck.getYearOfPlenty() - 1);
			return DevCardType.YEAR_OF_PLENTY;
		}
		else if(rand < roadBuildingRange){
			deck.setRoadBuilding(deck.getRoadBuilding() - 1);
			return DevCardType.ROAD_BUILD;
		}
		else if(rand < soldierRange){
			deck.setSoldier(deck.getSoldier() - 1);
			return DevCardType.SOLDIER;
		}
		else if(rand < monumentRange){
			deck.setMonument(deck.getMonument() - 1);
			return DevCardType.MONUMENT;
		}
		else{
			return null;
		}
	}

	/**
	 * Decides if a player can roll or not.
	 * @pre none
	 * @post The player can roll if the following conditions are met: <br />
	 * <ol>
	 *	 <li>It is the player's turn.</li>
	 *	 <li>The turn status is ROLLING</li>
	 * </ol>
	 * @param player a player's index
	 * @return true if the player can move, false otherwise.
	 */
	public boolean canRollNumber(int player){
		// Check that it is the player's turn.
		if(!getTurnTracker().isPlayerTurn(player)) {
			return false;
		}

		// Check that the turn status is ROLLING
		if(getTurnTracker().getStatus() != TurnStatus.ROLLING) {
			return false;
		}

		return true;
	}

	/**
	 * Decides if a player can build a road.
	 * @pre none
	 * @post The player can build the specified road only if the following conditions are met: <br />
	 * <ol>
	 *	 <li>It is the player's turn.</li>
	 *	 <li>There is no road at the specified location.</li>
	 *	 <li>The specified location is adjacent to one of the player's roads, settlements, or cities OR TurnStatus is
	 *		 FIRSTROUND or SECONDROUND</li>
	 *	 <li>The turn status is PLAYING or FIRSTROUND or SECONDROUND</li>
	 *	 <li>The road location is on land</li>
	 *	 <li>The player has 1 or more each of Brick and Lumber</li>
	 * </ol>
	 * @param player int the player index who is trying to build the road
	 * @param location Road the road that should be built
	 * @return true if the player can build the road, false otherwise.
	 */
	public boolean canBuildRoad(int player, EdgeLocation location){
		EdgeLocation nLocation = location.getNormalizedLocation();

		ProxyFacade facade = ProxyFacade.getInstance();
		if (facade != null) {
			IState state = facade.getPlayingState();
			if (state instanceof RoadBuildState) {
				EdgeLocation tempLocation = ((RoadBuildState) state).getTempLocation();
				if (tempLocation != null) {
					getMap().getRoads().add(new Road(player, tempLocation));
				}
			}
		}

		// It is the player's turn.
		if(!getTurnTracker().isPlayerTurn(player))
			return false;

		// there is no road at the specified location
		if(getMap().isRoadAtLocation(nLocation)) {
			////System.out.println(road.getLocation().getDirection());
			////System.out.println("Failing because road at location.");
			return false;
		}

		//the specified location is adjacent to one of the player's roads, settlements, or cities
		if(!getMap().roadAdjToPlayerRoadSettCity(player, nLocation) &&
				getTurnTracker().getStatus() != TurnStatus.FIRSTROUND &&
				getTurnTracker().getStatus() != TurnStatus.SECONDROUND) {
			return false;
		}

		// the specified location is on land
		if(!getMap().isEdgeOnLand(nLocation)) {
			////System.out.println("Failing because road not on land");
			return false;
		}

		// if it's firstround or secondround, make sure that an empty vertex is available
		if ((getTurnTracker().getStatus() == TurnStatus.FIRSTROUND ||
				getTurnTracker().getStatus() == TurnStatus.SECONDROUND) &&
				!getMap().edgeHasFreeVertex(location)) {
			return false;
		}

		// The turn status is PLAYING or FIRSTROUND or SECONDROUND
		if(getTurnTracker().getStatus() != TurnStatus.PLAYING &&
		   getTurnTracker().getStatus() != TurnStatus.FIRSTROUND &&
		   getTurnTracker().getStatus() != TurnStatus.SECONDROUND) {
			////System.out.println("Failing because bad turn status");
			return false;
		}

		return true;
	}

	/**
	 * Decides if a player can build a settlement.
	 * @pre none
	 * @post The player can build the specified settlement only if the following conditions are met: <br />
	 * <ol>
	 *	 <li>The targeted location does not already contain a Settlement or a City</li>
	 *	 <li>It is the player's turn</li>
	 *	 <li>The TurnStatus is either PLAYING or FIRSTROUND or SECONDROUND</li>
	 *	 <li>The settlement is connected to 1 more more of the player's roads OR the TurnStatus is FIRSTROUND or
	 *		 SECONDROUND</li>
	 *	 <li>The Distance Rule is followed (none of the 3 adjacent intersections contains a settlement or a city).</li>
	 *	 <li>The specified location is on land.</li>
	 *	 <li>The player has 1 or more: Brick, Wood, Sheep, and Wheat OR the TurnStatus is FIRSTROUND or SECONDROUND</li>
	 * </ol>
	 * @param player int the index of the player in question
	 * @param loc VertexObject the settlement under consideration
	 * @return true if the player can build the settlement at the location, false otherwise.
	 */
	public boolean canBuildSettlement(int player, VertexLocation loc){
		VertexLocation nLoc = loc.getNormalizedLocation();
		// the targeted location does not already contain a Settlement or a City
		if(getMap().isCityOrSettlementAtLocation(nLoc)) {
			////System.out.println("canBuildSettlement failing because location is occupied");
			return false;
		}

		// It is the player's turn.
		if(!getTurnTracker().isPlayerTurn(player)) {
			////System.out.println("canBuildSettlement failing because it is not the player's turn");
			return false;
		}

		// The turn status is PLAYING or FIRSTROUND or SECONDROUND
		if(getTurnTracker().getStatus() != TurnStatus.PLAYING &&
		   getTurnTracker().getStatus() != TurnStatus.FIRSTROUND &&
		   getTurnTracker().getStatus() != TurnStatus.SECONDROUND) {
			////System.out.println("canBuildSettlement failing because Turn Status is wrong");
			return false;
		}

		// The location is on land.
		if(!getMap().isVertexLocationOnLand(nLoc)) {
			////System.out.println("canBuildSettlement failing because location is not located on land");
			return false;
		}

		// The settlement is connected to 1 more more of the player's roads
		if(!getMap().isAdjacentToPlayerRoad(player, nLoc)) {
			////System.out.println("canBuildSettlement failing because location is not adjacent to player road");
			return false;
		}

		// The Distance Rule is followed (none of the 3 adjacent intersections contains a settlement or a city).
		if(getMap().hasAdjacentCityOrSettlement(nLoc)) {
			////System.out.println("canBuildSettlement failing another city is already adjacent");
			return false;
		}

		// The player has 1 or more: Brick, Wood, Sheep, and Wheat OR the TurnStatus is FIRSTROUND or SECONDROUND
		if(getTurnTracker().getStatus() != TurnStatus.FIRSTROUND &&
				getTurnTracker().getStatus() != TurnStatus.SECONDROUND &&
				(getPlayer(player).getResources().getBrick() == 0 ||
				getPlayer(player).getResources().getWheat() == 0 ||
				getPlayer(player).getResources().getSheep() == 0 ||
				getPlayer(player).getResources().getWheat() == 0)) {
			////System.out.println("canBuildSettlement failing because player lacks resources");
			return false;
		}
		return true;
	}

	/**
	 * Decides if a player can build a city.
	 * @pre none
	 * @post The player can build the specified city only if the following conditions are met: <br />
	 * <ol>
	 *	  <li>It is the player's turn.</li>
	 *	  <li>Turn tracker status is PLAYING</li>
	 *	  <li>The targeted location contains a settlement belonging to the player.</li>
	 *	  <li>The player has: 3 or more ore, 2 or more wheat.</li>
	 * </ol>
	 * @param player int the index of the player who might build the city
	 * @param loc VertexObject the location where the city might be built
	 * @return true if the city can be built, false otherwise.
	 */
	public boolean canBuildCity(int player, VertexLocation loc){
		// It is the player's turn.
		if(!getTurnTracker().isPlayerTurn(player)) {
			return false;
		}

		// Turn status is PLAYING
		if(getTurnTracker().getStatus() != TurnStatus.PLAYING) {
			return false;
		}

		// The targeted location contains a settlement belonging to the player.
		if(!getMap().settlementBelongsToPlayer(player, loc)) {
			return false;
		}

		// The player has: 3 or more ore, 2 or more grain.
		if(getPlayer(player).getResources().getOre() < 3 ||
				getPlayer(player).getResources().getWheat() < 2) {
			return false;
		}

		return true;
	}

	/**
	 * Decides if a player can offer a trade.
	 * @pre none
	 * @post The player can offer the specified trade only if the following conditions are met: <br />
	 * <ol>
	 *	 <li>It is the player's turn.</li>
	 *	 <li>Turn status is PLAYING.</li>
	 *	 <li>The player possesses the resources that he wishes to trade.</li>
	 * </ol>
	 * @param player Player the player who wants to offer the trade.
	 * @param trade ResourceList the resources that the player wishes to trade.
	 * @return true if the player can offer the trade, false otherwise.
	 */
	public boolean canOfferTrade(int player, ResourceList trade){
		if(!isTurnAndStatus(player, TurnStatus.PLAYING))
			return false;

		if(getPlayer(player).getResources().getSheep() < trade.getSheep() ||
				getPlayer(player).getResources().getWheat() < trade.getWheat() ||
				getPlayer(player).getResources().getBrick() < trade.getBrick() ||
				getPlayer(player).getResources().getOre() < trade.getOre() ||
				getPlayer(player).getResources().getWood() < trade.getWood()) {
			return false;
		}

		return true;
	}

	/**
	 * Decides if a player can accept a trade.
	 * @pre none
	 * @post Returns true when the following conditions are met:
	 *	   <ol>
	 *		   <li>It is the player's turn.</li>
	 *		   <li>The TurnTracker status is set to PLAYING.</li>
	 *		   <li>The player owns the necessary resources to complete the trade.</li>
	 *	   </ol>
	 * @param player
	 * @param trade
	 * @return
	 */
	public boolean canAcceptTrade(int player, ResourceList trade){

		// the player has the necessary resources
		if(getPlayer(player).getResources().getSheep() < trade.getSheep() ||
				getPlayer(player).getResources().getWheat() < trade.getWheat() ||
				getPlayer(player).getResources().getBrick() < trade.getBrick() ||
				getPlayer(player).getResources().getOre() < trade.getOre() ||
				getPlayer(player).getResources().getWood() < trade.getWood()) {
			return false;
		}

		return true;
	}

	/**
	 * Decides if a player can offer a maritime trade.
	 * @pre none
	 * @post The player can offer the specified trade only if the following conditions are met: <br />
	 * <ol>
	 *	 <li>The player possesses the resources that he wishes to trade.</li>
	 * </ol>
	 * @param player Player the player who wants to offer the trade.
	 * @param trade ResourceList the resources that the player wishes to trade.
	 * @return true if the player can offer the trade, false otherwise.
	 */
	public boolean canMaritimeTrade(int player, ResourceList trade){
        if(isTurnAndStatus(player, TurnStatus.PLAYING)) {
            return true;
        } else {
            return false;
        }
	}

	/**
	 * Decides if a player can finish his turn.
	 * @pre none
	 * @post no attributes are changed anywhere
	 * @post returns true if all conditions for finishing the game are met. <br />
	 * These are the conditions: <br />
	 * <ol>
	 *	 <li>It is currently the player's turn.</li>
	 *	 <li>Turn status is PLAYING</li>
	 * </ol>
	 * @param player int the index of the player in question
	 * @return boolean True if the player can finish his turn, false otherwise
	 */
	public boolean canFinishTurn(int player){
		return isTurnAndStatus(player, TurnStatus.PLAYING);
	}

	/**
	 * Decides if a player can buy a Development Card.
	 * @pre none
	 * @post no attributes are changed anywhere
	 * @post returns true if all conditions for buying a dev card are me. <br />
	 * These are the conditions: <br />
	 * <ol>
	 *	 <li>It is currently the player's turn.</li>
	 *	 <li>Turn status is PLAYING.</li>
	 *	 <li>The player has at least 1 of each: ore, sheep, wheat</li>
	 *	 <li>The Development Card supply is not empty.</li>
	 * </ol>
	 * @param player int the index of the player in question
	 * @return boolean True if the player can buy a Dev Card, false otherwise
	 */
	public boolean canBuyDevCard(int player){
		if(!isTurnAndStatus(player, TurnStatus.PLAYING))
			return false;

		if(getPlayer(player).getResources().getOre() < 1 ||
				getPlayer(player).getResources().getSheep() < 1 ||
				getPlayer(player).getResources().getWheat() < 1)
			return false;

		if(getDeck().isEmpty())
			return false;

		return true;
	}

	/**
	 * Decides if a player can use a Year of Plenty progress card.
	 * @pre none
	 * @post no attributes are changed anywhere
	 * @post returns true if all conditions for playing a Year of Plenty card are met. <br />
	 * These are the conditions: <br />
	 * <ol>
	 *	 <li>It is currently the player's turn.</li>
	 *	 <li>It is currently the Build Phase.</li>
	 *	 <li>The player has a Year of Plenty card.</li>
	 *	 <li>The player has not yet played a Development Card this turn.</li>
	 * </ol>
	 * @param player int the index of the player in question
	 * @return boolean True if the player can use a Year of Plenty card, false otherwise
	 */
	public boolean canUseYearOfPlenty(int player){
		if(!isTurnAndStatus(player, TurnStatus.PLAYING))
			return false;
		if(getPlayer(player).getOldDevCards().getYearOfPlenty()<1)
			return false;
		if(getPlayer(player).hasPlayedDevCard())
			return false;
		return true;
	}

	/**
	 * Decides if a player can use a Road Building progress card.
	 * @pre none
	 * @post no attributes are changed anywhere
	 * @post returns true if all conditions for playing a Road Building card are met. <br />
	 * These are the conditions: <br />
	 * <ol>
	 *	 <li>It is currently the player's turn.</li>
	 *	 <li>It is currently the Build Phase.</li>
	 *	 <li>The player has a Road Building card.</li>
	 *	 <li>The player has not yet played a Development Card this turn.</li>
	 * </ol>
	 * @param player int the index of the player in question
	 * @return boolean True if the player can use a Road Building card, false otherwise
	 */
	public boolean canUseRoadBuilder(int player){
		if(!isTurnAndStatus(player, TurnStatus.PLAYING)) {
			return false;
		}
		if(getPlayer(player).getOldDevCards().getRoadBuilding()<1) {
			return false;
		}
		if(getPlayer(player).hasPlayedDevCard()) {
			return false;
		}
		if(getPlayer(player).getRoads() < 2) {
			return false;
		}
		return true;

	}

	/**
	 * Decides if a player can use a Soldier (also called Knight) development card.
	 * @pre none
	 * @post no attributes are changed anywhere
	 * @post returns true if all conditions for playing a Soldier card are met. <br />
	 * These are the conditions: <br />
	 * <ol>
	 *	 <li>It is currently the player's turn.</li>
	 *	 <li>The player has a Soldier card.</li>
	 *	 <li>The player has not yet played a Development Card this turn.</li>
	 *	 <li>The player did not acquire the Soldier card this turn.</li>
	 * </ol>
	 * @param player int the index of the player in question
	 * @return boolean True if the player can use a Soldier card, false otherwise
	 */
	public boolean canUseSoldier(int player){
		isTurnAndStatus(player, TurnStatus.PLAYING);

		if(getPlayer(player).getOldDevCards().getSoldier()<1)
			return false;

		// the player has not yet played a Development Card this turn.
		if(getPlayer(player).hasPlayedDevCard())
			return false;

		return true;
	}

	/**
	 * Decides if a player can use a Soldier (also called Knight) development card.
	 * @pre none
	 * @post no attributes are changed anywhere
	 * @post returns true if all conditions for playing a Soldier card are met. <br />
	 * These are the conditions: <br />
	 * <ol>
	 *	 <li>It is currently the player's turn.</li>
	 *	 <li>It is currently the Build Phase.</li>
	 *	 <li>The player has a Soldier card.</li>
	 *	 <li>The player has not yet played a Development Card this turn.</li>
	 * </ol>
	 * @param player int the index of the player in question
	 * @return boolean True if the player can use a Soldier card, false otherwise
	 */
	public boolean canUseMonopoly(int player){
		if(!isTurnAndStatus(player, TurnStatus.PLAYING))
			return false;
		if(getPlayer(player).getOldDevCards().getMonopoly()<1)
			return false;
		if(getPlayer(player).hasPlayedDevCard())
			return false;
		return true;
	}

	/**
	 * Decides if a player can use a Monument card.
	 * @pre none
	 * @post no attributes are changed anywhere
	 * @post returns true if all conditions for playing a Monument card are met. <br />
	 * These are the conditions: <br />
	 * <ol>
	 *	 <li>It is currently the player's turn.</li>
	 *	 <li>It is currently the Build Phase.</li>
	 *	 <li>The player has a Monument card.</li>
	 *	 <li>The player has not yet played a Development Card this turn.</li>
	 * </ol>
	 * @param player int the index of the player in question
	 * @return boolean True if the player can use a Monument card, false otherwise
	 */
	public boolean canUseMonument(int player){
		if(!isTurnAndStatus(player, TurnStatus.PLAYING))
			return false;
		if(getPlayer(player).getOldDevCards().getMonument()<1)
			return false;
		//if(getPlayer(player).hasPlayedDevCard())
		//	return false;
		return true;
	}

	/**
	 * Decides if a player can place the robber.
	 * @pre none
	 * @post The following conditions for placing the robber must be met:
	 * <ol>
	 *	 <li>It is the player's turn.</li>
	 *	 <li>The player's turn status is ROBBING.</li>
	 *	 <li>The robber must be placed valid hex location.</li>
	 *	 <li>The robber must be placed on land.</li>
	 * </ol>
	 * @param player int the index of the player in question
	 * @return true if the player can place the robber, false otherwise
	 */
	public boolean canPlaceRobber(int player, HexLocation loc){
		// it is the player's turn
		// the turn status is ROBBING
//		if(!isTurnAndStatus(player, TurnStatus.ROBBING)) {
//			return false;
//		}

		//  the location is on land and is valid
		if(!getMap().isValidHex(loc)) {
			return false;
		}
		if(getMap().isWaterHex(loc)) {
			return false;
		}

		if(getMap().getRobber().equals(loc)) {
			return false;
		}

		return true;
	}

	/**
	 *
	 * @param player
	 * @param stat
	 * @return
	 */
	public boolean isTurnAndStatus(int player, TurnStatus stat){
		// It is the player's turn.
		if(!getTurnTracker().isPlayerTurn(player)) {
			return false;
		}

		// Turn status is PLAYING
		if(getTurnTracker().getStatus() != stat) {
			return false;
		}

		return true;
	}

	/**
	 * @param player
	 * @return
	 */
	public boolean isTurn(int player){
		// It is the player's turn.
		if(!getTurnTracker().isPlayerTurn(player)) {
			return false;
		}

		return true;
	}

	/**
	 * Gets the list of resources for a bank.
	 * @return a ResourceList for a bank.
	 */
	public ResourceList getBank() {
		return bank;
	}

	/**
	 * Sets the list of resources for a bank.
	 * @param bank ResourceList
	 */
	public void setBank(ResourceList bank) {
		this.bank = bank;
	}


	/**
	 * Gets the chat.
	 * @return a MessageList.
	 */
	public MessageList getChat() {
		return chat;
	}

	public List<LogEntry> getChatForView() {
		//get color of player from name 
		//(necessary because the server sends the name, the view expects the color... I didn't design it)
		List<LogEntry> messages = new LinkedList<>();
		if(chat == null) {
			return null;
		}
		for(MessageLine line : chat.getLines()) {
			CatanColor color = getPlayerColorFromName(line.getSource());
			String message = line.getMessage();
			LogEntry temp = new LogEntry(color,message);
			messages.add(temp);
		}
		
		return messages;
	}
	
	private CatanColor getPlayerColorFromName(String playerName) {
		for(Player player : players) {
			if(player.getName().equals(playerName)) {
				return player.getColor();
			}
		}
		return null;
	}

	/**
	 * Sets the chat.
	 * @param chat a MessageList.
	 */
	public void setChat(MessageList chat) {
		this.chat = chat;
	}

	/**
	 * Gets the log.
	 * @return a MessageList.
	 */
	public MessageList getLog() {
		return log;
	}

	public List<LogEntry> getGameHistory() {
		return gameHistory;
	}

	/**
	 * Sets the log and the gameHistory
	 * @param log a MessageList.
	 */
	public void setLog(MessageList log) {
		this.log = log;
		this.gameHistory.clear();
		if(log.getLines() != null) {
			for (MessageLine line : log.getLines()) {
				String src = line.getSource();
				Player srcPlayer = (Player) this.playerMap.get(src);
				LogEntry entry = new LogEntry(srcPlayer.getColor(), line.getMessage());
				this.gameHistory.add(entry);
			}
		}
	}

	public void addLog(MessageLine line) {
		log.addLine(line);
	}

	/**
	 * Gets the GameMap.
	 * @return a GameMap.
	 */
	public GameMap getMap() {
		return map;
	}

	/**
	 * Sets the GameMap.
	 * @param map a GameMap.
	 */
	public void setMap(GameMap map) {
		this.map = map;
	}

	/**
	 * Gets the list of players.
	 * @return the list of players.
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Gets the player by their index
	 * @param player the player in question
	 * @return Player
	 */
	public Player getPlayer(int player){
		return getPlayers().get(player);
	}

	public Player getPlayerById(int playerId) {
		for (Player daPlaya : players) {
			if (daPlaya.getPlayerID() == playerId) {
				return daPlaya;
			}
		}
		return null;
	}

	/**
	 * Sets the list of players.
	 * @param players the list of players.
	 */
	public void setPlayers(List<Player> players) {
		this.players = new ArrayList<>();
		this.playerMap = new HashMap<>();
		for (Player player : players) {
			if (player != null) {
				this.players.add(player);
				this.playerMap.put(player.getName(), player);
				if (player.getPlayerID() == ProxyFacade.getInstance().getLocalPlayer().getId()) {
					ProxyFacade.getInstance().getLocalPlayer().setPlayerIndex(player.getPlayerIndex());
				}
			}
		}
	}

    public void simpleSetPlayers(List<Player> players){
        this.players = players;
    }

	/**
	 * Gets the trade offer.
	 * @return a TradeOffer.
	 */
	public TradeOffer getTradeOffer() {
		return tradeOffer;
	}

	/**
	 * Sets the trade offer.
	 * @param tradeOffer a TradeOffer.
	 */
	public void setTradeOffer(TradeOffer tradeOffer) {
		this.tradeOffer = tradeOffer;
	}

	/**
	 * Gets the turn tracker.
	 * @return a TurnTracker.
	 */
	public TurnTracker getTurnTracker() {
		return turnTracker;
	}

	/**
	 * Sets the turn tracker.
	 * @param turnTracker a TurnTracker.
	 */
	public void setTurnTracker(TurnTracker turnTracker) {
		this.turnTracker = turnTracker;
	}

	/**
	 * Gets the deck.
	 * @return a DevCardList deck.
	 */
	public DevCardList getDeck() {
		return deck;
	}

	/**
	 * Sets the deck.
	 * @param deck a DevCardList.
	 */
	public void setDeck(DevCardList deck) {
		this.deck = deck;
	}

	/**
	 * Gets the version.
	 * @return an int version.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * @param version gets an int version.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Gets the winner.
	 * @return an int winner.
	 */
	public int getWinner() {
		return winner;
	}

	/**
	 * Sets the winner.
	 * @param winner gets an int winner.
	 */
	public void setWinner(int winner) {
		this.winner = winner;
	}

	/**
	 * Gets all robbable players that are adjacent to a hexLoc
	 * @param hexLoc HexLocation
	 * @return RobPlayerInfo
	 */
	public RobPlayerInfo[] getRobbablePlayers(HexLocation hexLoc) {
		Set<RobPlayerInfo> tempPlayers = new HashSet<>();
		PlayerInfo[] playerInfoArray = getPlayerInfoArray();

		for(VertexObject vo : getAllAdjacentVertexObjects(hexLoc)){
			// check that you're not trying to rob yourself
			if(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex() != vo.getOwner()){
				tempPlayers.add(new RobPlayerInfo(playerInfoArray[vo.getOwner()]));
			}
		}

		if (tempPlayers.size() == 0) {
			tempPlayers.add(new RobPlayerInfo("None", CatanColor.WHITE, 0));
		}

		return tempPlayers.toArray(new RobPlayerInfo[tempPlayers.size()]);
	}

	private Set<VertexObject> getAllAdjacentVertexObjects(HexLocation hexLoc) {
		return getMap().getAllAdjacentVertexObjects(hexLoc);
	}

	public Set<PortType> getPlayerPorts() {

		// get necessary elements
		int playerInd = getTurnTracker().getCurrentTurn();
		List<Port> allPorts = getMap().getPorts();

		// combine cities and settlements into a single list
		List<VertexObject> allCitiesAndSetts = new ArrayList<>();
		allCitiesAndSetts.addAll(getMap().getCities());
		allCitiesAndSetts.addAll(getMap().getSettlements());

		// set up ownedPorts
		Set<PortType> ownedPorts = new HashSet<>();

		// check each city to see if it's a port and the player owns it
		for(VertexObject citySett : allCitiesAndSetts) {
			// if the player owns the city
			if (citySett.getOwner() == playerInd) {
				// check each port to see if the city is a port
				for (Port port : allPorts) {
					// get the cities that touch the edge that the port is on
					VertexLocation[] portVertices = getMap().getAdjacentVertices(
							new EdgeLocation(port.getLocation(), port.getDirection()));

					// check if the city is in the same position/direction as the port
					if (citySett.getLocation().getNormalizedLocation().equals(portVertices[0]) ||
							citySett.getLocation().getNormalizedLocation().equals(portVertices[1])){
						// we now know that the player owns a port.

						if(port.getType() == PortType.WOOD){
							ownedPorts.add(PortType.WOOD);
						}
						if(port.getType() == PortType.WHEAT){
							ownedPorts.add(PortType.WHEAT);
						}
						if(port.getType() == PortType.SHEEP){
							ownedPorts.add(PortType.SHEEP);
						}
						if(port.getType() == PortType.BRICK){
							ownedPorts.add(PortType.BRICK);
						}
						if(port.getType() == PortType.ORE){
							ownedPorts.add(PortType.ORE);
						}
						if(port.getType() == PortType.THREE){
							ownedPorts.add(PortType.THREE);
						}
					}
				}
			}
		}

		return ownedPorts;
	}

	/**
	 * Figures out how many resources players are due after a roll.
	 * Adds the resources to each player.
	 * @param roll
	 */
	public void updatePlayerResources(int roll){
		Map<Integer, ResourceList> resources = new HashMap<>();
		//initialize all players to 0 resources
		for(Player p : getPlayers()){
			resources.put(p.getPlayerIndex(), new ResourceList(0,0,0,0,0));
		}

		// get resources due to each player
		getMap().getResourcesDue(roll, resources);

		// actually increase player resources
		for(Player p : getPlayers()){
			p.getResources().increase(resources.get(p.getPlayerIndex()));
            bank.reduce(resources.get(p.getPlayerIndex()));
		}
	}

	/**
	 * Figures out how many resources a player is due after a roll.
	 * Adds the resources to each player.
	 */
	public void updatePlayerResources(Player player, VertexObject vo){
		ResourceList resources = getMap().getResourcesDue(vo);
		player.getResources().increase(resources);
	}


}