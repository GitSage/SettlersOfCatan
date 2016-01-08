package server.facade;

import java.util.Set;
import java.util.logging.Logger;

import server.Server;
import server.commands.moves.*;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.HexLocation;
import shared.model.GameState;
import shared.model.Player;
import shared.model.ResourceList;
import shared.model.VertexObject;
import shared.request.move.*;

/**
 * This class is the go between for the server moves handlers and the command classes.
 * The server handlers call this class and this class creates command classes, executes the methods, and then returns the info.
 */
public class MovesFacade {

    private int numAlreadyDiscarded = 0;
    private int numNeedToDiscard = -1;

	public MovesFacade() {
		super();
        numAlreadyDiscarded = 0;
        numNeedToDiscard = -1;
		Logger.getLogger("catanserver").finest("In moves facade model method");
	}

	/*******************************/
	/**Internal validation methods**/
	/*******************************/
	
	private boolean validatePlayerIndex(int playerId) {

		Logger.getLogger("catanserver").finer("In moves facade validate player index method");
		if(playerId < 0 || playerId > 3) {
			Logger.getLogger("catanserver").fine("player index not valid");
			return false;
		}
		else {
			Logger.getLogger("catanserver").fine("player index valid");
			return true;
		}
	}
	
	private boolean validatePlayerTurn(int gameId, int playerId) {
		Logger.getLogger("catanserver").finer("In moves facade validate player turn method");
		if(Server.state.getGame(gameId).isTurn(playerId)) {
			Logger.getLogger("catanserver").fine("It is the players turn");
			return true;
		}
		else {
			Logger.getLogger("catanserver").fine("It is not the players turn");
			return false;
		}
	}
	
	private boolean locationNextToPlayerSettlementCity(int victimIndex, HexLocation location, int gameId) {
		
		//not really robbing anyone, just moving the robber
		if(victimIndex < 0 || victimIndex > 3) {
			return true;
		}
		
		Logger.getLogger("catanserver").finer("In moves facade location next to player settlement city method");
		Set<VertexObject> vertexLocations = Server.state.getGame(gameId).getMap().getAllAdjacentVertexObjects(location);
		for(VertexObject vo : vertexLocations) {
			Logger.getLogger("catanserver").fine("Looking at vertex Location: " + vo.toString());
			if(vo.getOwner() == victimIndex) {
				Logger.getLogger("catanserver").fine("vertex location is owned by player");
				return true;
			}
		}
		Logger.getLogger("catanserver").fine("vertex location owned by player not found");
		return false;
	}

	//this means the port is 2-1
	private boolean validatePlayerHasPort(int gameId,ResourceType inputResource) {
		Logger.getLogger("catanserver").finer("In moves facade validate player has port (2-1)");
		PortType requestedPort = resourceTypeToPort(inputResource);
		
		Set<PortType> ports = Server.state.getGame(gameId).getPlayerPorts();
		for(PortType port : ports) {
			if(port == requestedPort) {
				Logger.getLogger("catanserver").fine("player has the correct 2-1 port");
				return true;
			}
		}
		Logger.getLogger("catanserver").fine("player does not have the correct 2-1 port");
		return false;
	}

	private PortType resourceTypeToPort(ResourceType inputResource) {
		switch(inputResource) {
			case WOOD :
				return PortType.WOOD;
			case BRICK :
				return PortType.BRICK;
			case ORE :
				return PortType.ORE;
			case WHEAT :
				return PortType.WHEAT;
			case SHEEP :
				return PortType.SHEEP;
			default :
				return null;
		}
	}

	//these means the port is 3-1
	private boolean validatePlayerHasPort(int gameId) {
		Logger.getLogger("catanserver").finer("In moves facade validate player has port (3-1)");
		
		Set<PortType> ports = Server.state.getGame(gameId).getPlayerPorts();
		for(PortType port : ports) {
			if(port == PortType.THREE) {
				Logger.getLogger("catanserver").fine("player has 3-1 port");
				return true;
			}
		}
		Logger.getLogger("catanserver").fine("player does not have 3-1 port");
		return false;
	}
	
	private boolean isValidTrade(MaritimeTradeRequest request,int gameId) {
		Logger.getLogger("catanserver").finer("In moves facade is valid trade method");
		
		//validate player has resources
		ResourceList playerResources = Server.state.getGame(gameId).getPlayer(request.getPlayerIndex()).getResources();
		if(playerResources.getResource(request.getInputResource()) < request.getRatio()) {
			Logger.getLogger("catanserver").fine("player does not have correct resources");
			return false;
		}

		//validate bank has resource
		ResourceList bank = Server.state.getGame(gameId).getBank();
		if(bank.getResource(request.getOutputResource()) < 1) {
			Logger.getLogger("catanserver").fine("bank does not have the resource");
			return false;
		}
		
		//validate ratio
		if(request.getRatio() == 3) {
			if(!validatePlayerHasPort(gameId)) {
				return false;
			}
		}
		else if (request.getRatio() == 2) {
			if(!validatePlayerHasPort(gameId,request.getInputResource())) {
				return false;
			}
		}
				
		return true;
	}
	
