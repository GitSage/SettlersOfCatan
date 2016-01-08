package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.handlers.Handler;
import shared.model.GameState;

import java.io.IOException;

/**
 * Abstract class for handling requests to /moves/*
 */
public abstract class MoveHandler extends Handler {

    /**
     * Expects request body to contain a serialized Request object.
     * Sends a serialized GameState object as the response body.
     *
     * @param exchange the HttpExchange to be handled.
     * @throws java.io.IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
	    try {
		    logger.info(this.getClass().toString() + " Called");
		    if (authGame(exchange)) {
			    int gameId = getGameCookie(exchange);
			    GameState gameState = getResponse(exchange, gameId);
			    sendJsonResponse(exchange, 200, gson.toJson(gameState));
		    } else {
			    sendTextResponse(exchange, 400, "Missing required cookies.");
		    }
		    exchange.close();
	    } catch (Exception ex) {
		    ex.printStackTrace();
	    }
    }

    public abstract GameState getResponse(HttpExchange exchange, int gameId);

}
