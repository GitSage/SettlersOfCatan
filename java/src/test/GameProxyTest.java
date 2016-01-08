package test;

import client.proxy.FileProxy;
import client.proxy.GameProxy;
import junit.framework.TestCase;
import org.junit.Test;
import shared.definitions.HexType;
import shared.locations.EdgeDirection;
import shared.model.*;
import shared.request.game.AddAiRequest;

import java.util.List;

/**
 * Created by thomas on 2/4/2015.
 */
public class GameProxyTest extends TestCase {

    /**
     * Because most of the methods return the game state this method tests to make sure it has been loaded
     * Tests that every piece of the game state has been converted from the JSON to a full GameState object
      * @param state
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
        assert (map.getPorts().get(0).getDirection() == EdgeDirection.NorthEast);
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

        //Test the bank
        assert (state.getBank().getBrick() == 24);
        assert (state.getBank().getWood() == 24);
        assert (state.getBank().getSheep() == 24);

        //Test the turn tracker
        assert (state.getTurnTracker().getCurrentTurn() == 0);

        //Test winner conditions and version
        assert (state.getWinner() == -1);
        assert (state.getVersion() == 0);
    }

    /**
     * Just calls model() and then calls testGameState()
     * @param gameProxy
     */
    private void modelTest(GameProxy gameProxy) {
        //All it does it get the gameState
        GameState gameState = gameProxy.model(-1);
        testGameState(gameState);
    }

    /**
     * More pointless javadocs.  This one tests reset
     * @param gameProxy
     */
    private void resetTest(GameProxy gameProxy) {
        GameState gameState = gameProxy.reset();
        testGameState(gameState);
    }

    /**
     * Tests both versions of commands
     * First gets the list of commands from the JSON and then passes the list into the commands(List) method and verifies both work
     * @param gameProxy
     */
    private void commandsTest(GameProxy gameProxy) {
        //Get the commands
        List<GameCommand> commands = gameProxy.commands();
        assertNotNull(commands);
        //Pass them back in
        GameState gameState = gameProxy.commands(commands);
        testGameState(gameState);
    }

    /**
     * Runs both AI commands
     * First gets a list and then passes a value back in
     * @param gameProxy
     */
    private void aiTest(GameProxy gameProxy) {
        //Get the list
        List<String> ais = gameProxy.listAi();
        assertNotNull(ais);
        //Pass in the first value
        AddAiRequest request = new AddAiRequest(ais.get(0));
        assert (gameProxy.addAi(request) == 1);

    }

    /**
     * Wrapper function just use to call all of the tests
     * @param gameProxy
     */
    private void runTests(GameProxy gameProxy) {
        modelTest(gameProxy);
        resetTest(gameProxy);
        commandsTest(gameProxy);
        aiTest(gameProxy);
    }

    @Test
    /**
     * Inititlizes the test
     */
    public void test() {
        FileProxy proxy = new FileProxy("testJson/");
        GameProxy gameProxy =  new GameProxy(proxy);
        runTests(gameProxy);
    }

}