	/*************************/
	/**Public Moves Commands**/
	/*************************/
	
	/**
	 * Sends a chat message
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState sendChat(int gameId, SendChatRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade send chat");
		
		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			Logger.getLogger("catanserver").finer("Invalid player index: " + request.getPlayerIndex());
			return null;
		}
		
		if(request.getContent().length() > 1000) {
			Logger.getLogger("catanserver").finer("Chat too long: " + request.getContent().length());
			return null;
		}
		
		SendChatCommand command = new SendChatCommand(gameId, request);
		command.execute();
		
		return Server.state.getGames().get(gameId);
	}
	
	/**
	 * Used to roll a number a the beginning of your turn.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState rollNumber(int gameId, RollNumberRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade roll number");
		
		//validate dice number
		if(request.getNumber() < 1 || request.getNumber() > 12) {
			return null;
		}
		
		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}
		
		RollNumberCommand command = new RollNumberCommand(gameId, request);
		command.execute();
		
		return Server.state.getGames().get(gameId);
	}
	
	/**
	 * Moves the robber, selecting the new robber position and player to rob.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState robPlayer(int gameId, RobPlayerRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade rob player");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			Logger.getLogger("catanserver").fine("not the players turn!");
			return null;
		}
		
		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			Logger.getLogger("catanserver").fine("player's index is invalid!");
			return null;
		}

		//Use the gameStates validation method
		if(!Server.state.getGame(gameId).canPlaceRobber(request.getPlayerIndex(), request.getLocation())) {
			Logger.getLogger("catanserver").fine("can do validation failed!");
			return null;
		}
			
		if(!locationNextToPlayerSettlementCity(request.getVictimIndex(),request.getLocation(),gameId)) {
			Logger.getLogger("catanserver").fine("locationNextToPlayerSettlementCity returned false");
			return null;
		}
		
		//execute command and return serverState
		RobPlayerCommand command = new RobPlayerCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}

	/**
	 * Used to finish your turn.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState finishTurn(int gameId, FinishTurnRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade finish turn");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}
		
		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		FinishTurnCommand command = new FinishTurnCommand(gameId,request);
		command.execute();

		return Server.state.getGame(gameId);
	}
	
	/**
	 * Used to buy a development card.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState buyDevCard(int gameId, BuyDevCardRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade buy dev card");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}
		
		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}

		//GameState validation... Oh yeah
		if(!Server.state.getGame(gameId).canBuyDevCard(request.getPlayerIndex())) {
			return null;
		}
		
		//only let them buy a dev card if the deck is not empty - but don't return null if it is empty because that isn't a bad request
		if(!Server.state.getGame(gameId).getDeck().isEmpty()) {
			BuyDevCardCommand command = new BuyDevCardCommand(gameId,request);
			command.execute();
		}
				
		return Server.state.getGame(gameId);
	}
	
	/**
	 * Plays a 'Year of Plenty' card from your hand to gain the two specified resources.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState yearOfPlenty(int gameId, YearOfPlentyCardRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade year of plenty");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		if(!Server.state.getGame(gameId).canUseYearOfPlenty(request.getPlayerIndex())) {
			return null;
		}
		
		//Check that the bank has the requested resources
		ResourceList bank = Server.state.getGame(gameId).getBank();
		if( (bank.getResource(request.getResource1()) < 1) || (bank.getResource(request.getResource2()) < 1) ) {
			return null;
		}
		
		YearOfPlentyCommand command = new YearOfPlentyCommand(gameId,request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}
	
	/**
	 * Plays a 'Road Building' card from your hand to build two roads at the specified locations.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState roadBuilding(int gameId, RoadBuildingCardRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade road building");
		
		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		if(!Server.state.getGame(gameId).canUseRoadBuilder(request.getPlayerIndex())) {
			return null;
		}

		RoadBuildingCardCommand command = new RoadBuildingCardCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}

	/**
	 * Plays a 'Soldier' from your hand, selecting the new robber position and player to rob.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState soldier(int gameId, SoldierCardRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade soldier");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		if(!Server.state.getGame(gameId).canUseSoldier(request.getPlayerIndex())) {
			return null;
		}

		SoldierCardCommand command = new SoldierCardCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}

	/**
	 * Plays a 'Monopoly' card from your hand to monopolize the specified resource.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState monopoly(int gameId, MonopolyCardRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade monopoly");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		if(!Server.state.getGame(gameId).canUseMonopoly(request.getPlayerIndex())) {
			return null;
		}

		MonopolyCardCommand command = new MonopolyCardCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}

	/**
	 * Plays a 'Monument' card from your hand to give you a victory point.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState monument(int gameId, MonumentCardRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade monument");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		if(!Server.state.getGame(gameId).canUseMonument(request.getPlayerIndex())) {
			return null;
		}

		MonumentCardCommand command = new MonumentCardCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}
	
	/**
	 * POST /moves/buildRoad Builds a road at the specified location. 
	 * @param request the object containing the request data (Set 'free' to true during initial setup.)
	 * @return
	 */
	public GameState buildRoad(int gameId, BuildRoadRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade build road");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		if(!Server.state.getGame(gameId).canBuildRoad(request.getPlayerIndex(),request.getRoadLocation())) {
			return null;
		}

