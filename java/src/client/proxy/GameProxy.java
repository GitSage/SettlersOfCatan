package client.proxy;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.GameCommand;
import shared.model.GameState;
import shared.model.Hex;
import shared.model.Port;
import shared.adapter.EdgeLocationTypeAdapter;
import shared.adapter.HexTypeAdapter;
import shared.adapter.PortTypeAdapter;
import shared.adapter.VertexLocationTypeAdapter;
import shared.request.game.AddAiRequest;

/**
 * This class is the proxy used for top level game access.
 * This contains controls that are not designated to a specific player
 */
public class GameProxy {
    private Proxy proxy;
    private Gson deserializer;

    /**
     * Initialize Proxy
     */
    public GameProxy(Proxy proxy) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Hex.class, new HexTypeAdapter());
        gsonBuilder.registerTypeAdapter(Port.class, new PortTypeAdapter());
	    gsonBuilder.registerTypeAdapter(EdgeLocation.class, new EdgeLocationTypeAdapter());
	    gsonBuilder.registerTypeAdapter(VertexLocation.class, new VertexLocationTypeAdapter());
        deserializer = gsonBuilder.create();
        this.proxy = proxy;
    }

    /**
     * @pre Takes in int for the version
     * @post returns a model.GameState
     * @return current game state from server
     */
    public GameState model(int version) {
        String url = "/game/model";
        if (version >= -1) {
            url += "?version=" + String.valueOf(version);
        }
        String result = proxy.get(url);
				if (result.equals("\"true\"")) {
					return GameState.get();
				}
        if (result == null) {
            return null;
        } else {
            return deserializer.fromJson(result, GameState.class);
        }
    }

    /**
     * Returns the game back to right before the team placement phase
     * @pre Takes in nothing just requires proxy to be working
     * @post returns a model.GameState
     * @return GameState after it's been reset
     */
    public GameState reset() {
        String url = "/game/reset";
        String result = proxy.post(url, null);
        if (result == null) {
            return null;
        } else {
            return deserializer.fromJson(result, GameState.class);
        }
    }

    /**
     * Takes a list of commands and executes them on the server
     * @pre Takes in list of GameCommand
     * @post returns a model.GameState
     * @param commandList
     * @return GameState after commands have been executed
     */
    public GameState commands(List<GameCommand> commandList) {
        String url = "/game/commands";
        String result = proxy.post(url, commandList);
        if (result == null) {
            return null;
        } else {
            return deserializer.fromJson(result, GameState.class);
        }
    }

    /**
     * Returns a list of all available commands from the server
     * @pre Takes in nothing just requires proxy to be working
     * @post returns a list of GameCommand
     * @return GameCommand
     */
    public List<GameCommand> commands() {
        String url = "/game/commands";
        String result = proxy.get(url);
        if (result == null) {
            return null;
        } else {
            return Arrays.asList(deserializer.fromJson(result, GameCommand[].class));
        }
    }

    /**
     * Adds an ai to the game
     * @pre Takes in AiRequest {}
     * @post -1 for failure and 1 for success
     * @param request
     * @return int
     */
    public int addAi(AddAiRequest request) {
        String url = "/game/addAI";
        String result = proxy.post(url, request);
        if (result == null) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * @pre Takes in nothing just requires proxy to be working
     * @post returns a list of Ai's only one type {LARGEST_ARMY}
     * @return list of AI types
     */
    public List<String> listAi() {
        String url = "/game/listAI";
        String result = proxy.get(url);
        if (result == null) {
            return null;
        } else {
            return Arrays.asList(deserializer.fromJson(result, String[].class));
        }
    }
}