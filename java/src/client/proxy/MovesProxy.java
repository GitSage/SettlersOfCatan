package client.proxy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.*;
import shared.adapter.EdgeLocationTypeAdapter;
import shared.adapter.HexTypeAdapter;
import shared.adapter.PortTypeAdapter;
import shared.adapter.VertexLocationTypeAdapter;
import shared.request.move.*;

/**
 * TODO added error checking if proxy.post returns NULL return null
 * This class will be responsible for everything a player can do on his/her turn
 */
public class MovesProxy {

    private Proxy proxy;
    private Gson deserializer;

    /**
     * Initialize HttpProxy
     */
    public MovesProxy(Proxy proxy) {
	    GsonBuilder gsonBuilder = new GsonBuilder();
	    gsonBuilder.registerTypeAdapter(Hex.class, new HexTypeAdapter());
	    gsonBuilder.registerTypeAdapter(Port.class, new PortTypeAdapter());
	    gsonBuilder.registerTypeAdapter(EdgeLocation.class, new EdgeLocationTypeAdapter());
	    gsonBuilder.registerTypeAdapter(VertexLocation.class, new VertexLocationTypeAdapter());
	    this.deserializer = gsonBuilder.create();
        this.proxy = proxy;
    }

    /**
     * Sends a chat object to server and returns the state of the game
     * @param chat
     * @return GameState after chat is sent
     */
    public GameState sendChat(SendChatRequest chat) {
        String result = this.proxy.post("/moves/sendChat", chat);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Sends a roll to the server
     * @param roll
     * @return GameState after roll
     */
    public GameState rollNumber(RollNumberRequest roll) {
        String result = this.proxy.post("/moves/rollNumber", roll);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Sends a request to server to rob player and then returns resulting game state
     * @param robbery
     * @return GameState after robbery takes place
     */
    public GameState robPlayer(RobPlayerRequest robbery) {
        String result = this.proxy.post("/moves/robPlayer", robbery);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Finishes the player turn and sends request to server
     * @param finish
     * @return GameState after the turn has ended
     */
    public GameState finishTurn(FinishTurnRequest finish) {
        String result = this.proxy.post("/moves/finishTurn", finish);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Purchases a dev card and updates server with request
     * @param purchase
     * @return GameState after purchase
     */
    public GameState buyDevCard(BuyDevCardRequest purchase) {
        String result = this.proxy.post("/moves/buyDevCard", purchase);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Plays the year of plenty card for player
     * @param card
     * @return GameState after the card has been played
     */
    public GameState yearOfPlenty(YearOfPlentyCardRequest card) {
        String result = this.proxy.post("/moves/Year_of_Plenty", card);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Plays the RoadBuilding card for the player
     * @param card
     * @return GameState after card is played
     */
    public GameState roadBuilding(RoadBuildingCardRequest card) {
        String result = this.proxy.post("/moves/Road_Building", card);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Plays the Soldier Card for the player
     * @param card
     * @return GameState after card is played
     */
    public GameState soldier(SoldierCardRequest card) {
        String result = this.proxy.post("/moves/Soldier", card);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Plays the monopoly card for player
     * @param card
     * @return GameState after card is played
     */
    public GameState monopoly(MonopolyCardRequest card) {
        String result = this.proxy.post("/moves/Monopoly", card);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Plays Monument card for player
     * @param card
     * @return GameState after card played
     */
    public GameState monument(MonumentCardRequest card) {
        String result = this.proxy.post("/moves/Monument", card);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Builds a road for the player
     * @param request
     * @return GameState after road built
     */
    public GameState buildRoad(BuildRoadRequest request) {
        String result = this.proxy.post("/moves/buildRoad", request);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Builds a settlement for the player
     * @param request
     * @return GameState after settlement built
     */
    public GameState buildSettlement(BuildSettlementRequest request) {
        String result = this.proxy.post("/moves/buildSettlement", request);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Builds a city for the player
     * @param request
     * @return GameState after city built
     */
    public GameState buildCity(BuildCityRequest request) {
        String result = this.proxy.post("/moves/buildCity", request);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Offers a trade to other players
     * @param request
     * @return GameState after trade request
     */
    public GameState offerTrade(OfferTradeRequest request) {
        String result = this.proxy.post("/moves/offerTrade", request);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Accept/Decline an offer from another player
     * @param request
     * @return GameState after accepting request
     */
    public GameState acceptTrade(AcceptTradeRequest request) {
        String result = this.proxy.post("/moves/acceptTrade", request);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Transacts a maritime trade for player
     * @param request
     * @return GameState after trade
     */
    public GameState maritimeTrade(MaritimeTradeRequest request) {
        String result = this.proxy.post("/moves/maritimeTrade", request);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

    /**
     * Discards cards for player
     * @param request
     * @return GameState after discard
     */
    public GameState discardCards(DiscardRequest request) {
        String result = this.proxy.post("/moves/discardCards", request);
        if (result == null) {
            return null;
        }
        return this.deserializer.fromJson(result, GameState.class);
    }

}