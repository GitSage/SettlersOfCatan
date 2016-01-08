package test;

import client.proxy.FileProxy;
import client.proxy.MovesProxy;
import client.proxy.Proxy;
import junit.framework.TestCase;

import org.junit.Test;

import shared.definitions.HexType;
import shared.definitions.ResourceType;
import shared.locations.*;
import shared.model.*;
import shared.request.move.*;

import java.util.List;

/**
 * Created by thomas on 2/3/2015.
 */
public class MovesProxyTest extends TestCase {

    /**
     * All actions get a G0amestate back and so they will be doing all of the same tests
     * This test just verifies that the GameState has been properly loaded
     * @param state
     * @return
     */
    private void testGameState(GameState state) {
        //Make sure a gameState was loaded
        assertNotNull(state);

        //Test the deck first
        DevCardList deck = state.getDeck();
        assert (deck.getMonopoly() == 2);
        assert (deck.getMonument() == 5);

        //Test the map
        GameMap map = state.getMap();
        List<Hex> hexes = map.getHexes();
        Hex hex1 = hexes.get(0);
        assert (hex1.getType() == HexType.SHEEP);
        assert (hex1.getNumber() == 9);
        assert (hex1.getLocation().getX() == 0);
        assert (hex1.getLocation().getY() == -2);
        assert (map.getRadius() == 3);
        assert (map.getPorts().get(0).getRatio() == 3);
        assert (map.getPorts().get(0).getDirection().equals("SE"));
        assert (map.getPorts().get(0).getLocation().getX() == -3);
        assert (map.getPorts().get(0).getLocation().getY() == -0);
        assert (map.getRobber().getX() == 1);
        assert (map.getRobber().getY() == -1);

        //Test the players
        List<Player> players = state.getPlayers();
        Player player = players.get(0);
        assert (player.getResources().getBrick() == 0);
        assert (player.getOldDevCards().getMonument() == 0);
        assert (player.getNewDevCards().getMonument() == 0);
        assert (player.getRoads() == 15);
        assert (player.getName().equals("Sam"));

        //Test the chat
        assert (state.getChat().getLines().get(0).getMessage().equals("Hello friends!"));
        assert (state.getChat().getLines().get(0).getSource().equals("Quinn"));

        //Test the bank
        assert (state.getBank().getBrick() == 24);
        assert (state.getBank().getWood() == 24);
        assert (state.getBank().getSheep() == 24);

        //Test the turn tracker
        assert (state.getTurnTracker().getCurrentTurn() == 0);

        //Test winner conditions and version
        assert (state.getWinner() == -1);
        assert (state.getVersion() == 1);
    }

    private void sendChatTest(MovesProxy movesProxy) {
        SendChatRequest request = new SendChatRequest(0, "Hello friends!");
        //Should get a gameState back
        GameState gameState = movesProxy.sendChat(request);
        testGameState(gameState);
    }

    private void rollNumberTest(MovesProxy movesProxy) {
        RollNumberRequest request = new RollNumberRequest(0, 12);
        GameState gameState = movesProxy.rollNumber(request);
        testGameState(gameState);
    }

    private void robPlayerTest(MovesProxy movesProxy) {
        HexLocation location = new HexLocation(0, -2);
        RobPlayerRequest request = new RobPlayerRequest(0, 1, location);
        GameState gameState = movesProxy.robPlayer(request);
        testGameState(gameState);
    }

    private void finishTurnTest(MovesProxy movesProxy) {
        FinishTurnRequest request = new FinishTurnRequest(0);
        GameState gameState = movesProxy.finishTurn(request);
        testGameState(gameState);
    }

    private void buyDevCardTest(MovesProxy movesProxy) {
        BuyDevCardRequest request = new BuyDevCardRequest(0);
        GameState gameState = movesProxy.buyDevCard(request);
        testGameState(gameState);
    }

    private void yearOfPlentyTest(MovesProxy movesProxy) {
        YearOfPlentyCardRequest request = new YearOfPlentyCardRequest(0, ResourceType.SHEEP, ResourceType.BRICK);
        GameState gameState = movesProxy.yearOfPlenty(request);
        testGameState(gameState);
    }

    private void roadBuildingTest(MovesProxy movesProxy) {
        HexLocation hexLocation1 = new HexLocation(0, -2);
        EdgeLocation location1 = new EdgeLocation(hexLocation1, EdgeDirection.NorthWest);
        HexLocation hexLocation2 = new HexLocation(1, -2);
        EdgeLocation location2 = new EdgeLocation(hexLocation2, EdgeDirection.NorthWest);
        RoadBuildingCardRequest request = new RoadBuildingCardRequest(0, location1, location2);
        GameState gameState = movesProxy.roadBuilding(request);
        testGameState(gameState);
    }

