package client.proxy;

import client.data.GameInfo;
import com.google.gson.Gson;
import shared.request.game.*;

import java.util.Arrays;
import java.util.List;

/**
 * This class will handle a majority of the higher level game functions: save, join, load, create, list
 */
public class GamesProxy {
    private Proxy proxy;
    private Gson deserializer;
    /**
     * Initialize the HttpProxy
     */
    public GamesProxy(Proxy proxy) {
        this.proxy = proxy;
        this.deserializer = new Gson();
    }

    /**
     * Sends a request to the server and gets all games running on server
     * @pre none
     * @post returns a list of Game
     * @return an array of Games
     */
    public List<GameInfo> list() {
        String response = this.proxy.get("/games/list");
        if (response == null) {
            return null;
        }
        GameInfo[] result = this.deserializer.fromJson(response, GameInfo[].class);
        for (GameInfo game : result) {
            game.removeNullPlayers();
        }
        return Arrays.asList(result);
    }

    /**
     * Sends a request to server and creates a game
     * @pre CreateGameRequest formatted {randomTiles (bool), randomNumbers (bool), randomPorts (bool), name (string)}
     * @post returns -1 if failed and 1 if success
     * @param request
     * @return int
     */
    public GameInfo create(CreateGameRequest request) {
        String response = this.proxy.post("/games/create", request);
        return deserializer.fromJson(response, GameInfo.class);
    }

    /**
     * Joins the player to the designated game
     * @pre takes a joinRequest formatted {id (int), color (enum)}
     * @post returns a -1 if failed and a 1 if successful
     * @param request
     * @return int
     */
    public int join(JoinGameRequest request) {
        if (this.proxy.post("/games/join", request) != null) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Saves the game for debugging purposes.
     * Allows the dev to reinstate the game from the buggy state
     * @pre takes a SaveGameRequest {id (int), name (string)}
     * @post returns a -1 if failed and a 1 if successful
     * @param request
     * @return int
     */
    public int save(SaveGameRequest request) {
        if (this.proxy.post("/games/save", request) != null) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Loads a previously saved game file
     * This is for debug purposes only
     * @pre LoadRequest {name (string)}
     * @post returns a -1 if failed and a 1 if successful
     * @param request
     * @return int
     */
    public int load(LoadGameRequest request) {
        if (this.proxy.post("/games/load", request) != null) {
            return 1;
        } else {
            return -1;
        }
    }
}