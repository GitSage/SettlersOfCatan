package test;

import client.proxy.FileProxy;
import client.proxy.Proxy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import junit.framework.TestCase;
import shared.definitions.TurnStatus;
import shared.locations.*;
import shared.model.*;
import shared.adapter.EdgeLocationTypeAdapter;
import shared.adapter.HexTypeAdapter;
import shared.adapter.PortTypeAdapter;
import shared.adapter.VertexLocationTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class GameStateTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    /*public void testDeserializeSerialize() throws Exception {
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(Hex.class, new HexTypeAdapter());
	    gsonBuilder.registerTypeAdapter(Port.class, new PortTypeAdapter());
	    gsonBuilder.registerTypeAdapter(EdgeLocation.class, new EdgeLocationTypeAdapter());
	    gsonBuilder.registerTypeAdapter(VertexLocation.class, new VertexLocationTypeAdapter());
	    Gson gson = gsonBuilder.create();
        Proxy proxy = new FileProxy("testJson/");
        String jsonString = proxy.get("/game/model");
        JsonElement jsonElement = new JsonPrimitive(jsonString);
        GameState state = gson.fromJson(jsonString, GameState.class);
        String preString = gson.toJson(jsonElement);
        String postString = gson.toJson(state);
        assert(preString.equals(postString));

    }*/

    /**
     * Tests the canDiscardCards function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>TurnStatus is wrong</li>
     *     <li>Player lacks resources</li>
     *     <li>Currently the player's turn</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanDiscardCards() throws Exception {
        // setup
        GameState g = getDefaultGameState(0, TurnStatus.DISCARDING);
        g.getPlayer(0).setResources(new ResourceList(2, 2, 2, 2, 0));

        // test player's turn
        assertFalse(g.canDiscardCards(1));

        // test TurnStatus is Discarding
        g.getTurnTracker().setStatus(TurnStatus.PLAYING);
        assertFalse(g.canDiscardCards(0));

        // test not enough cards
        g.getPlayer(0).setResources(new ResourceList(1, 2, 2, 2, 0));
        assertFalse(g.canDiscardCards(0));

        //test good conditions
        g.getTurnTracker().setStatus(TurnStatus.DISCARDING);
        g.getPlayer(0).setResources(new ResourceList(2, 2, 2, 2, 0));
        assertTrue(g.canDiscardCards(0));
    }

    /**
     * Tests the canRollNumber function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanRollNumber() throws Exception {
        // setup
        GameState g = getDefaultGameState(0, TurnStatus.ROLLING);

        // test player's turn
        assertFalse(g.canRollNumber(1));

        // test TurnStatus is Rolling
        g.getTurnTracker().setStatus(TurnStatus.PLAYING);
        assertFalse(g.canRollNumber(0));

        // test good conditions
        g.getTurnTracker().setStatus(TurnStatus.ROLLING);
        assertTrue(g.canRollNumber(0));
    }

    /**
     * Tests the canBuildRoad function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Road is in the way</li>
     *     <li>Player is trying to build in an invalid location</li>
     *     <li>Road is not connected to another road or city</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanBuildRoad() throws Exception {
        // setup
        GameState g = getDefaultGameState(0, TurnStatus.ROLLING);
        VertexObject pCity = new VertexObject();
        pCity.setOwner(0);
        VertexLocation vl = new VertexLocation(new HexLocation(1, 0), VertexDirection.NorthWest);
        pCity.setLocation(vl);

        EdgeLocation eBad = new EdgeLocation(new HexLocation(0, 0), EdgeDirection.North);
        EdgeLocation eGood = new EdgeLocation(new HexLocation(-2, 0), EdgeDirection.NorthEast);
        Road rBad = new Road();
        rBad.setLocation(eBad);
        g.getMap().getRoads().add(rBad);
        g.getMap().getCities().add(pCity);

        // test player's turn
        // supposed to fail because it is player 0's turn
        assertFalse(g.canBuildRoad(1, eGood));

        // test is road already in the way
        assertFalse(g.canBuildRoad(0, eBad));

        // test TurnStatus is FIRSTROUND or SECONDROUND
        g.getTurnTracker().setStatus(TurnStatus.FIRSTROUND);
        assertTrue(g.canBuildRoad(0, eGood));
        g.getTurnTracker().setStatus(TurnStatus.SECONDROUND);
        assertTrue(g.canBuildRoad(0, eGood));
        g.getTurnTracker().setStatus(TurnStatus.ROLLING);
        assertFalse(g.canBuildRoad(0, eGood));
        g.getTurnTracker().setStatus(TurnStatus.DISCARDING);
        assertFalse(g.canBuildRoad(0, eGood));
        g.getTurnTracker().setStatus(TurnStatus.ROBBING);
        assertFalse(g.canBuildRoad(0, eGood));

        // test resources
        // should fail because player does not have sufficient resources
        g.getPlayer(0).setResources(new ResourceList(0, 0, 0, 0, 0));
        assertFalse(g.canBuildRoad(0, eGood));

    }


    /**
     * Tests the canBuildSettlement function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>City is already in the way</li>
     *     <li>City is in an invalid location</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanBuildSettlement() throws Exception {
        GameState g = getDefaultGameState(0, TurnStatus.FIRSTROUND);
        VertexLocation newCity = new VertexLocation(new HexLocation(0, 0), VertexDirection.NorthEast);
        VertexLocation cityInTheWay = new VertexLocation(new HexLocation(1, 1), VertexDirection.NorthEast);
        VertexObject cityInTheWayObject = new VertexObject();
        cityInTheWayObject.setLocation(cityInTheWay);
        g.getMap().getCities().add(cityInTheWayObject);

        // set up road for settlement to connect to
        Road r = new Road();
        r.setLocation(new EdgeLocation(new HexLocation(0, 0), EdgeDirection.North));
        g.getMap().getRoads().add(r);

        // successful attempt
        //System.out.println("testCanBuildSettlement: trying successful case");
        assertTrue(g.canBuildSettlement(0, newCity));

        // city already in the way
        assertFalse(g.canBuildSettlement(0, new VertexLocation(new HexLocation(1, 1), VertexDirection.NorthEast)));

        // turn status is PLAYING, FIRSTROUND, or SECONDROUND
        g.getTurnTracker().setStatus(TurnStatus.PLAYING);
        assertTrue(g.canBuildSettlement(0, newCity));
        g.getTurnTracker().setStatus(TurnStatus.FIRSTROUND);
        assertTrue(g.canBuildSettlement(0, newCity));
        g.getTurnTracker().setStatus(TurnStatus.SECONDROUND);
        assertTrue(g.canBuildSettlement(0, newCity));
        g.getTurnTracker().setStatus(TurnStatus.ROLLING);
        assertFalse(g.canBuildSettlement(0, newCity));
        g.getTurnTracker().setStatus(TurnStatus.DISCARDING);
        assertFalse(g.canBuildSettlement(0, newCity));
        g.getTurnTracker().setStatus(TurnStatus.ROBBING);
        assertFalse(g.canBuildSettlement(0, newCity));

        // city has to be on land
        VertexLocation cityOnWater = new VertexLocation(new HexLocation(0, 4), VertexDirection.NorthEast);
        assertFalse(g.canBuildSettlement(0, cityOnWater));

    }


    /**
     * Tests the canBuildCity function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Player doesn't have enough resources</li>
     *     <li>Settlement belongs to someone else</li>
     *     <li>Trying to build in an area without a settlement</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanBuildCity() throws Exception {
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);
        VertexObject pCity = new VertexObject();
        pCity.setOwner(0);
        VertexLocation vl = new VertexLocation(new HexLocation(0, 0), VertexDirection.West);
        pCity.setLocation(vl);

        VertexObject pSett = new VertexObject();
        pSett.setOwner(0);
        VertexLocation v2 = new VertexLocation(new HexLocation(0, 0), VertexDirection.West);
        pSett.setLocation(v2);

        g.getMap().getSettlements().add(pSett);
        g.getPlayer(0).setResources(new ResourceList(3, 3, 3, 3, 3));

        // successsful
        assertTrue(g.canBuildCity(0, pCity.getLocation()));

        // settlement belongs to someone else
        pSett.setOwner(1);
        assertFalse(g.canBuildCity(0, pCity.getLocation()));
        pSett.setOwner(0);

        // not enough resources
        g.getPlayer(0).setResources(new ResourceList(0, 0, 0, 0, 0));
        assertFalse(g.canBuildCity(0, pCity.getLocation()));

    }


    /**
     * Tests the canOfferTrade function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Trying to trade a resource that the player doesn't have</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanOfferTrade() throws Exception {
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);
        ResourceList r = new ResourceList(2, 2, 2, 2, 2);
        g.getPlayer(0).setResources(r);

        // test brick
        r = new ResourceList(2, 0, 0, 0, 0);
        assertTrue(g.canOfferTrade(0, r));
        r = new ResourceList(3, 0, 0, 0, 0);
        assertFalse(g.canOfferTrade(0, r));

        // test ore
        r = new ResourceList(0, 2, 0, 0, 0);
        assertTrue(g.canOfferTrade(0, r));
        r = new ResourceList(0, 3, 0, 0, 0);
        assertFalse(g.canOfferTrade(0, r));

        // test sheep
        r = new ResourceList(0, 0, 2, 0, 0);
        assertTrue(g.canOfferTrade(0, r));
        r = new ResourceList(0, 0, 3, 0, 0);
        assertFalse(g.canOfferTrade(0, r));

        // test wheat
        r = new ResourceList(0, 0, 0, 2, 0);
        assertTrue(g.canOfferTrade(0, r));
        r = new ResourceList(0, 0, 0, 3, 0);
        assertFalse(g.canOfferTrade(0, r));

        // test wood
        r = new ResourceList(0, 0, 0, 0, 2);
        assertTrue(g.canOfferTrade(0, r));
        r = new ResourceList(0, 0, 0, 0, 3);
        assertFalse(g.canOfferTrade(0, r));

    }

    /**
     * Tests the canAcceptTrade function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Trying to trade a resource that the player doesn't have</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanAcceptTrade() throws Exception {
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);
        ResourceList r = new ResourceList(2, 2, 2, 2, 2);
        g.getPlayer(0).setResources(r);

        // test brick
        r = new ResourceList(2, 0, 0, 0, 0);
        assertTrue(g.canAcceptTrade(0, r));
        r = new ResourceList(3, 0, 0, 0, 0);
        assertFalse(g.canAcceptTrade(0, r));

        // test ore
        r = new ResourceList(0, 2, 0, 0, 0);
        assertTrue(g.canAcceptTrade(0, r));
        r = new ResourceList(0, 3, 0, 0, 0);
        assertFalse(g.canAcceptTrade(0, r));

        // test sheep
        r = new ResourceList(0, 0, 2, 0, 0);
        assertTrue(g.canAcceptTrade(0, r));
        r = new ResourceList(0, 0, 3, 0, 0);
        assertFalse(g.canAcceptTrade(0, r));

        // test wheat
        r = new ResourceList(0, 0, 0, 2, 0);
        assertTrue(g.canAcceptTrade(0, r));
        r = new ResourceList(0, 0, 0, 3, 0);
        assertFalse(g.canAcceptTrade(0, r));

        // test wood
        r = new ResourceList(0, 0, 0, 0, 2);
        assertTrue(g.canAcceptTrade(0, r));
        r = new ResourceList(0, 0, 0, 0, 3);
        assertFalse(g.canAcceptTrade(0, r));

    }


    /**
     * Tests the canMaritimeTrade function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Trying to trade a resource that the player doesn't have</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanMaritimeTrade() throws Exception {
        // setup
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);

        // success
        assertTrue(g.canMaritimeTrade(0, null));

        // test player's turn
        assertFalse(g.canMaritimeTrade(1, null));

        // test TurnStatus is PLAYING
        g.getTurnTracker().setStatus(TurnStatus.DISCARDING);
        assertFalse(g.canMaritimeTrade(0, null));
    }

    /**
     * Tests the canFinishTurn function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanFinishTurn() throws Exception {
        // setup
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);

        // success
        assertTrue(g.canFinishTurn(0));

        // test player's turn
        assertFalse(g.canFinishTurn(1));

        // test TurnStatus is PLAYING
        g.getTurnTracker().setStatus(TurnStatus.DISCARDING);
        assertFalse(g.canFinishTurn(0));
    }

    /**
     * Tests the canBuyDevCard function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Insufficient resources</li>
     *     <li>Deck is empty</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanBuyDevCard() throws Exception {
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);
        ResourceList r = new ResourceList(0, 1, 1, 1, 0);
        g.getPlayer(0).setResources(r);
        g.setDeck(new DevCardList(5, 5, 5, 5, 5));

        // successful
        assertTrue(g.canBuyDevCard(0));

        // deck is empty
        g.setDeck(new DevCardList(0, 0, 0, 0, 0));
        assertFalse(g.canBuyDevCard(0));
        g.setDeck(new DevCardList(5, 5, 5, 5, 5));

        // insufficient resources
        g.getPlayer(0).setResources(new ResourceList(0, 0, 0, 0, 0));
        assertFalse(g.canBuyDevCard(0));
    }

    /**
     * Tests the canUseYearOfPlenty function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Doesn't have the correct dev card</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanUseYearOfPlenty() throws Exception {
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);
        g.getPlayer(0).setOldDevCards(new DevCardList(0, 0, 0, 0, 1));
        g.getPlayer(0).setNewDevCards(new DevCardList(0, 0, 0, 0, 1));
        g.getPlayer(0).setPlayedDevCard(false);

        // successful
        assertTrue(g.canUseYearOfPlenty(0));

        // has already played dev card
        g.getPlayer(0).setPlayedDevCard(true);
        assertFalse(g.canUseYearOfPlenty(0));
        g.getPlayer(0).setPlayedDevCard(false);

        // doesn't have dev card
        g.getPlayer(0).setOldDevCards(new DevCardList(0, 0, 0, 0, 0));
        assertFalse(g.canUseYearOfPlenty(0));
    }

    /**
     * Tests the canUseRoadBuilder function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Doesn't have the correct dev card</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanUseRoadBuilder() throws Exception {
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);
        g.getPlayer(0).setOldDevCards(new DevCardList(0, 0, 1, 0, 0));
        g.getPlayer(0).setNewDevCards(new DevCardList(0, 0, 1, 0, 0));
        g.getPlayer(0).setPlayedDevCard(false);

        // successful
        assertTrue(g.canUseRoadBuilder(0));

        // has already played dev card
        g.getPlayer(0).setPlayedDevCard(true);
        assertFalse(g.canUseRoadBuilder(0));
        g.getPlayer(0).setPlayedDevCard(false);

        // doesn't have dev card
        g.getPlayer(0).setOldDevCards(new DevCardList(0, 0, 0, 0, 0));
        assertFalse(g.canUseRoadBuilder(0));
    }

    /**
     * Tests the canUseSoldier function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Doesn't have the correct dev card</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanUseSoldier() throws Exception {
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);
        g.getPlayer(0).setOldDevCards(new DevCardList(0, 0, 0, 1, 0));
        g.getPlayer(0).setNewDevCards(new DevCardList(0, 0, 0, 1, 0));
        g.getPlayer(0).setPlayedDevCard(false);

        // successful
        assertTrue(g.canUseSoldier(0));

        // has already played dev card
        g.getPlayer(0).setPlayedDevCard(true);
        assertFalse(g.canUseSoldier(0));
        g.getPlayer(0).setPlayedDevCard(false);

        // doesn't have dev card
        g.getPlayer(0).setOldDevCards(new DevCardList(0, 0, 0, 0, 0));
        assertFalse(g.canUseSoldier(0));
    }

    /**
     * Tests the canUseMonopoly function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Doesn't have the correct dev card</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanUseMonopoly() throws Exception {
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);
        g.getPlayer(0).setOldDevCards(new DevCardList(1, 0, 0, 0, 0));
        g.getPlayer(0).setNewDevCards(new DevCardList(1, 0, 0, 0, 0));
        g.getPlayer(0).setPlayedDevCard(false);

        // successful
        assertTrue(g.canUseMonopoly(0));

        // has already played dev card
        g.getPlayer(0).setPlayedDevCard(true);
        assertFalse(g.canUseMonopoly(0));
        g.getPlayer(0).setPlayedDevCard(false);

        // doesn't have dev card
        g.getPlayer(0).setOldDevCards(new DevCardList(0, 0, 0, 0, 0));
        assertFalse(g.canUseMonopoly(0));
    }

    /**
     * Tests the canUseMonument function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Doesn't have the correct dev card</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanUseMonument() throws Exception {
        GameState g = getDefaultGameState(0, TurnStatus.PLAYING);
        g.getPlayer(0).setOldDevCards(new DevCardList(0, 1, 0, 0, 0));
        g.getPlayer(0).setNewDevCards(new DevCardList(0, 1, 0, 0, 0));
        g.getPlayer(0).setPlayedDevCard(false);

        // successful
        assertTrue(g.canUseMonument(0));

        // doesn't have dev card
        g.getPlayer(0).setOldDevCards(new DevCardList(0, 0, 0, 0, 0));
        assertFalse(g.canUseMonument(0));
    }

    /**
     * Tests the canPlaceRobber function.
     * Conducts a successful test and failed tests for several cases:
     * <ol>
     *     <li>Not player's turn</li>
     *     <li>TurnStatus is wrong</li>
     *     <li>Doesn't have the correct dev card</li>
     * </ol>
     * <pre>none</pre>
     * <post>none</post>
     * @throws Exception
     */
    public void testCanPlaceRobber() throws Exception {
        // setup
        GameState g = getDefaultGameState(0, TurnStatus.ROBBING);
        HexLocation land = new HexLocation(0, 0);
        HexLocation water = new HexLocation(0, 4);
        HexLocation invalid = new HexLocation(10, 10);

        // test location is invalid
        assertFalse(g.canPlaceRobber(0, water));
        assertFalse(g.canPlaceRobber(0, invalid));


        // test good conditions
        g.getTurnTracker().setStatus(TurnStatus.ROBBING);
        assertTrue(g.canPlaceRobber(0, land));
    }

    /**
     * Gets a GameState with the player and TurnStatus specified.
     * @return
     */
    public GameState getDefaultGameState(int p, TurnStatus turnStatus){
        TurnTracker tt= new TurnTracker();
        tt.setStatus(turnStatus);
        tt.setCurrentTurn(p);
        GameMap m = new GameMap();
        m.setRadius(3);
        List<Road> roads = new ArrayList<Road>();
        List<VertexObject> cities = new ArrayList<VertexObject>();
        List<VertexObject> settlements = new ArrayList<VertexObject>();

        DevCardList deck = new DevCardList();
        deck.setMonopoly(0);
        deck.setMonument(0);
        deck.setRoadBuilding(0);
        deck.setSoldier(0);
        deck.setYearOfPlenty(0);

        m.setRoads(roads);
        m.setCities(cities);
        m.setSettlements(settlements);

        m.setRobber(new HexLocation(0, 1));

        GameState g = new GameState();
        g.simpleSetPlayers(getPlayerList());
        g.setTurnTracker(tt);
        g.setMap(m);
        g.setDeck(deck);

        return g;
    }

    public List<Player> getPlayerList(){
        List<Player> p = new ArrayList<Player>();
        Player p0 = new Player();
        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();
        p0.setPlayerIndex(0);
        p0.setNewDevCards(new DevCardList());
        p1.setPlayerIndex(1);
        p2.setPlayerIndex(2);
        p3.setPlayerIndex(3);

        p0.setNewDevCards(new DevCardList());
        p0.setOldDevCards(new DevCardList());
        p1.setNewDevCards(new DevCardList());
        p1.setOldDevCards(new DevCardList());
        p2.setNewDevCards(new DevCardList());
        p2.setOldDevCards(new DevCardList());
        p3.setNewDevCards(new DevCardList());
        p3.setOldDevCards(new DevCardList());

        p0.setResources(new ResourceList(0, 0, 0, 0, 0));
        p1.setResources(new ResourceList(0, 0, 0, 0, 0));
        p2.setResources(new ResourceList(0, 0, 0, 0, 0));
        p3.setResources(new ResourceList(0, 0, 0, 0, 0));

        p0.setRoads(15);
        p1.setRoads(15);
        p2.setRoads(15);
        p3.setRoads(15);


        p.add(p0);
        p.add(p1);
        p.add(p2);
        p.add(p3);



        p.get(0).setResources(new ResourceList(1, 1, 1, 1, 1));

        return p;
    }
}