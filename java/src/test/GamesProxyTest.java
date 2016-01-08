package test;

import client.data.GameInfo;
import client.data.PlayerInfo;
import client.proxy.FileProxy;
import client.proxy.GamesProxy;
import client.proxy.Proxy;
import junit.framework.TestCase;
import org.junit.Test;
import shared.definitions.CatanColor;
import shared.request.game.*;

import java.util.List;

public class GamesProxyTest extends TestCase {


    /**
     * Just a test to verify the JSON is being converted to a properly formatted Game object
     * @param gamesProxy
     */
    private void listTest(GamesProxy gamesProxy) {
        List<GameInfo> results = gamesProxy.list();
        GameInfo test1 = results.get(0);
        //Test that the game loaded
        assert (test1.getTitle().equals("Default Game"));
        assert (test1.getId() == 0);
        //Get a player from the Default Game
        List<PlayerInfo> playerList = test1.getPlayers();
        PlayerInfo player1 = playerList.get(0);
        //Test the player was retrieved successfully
        assert (player1.getColor().equals("orange"));
        assert (player1.getName().equals("Sam"));
        assert (player1.getId() == 0);
    }

    /**
     * Tests game creation
     * @param gamesProxy
     */
    private void createTest(GamesProxy gamesProxy) {
        CreateGameRequest request = new CreateGameRequest(true, true, true, "JUnit Test");
        assert (gamesProxy.create(request) != null);
    }

    /**
     * Tests join
     * @param gamesProxy
     */
    private void joinTest(GamesProxy gamesProxy) {
        JoinGameRequest request = new JoinGameRequest(0, CatanColor.BLUE);
        assert (gamesProxy.join(request) == 1);
    }

    /**
     * Tests save
     * @param gamesProxy
     */
    private void saveTest(GamesProxy gamesProxy) {
        SaveGameRequest request = new SaveGameRequest(0, "Junit Test Save");
        assert (gamesProxy.save(request) == 1);
    }

    /**
     * Tests Loading a game
     * @param gamesProxy
     */
    private void loadTest(GamesProxy gamesProxy) {
        LoadGameRequest request = new LoadGameRequest("Junit Test Save");
        assert (gamesProxy.load(request) == 1);
    }

    /**
     * Wrapper function that calls all of the tests
     * @param proxy
     */
    private void runTests(Proxy proxy) {
        GamesProxy gamesProxy = new GamesProxy(proxy);
        listTest(gamesProxy);
        createTest(gamesProxy);
        joinTest(gamesProxy);
        saveTest(gamesProxy);
        loadTest(gamesProxy);
    }

    @Test
    /**
     * Initializes the needed proxy and then calls the wrapper runTests()
     */
    public void test() {
        //First test with the file proxy
        FileProxy proxy = new FileProxy("testJson/");
        //Done this way so if we wanted we could add HTTP proxy in and easily swap the tests
        runTests(proxy);
    }

}