		BuildRoadCommand command = new BuildRoadCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}

	/**
	 * Builds a settlement at the specified location.
	 * @param request the object containing the request data (Set 'free' to true during initial setup.)
	 * @return
	 */
	public GameState buildSettlement(int gameId, BuildSettlementRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade build settlement");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		if(!Server.state.getGame(gameId).canBuildSettlement(request.getPlayerIndex(),request.getVertexLocation())) {
			return null;
		}

		BuildSettlementCommand command = new BuildSettlementCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}

	/**
	 * Builds a city at the specified location.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState buildCity(int gameId, BuildCityRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade build city");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		if(!Server.state.getGame(gameId).canBuildCity(request.getPlayerIndex(),request.getVertexLocation())) {
			return null;
		}

		BuildCityCommand command = new BuildCityCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}

	/**
	 * Offers a domestic trade to another player.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState offerTrade(int gameId, OfferTradeRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade offer trade");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		if(!Server.state.getGame(gameId).canOfferTrade(request.getPlayerIndex(),request.getOffer())) {
			return null;
		}

		OfferTradeCommand command = new OfferTradeCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}

	/**
	 * Used to accept or reject a trade offered to you.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState acceptTrade(int gameId, AcceptTradeRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade accept trade");

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		ResourceList playerResources = Server.state.getGame(gameId).getPlayer(request.getPlayerIndex()).getResources();
		if(!Server.state.getGame(gameId).canAcceptTrade(request.getPlayerIndex(),playerResources)) {
			return null;
		}

		AcceptTradeCommand command = new AcceptTradeCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}

	/**
	 * Used to execute a maritime trade.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState maritimeTrade(int gameId, MaritimeTradeRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade maritime trade");

		//validate it is the player's turn
		if(!validatePlayerTurn(gameId,request.getPlayerIndex())) {
			return null;
		}

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		if(!isValidTrade(request,gameId)) {
			return null;
		}

		MaritimeTradeCommand command = new MaritimeTradeCommand(gameId, request);
		command.execute();
		
		return Server.state.getGame(gameId);
	}

	/**
	 * Discards the specified resource cards.
	 * @param request the object containing the request data
	 * @return
	 */
	public GameState discardCard(int gameId, DiscardRequest request) {
		Logger.getLogger("catanserver").finer("In moves facade discard cards");

        // if this is the first discard, figure out how many people are supposed to be discarding
        if(numNeedToDiscard == -1){
            numNeedToDiscard = 0;
            for(Player p : Server.state.getGame(gameId).getPlayers()){
                if(p.getTotalNumCards() > 7){
                    numNeedToDiscard++;
                }
            }
        }

		//validate playerIndex
		if(!validatePlayerIndex(request.getPlayerIndex())) {
			return null;
		}
		
		//canDo validation baby
		if(!Server.state.getGame(gameId).canDiscardCards(request.getPlayerIndex())) {
			return null;
		}

        // check if everyone has discarded already
        boolean lastDiscarder = false;
        numAlreadyDiscarded++;
        if(numAlreadyDiscarded == numNeedToDiscard){
            // everyone has discarded, leave discarding mode
            lastDiscarder = true;
            numAlreadyDiscarded = 0;
            numNeedToDiscard = -1;
        }

        // actually execute the command
		DiscardCardsCommand command = new DiscardCardsCommand(gameId, request, lastDiscarder);
		command.execute();

		return Server.state.getGame(gameId);
	}
}
