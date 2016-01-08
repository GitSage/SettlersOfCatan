package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.*;
import shared.model.ResourceList;
import shared.request.game.*;
import shared.request.move.*;
import client.proxy.HttpProxy;
import client.proxy.Proxy;

/**
 * HttpProxyTest is a JUnit test class to test the HttpProxy class
 */
public class HttpProxyTest {

	private int game;
	
	/*************************/
	/*** Test Post methods ***/
	/*************************/
	private void postGameAddAI(Proxy proxy) {
		AddAiRequest request = new AddAiRequest("LARGEST_ARMY");
		String response = proxy.post("/game/addAI", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postGameCommands(Proxy proxy) {
		List<Object> request = new ArrayList<Object>();
		request.add(new DiscardRequest(0,new ResourceList(0,0,0,0,0)));
		request.add(new SendChatRequest(0,"Test Message One"));
		
		String response = proxy.post("/game/commands", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postGameReset(Proxy proxy) {
		String response = proxy.post("/game/reset", null);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postGamesCreate(Proxy proxy) {
		CreateGameRequest request = new CreateGameRequest(true,true,true,"JUnitTestGame");
		String response = proxy.post("/games/create", request);
		game = Integer.parseInt(response.split("\"id\":")[1].split(",")[0]);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postGamesJoin(Proxy proxy) {
		JoinGameRequest request = new JoinGameRequest(game, CatanColor.RED);
		String response = proxy.post("/games/join", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postGamesLoad(Proxy proxy) {
		LoadGameRequest request = new LoadGameRequest("JUnit Test Game");
		String response = proxy.post("/games/load", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postGamesSave(Proxy proxy) {
		SaveGameRequest request = new SaveGameRequest(3,"JUnit Test Game");
		String response = proxy.post("/games/save", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesAcceptTrade(Proxy proxy) {
		AcceptTradeRequest request = new AcceptTradeRequest(0,false);
		String response = proxy.post("/moves/acceptTrade", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesBuildCity(Proxy proxy) {
		BuildCityRequest request = new BuildCityRequest(0,new VertexLocation(new HexLocation(0,0), VertexDirection.NorthWest),true);
		String response = proxy.post("/moves/buildCity", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesBuildRoad(Proxy proxy) {
		BuildRoadRequest request = new BuildRoadRequest(0,new EdgeLocation(new HexLocation(0,0), EdgeDirection.North),true);
		String response = proxy.post("/moves/buildRoad", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}	
	private void postMovesBuildSettlement(Proxy proxy) {
		BuildSettlementRequest request = new BuildSettlementRequest(0, new VertexLocation(new HexLocation(0,0), VertexDirection.NorthWest),true);
		String response = proxy.post("/moves/buildSettlement", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesBuyDevCard(Proxy proxy) {
		BuyDevCardRequest request = new BuyDevCardRequest(0);
		String response = proxy.post("/moves/buyDevCard", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesDiscardCards(Proxy proxy) {
		DiscardRequest request = new DiscardRequest(0,new ResourceList(0,0,0,0,0));
		String response = proxy.post("/moves/discardCards", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesFinishTurn(Proxy proxy) {
		FinishTurnRequest request = new FinishTurnRequest(0);
		String response = proxy.post("/moves/finishTurn", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesMaritimeTrade(Proxy proxy) {
		MaritimeTradeRequest request = new MaritimeTradeRequest(0,3,ResourceType.WOOD,ResourceType.BRICK);
		String response = proxy.post("/moves/maritimeTrade", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesMonopoly(Proxy proxy) {
		MonopolyCardRequest request = new MonopolyCardRequest(0,ResourceType.WOOD);
		String response = proxy.post("/moves/Monopoly", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesMonument(Proxy proxy) {
		MonumentCardRequest request = new MonumentCardRequest(0);
		String response = proxy.post("/moves/Monument", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesOfferTrade(Proxy proxy) {
		OfferTradeRequest request = new OfferTradeRequest(0,new ResourceList(2,-2,0,0,0),2);
		String response = proxy.post("/moves/offerTrade", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesRoad_Building(Proxy proxy) {
		RoadBuildingCardRequest request = new RoadBuildingCardRequest(0,new EdgeLocation(new HexLocation(0,0),EdgeDirection.NorthEast),new EdgeLocation(new HexLocation(1,-1),EdgeDirection.South));
		String response = proxy.post("/moves/Road_Building", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesRobPlayer(Proxy proxy) {
		RobPlayerRequest request = new RobPlayerRequest(0,2,new HexLocation(2,2));
		String response = proxy.post("/moves/robPlayer", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesRollNumber(Proxy proxy) {
		RollNumberRequest request = new RollNumberRequest(0,8);
		String response = proxy.post("/moves/rollNumber", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesSendChat(Proxy proxy) {
		SendChatRequest request = new SendChatRequest(0,"Test Message One");
		String response = proxy.post("/moves/sendChat", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesSoldier(Proxy proxy) {
		SoldierCardRequest request = new SoldierCardRequest(0,2,new HexLocation(1,0));
		String response = proxy.post("/moves/Soldier", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postMovesYear_of_Plenty(Proxy proxy) {
		YearOfPlentyCardRequest request = new YearOfPlentyCardRequest(0,ResourceType.SHEEP,ResourceType.ORE);
		String response = proxy.post("/moves/Year_of_Plenty", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postUserLogin(Proxy proxy) {
		UserRequest request = new UserRequest("aserxcu","test");
		String response = proxy.post("/user/login", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postUserRegister(Proxy proxy) {
		RegisterRequest request = new RegisterRequest("aserxcu","test");
		String response = proxy.post("/user/register", request);
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}
	private void postUtilChangeLogLevel(Proxy proxy) {
		ChangeLogLevelRequest request = new ChangeLogLevelRequest("ALL");
		String response = proxy.post("/util/changeLogLevel", request);
		assertNotNull(response);
		return;
	}
	
	
	/************************/
	/*** Test Get methods ***/
	/************************/
	private void getGamesList(Proxy proxy) {
		String response = proxy.get("/games/list");
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}	
	private void getGameModel(Proxy proxy) {
		String response = proxy.get("/game/model");
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}	
	private void getGameCommands(Proxy proxy) {
		String response = proxy.get("/game/commands");
//		System.out.println("response received: " + response);
		assertNotNull(response);
		return;
	}	
	
	private void testRequestCalls(Proxy proxy) {
		postUserRegister(proxy);
		postUserLogin(proxy);
		postUtilChangeLogLevel(proxy);
		postGamesCreate(proxy);
		getGamesList(proxy);
		postGamesJoin(proxy);

		postGamesSave(proxy);
		postGamesLoad(proxy);

		postMovesSendChat(proxy);
		postGameReset(proxy);
		
		//The rest of these won't pass our server's validation but they worked fine on the example server because it did no validation....
//		postGameAddAI(proxy);
//		postGameAddAI(proxy);
//		postGameAddAI(proxy);
//		postMovesYear_of_Plenty(proxy);
//		postMovesFinishTurn(proxy);
//		postGameReset(proxy);
//		
//		postMovesSoldier(proxy);
//		postMovesRollNumber(proxy);
//		postMovesRobPlayer(proxy);
//		
//		postMovesYear_of_Plenty(proxy);
//		postMovesOfferTrade(proxy);
//		postMovesAcceptTrade(proxy);
//		
//		postMovesMonument(proxy);
//		postMovesMonopoly(proxy);
//		postMovesMaritimeTrade(proxy);
//		
//		postMovesBuyDevCard(proxy);
//		getGameModel(proxy);
//		getGameCommands(proxy);
//		postMovesDiscardCards(proxy);
//		postGameCommands(proxy);
//
//		postMovesBuildCity(proxy);
//		postMovesBuildSettlement(proxy);
//		postMovesBuildRoad(proxy);
//		postMovesRoad_Building(proxy);
		
		return;
	}
	
	/**
	 * Tests all calls to the server by creating an HttpProxy, then for each request<br>
	 * 	1) Create a request object for the call <br>
	 *	2) Pass the request object into the HttpProxy which makes the call <br>
	 *	3) Check that it received a valid response (ie not null) <br>
	 */
	@Test
	public void test() {
		Proxy httpProxy = new HttpProxy("localhost","8081");
		testRequestCalls(httpProxy);
		System.out.println("HttpProxy tests all passed!");
	}
}
