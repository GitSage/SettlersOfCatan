package test;

import static org.junit.Assert.*;

import org.junit.Test;

import client.proxy.FileProxy;
import client.proxy.Proxy;

/**
 * FileProxyTest is a JUnit test class to test the FileProxy class
 */
public class FileProxyTest {

	/*************************/
	/*** Test Post methods ***/
	/*************************/
	private void postGameAddAI(Proxy proxy) {
		String response = proxy.post("/game/addAI", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postGameCommands(Proxy proxy) {
		String response = proxy.post("/game/commands", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postGameReset(Proxy proxy) {
		String response = proxy.post("/game/reset", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postGamesCreate(Proxy proxy) {
		String response = proxy.post("/games/create", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postGamesJoin(Proxy proxy) {
		String response = proxy.post("/games/join", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postGamesLoad(Proxy proxy) {
		String response = proxy.post("/games/load", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postGamesSave(Proxy proxy) {
		String response = proxy.post("/games/save", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesAcceptTrade(Proxy proxy) {
		String response = proxy.post("/moves/acceptTrade", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesBuildCity(Proxy proxy) {
		String response = proxy.post("/moves/buildCity", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesBuildRoad(Proxy proxy) {
		String response = proxy.post("/moves/buildRoad", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}	
	private void postMovesBuildSettlement(Proxy proxy) {
		String response = proxy.post("/moves/buildSettlement", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesBuyDevCard(Proxy proxy) {
		String response = proxy.post("/moves/buyDevCard", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesDiscardCards(Proxy proxy) {
		String response = proxy.post("/moves/discardCards", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesFinishTurn(Proxy proxy) {
		String response = proxy.post("/moves/finishTurn", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesMaritimeTrade(Proxy proxy) {
		String response = proxy.post("/moves/maritimeTrade", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesMonopoly(Proxy proxy) {
		String response = proxy.post("/moves/Monopoly", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesMonument(Proxy proxy) {
		String response = proxy.post("/moves/Monument", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesOfferTrade(Proxy proxy) {
		String response = proxy.post("/moves/offerTrade", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesRoad_Building(Proxy proxy) {
		String response = proxy.post("/moves/Road_Building", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesRobPlayer(Proxy proxy) {
		String response = proxy.post("/moves/robPlayer", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesRollNumber(Proxy proxy) {
		String response = proxy.post("/moves/rollNumber", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesSendChat(Proxy proxy) {
		String response = proxy.post("/moves/sendChat", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesSoldier(Proxy proxy) {
		String response = proxy.post("/moves/Soldier", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postMovesYear_of_Plenty(Proxy proxy) {
		String response = proxy.post("/moves/Year_of_Plenty", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postUserLogin(Proxy proxy) {
		String response = proxy.post("/user/login", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postUserRegister(Proxy proxy) {
		String response = proxy.post("/user/register", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	private void postUtilChangeLogLevel(Proxy proxy) {
		String response = proxy.post("/util/changeLogLevel", null);
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}
	
	private void testPost(Proxy proxy) {
		postUtilChangeLogLevel(proxy);
		postUserRegister(proxy);
		postUserLogin(proxy);
		postMovesYear_of_Plenty(proxy);
		postMovesSoldier(proxy);
		postMovesSendChat(proxy);
		postMovesRollNumber(proxy);
		postMovesRobPlayer(proxy);
		postMovesRoad_Building(proxy);
		postMovesYear_of_Plenty(proxy);
		postMovesOfferTrade(proxy);
		postMovesMonument(proxy);
		postMovesMonopoly(proxy);
		postMovesMaritimeTrade(proxy);
		postMovesFinishTurn(proxy);
		postMovesDiscardCards(proxy);
		postMovesBuyDevCard(proxy);
		postMovesBuildSettlement(proxy);
		postMovesBuildRoad(proxy);
		postMovesBuildCity(proxy);
		postMovesAcceptTrade(proxy);
		postGamesSave(proxy);
		postGamesLoad(proxy);
		postGamesJoin(proxy);
		postGamesCreate(proxy);
		postGameReset(proxy);
		postGameCommands(proxy);
		postGameAddAI(proxy);
		
		return;
	}
	
	/************************/
	/*** Test Get methods ***/
	/************************/
	private void getGamesList(Proxy proxy) {
		String response = proxy.get("/games/list");
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}	
	private void getGameModel(Proxy proxy) {
		String response = proxy.get("/game/model");
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}	
	private void getGameCommands(Proxy proxy) {
		String response = proxy.get("/game/commands");
	 //System.out.println(response);
		assertNotNull(response);
		return;
	}	
	
	private void testGet(Proxy proxy) {
		getGamesList(proxy);
		getGameModel(proxy);
		getGameCommands(proxy);
		return;
	}

	/**
	 * Tests all file calls by creating a FileProxy, then for each request<br>
	 * 1)Use the FileProxy to make the request
	 * 2)Check that the response is valid (ie not null)
	 */
	@Test
	public void test() {
		Proxy fileProxy = new FileProxy("testJson/");
		testGet(fileProxy);
		testPost(fileProxy);
		System.out.println("FileProxy tests all passed!");
	}
	
}