    private void soldierTest(MovesProxy movesProxy) {
        HexLocation location = new HexLocation(0,-2);
        SoldierCardRequest request = new SoldierCardRequest(0, 1, location);
        GameState gameState = movesProxy.soldier(request);
        testGameState(gameState);
    }

    private void monopolyTest(MovesProxy movesProxy) {
        MonopolyCardRequest request = new MonopolyCardRequest(0, ResourceType.BRICK);
        GameState gameState = movesProxy.monopoly(request);
        testGameState(gameState);
    }

    private void monumentTest(MovesProxy movesProxy) {
        MonumentCardRequest request = new MonumentCardRequest(0);
        GameState gameState = movesProxy.monument(request);
        testGameState(gameState);
    }

    private void buildRoadTest(MovesProxy movesProxy) {
        HexLocation hexLocation1 = new HexLocation(0, -2);
        EdgeLocation location1 = new EdgeLocation(hexLocation1, EdgeDirection.NorthWest);
        BuildRoadRequest request = new BuildRoadRequest(0,location1,true);
        GameState gameState = movesProxy.buildRoad(request);
        testGameState(gameState);
    }

    private void buildSettlementTest(MovesProxy movesProxy) {
        HexLocation hexLocation1 = new HexLocation(0, -2);
        VertexLocation location = new VertexLocation(hexLocation1, VertexDirection.East);
        BuildSettlementRequest request = new BuildSettlementRequest(0,location,true);
        GameState gameState = movesProxy.buildSettlement(request);
        testGameState(gameState);
    }

    private void buildCityTest(MovesProxy movesProxy) {
        HexLocation hexLocation1 = new HexLocation(0, -2);
        VertexLocation location = new VertexLocation(hexLocation1, VertexDirection.East);
        BuildCityRequest request = new BuildCityRequest(0,location,true);
        GameState gameState = movesProxy.buildCity(request);
        testGameState(gameState);
    }

    private void offerTradeTest(MovesProxy movesProxy) {
        ResourceList list = new ResourceList(1, 0, 1, 0, 0);
        OfferTradeRequest request = new OfferTradeRequest(0,list,1);
        GameState gameState = movesProxy.offerTrade(request);
        testGameState(gameState);
    }

    private void acceptTradeTest(MovesProxy movesProxy) {
        AcceptTradeRequest request = new AcceptTradeRequest(1, true);
        GameState gameState = movesProxy.acceptTrade(request);
        testGameState(gameState);
    }

    private void maritimeTradeTest(MovesProxy movesProxy) {
        MaritimeTradeRequest request = new MaritimeTradeRequest(0, 4, ResourceType.BRICK, ResourceType.SHEEP);
        GameState gameState = movesProxy.maritimeTrade(request);
        testGameState(gameState);
    }

    private void discardCardsTest(MovesProxy movesProxy) {
        ResourceList list = new ResourceList(1, 0, 1, 0, 0);
        DiscardRequest request = new DiscardRequest(0, list);
        GameState gameState = movesProxy.discardCards(request);
        testGameState(gameState);
    }

    /**
     * Wrapper function that initializes the movesProxy and then calls all the required tests
     * @param proxy
     */
    private void runTests(Proxy proxy) {
        MovesProxy movesProxy = new MovesProxy(proxy);
        sendChatTest(movesProxy);
        rollNumberTest(movesProxy);
        robPlayerTest(movesProxy);
        finishTurnTest(movesProxy);
        buyDevCardTest(movesProxy);
        yearOfPlentyTest(movesProxy);
        roadBuildingTest(movesProxy);
        soldierTest(movesProxy);
        monopolyTest(movesProxy);
        monumentTest(movesProxy);
        buildRoadTest(movesProxy);
        buildSettlementTest(movesProxy);
        buildCityTest(movesProxy);
        offerTradeTest(movesProxy);
        acceptTradeTest(movesProxy);
        maritimeTradeTest(movesProxy);
        discardCardsTest(movesProxy);
    }

    @Test
    /**
     * Runs all the tests after creating the appropriate proxy connection.
     */
    public void test() {
        FileProxy proxy = new FileProxy("testJson/");
        runTests(proxy);
    }
}
