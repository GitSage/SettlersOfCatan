package server.handlers.game;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import server.handlers.Handler;
import shared.model.GameState;

import java.io.IOException;

/**
 * For handling requests to /game/model
 */
public class ListAiHandler extends Handler {

	/**
	 * Sends a serialized GameState object as the response body.
	 * @param exchange
	 * @throws java.io.IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		logger.info("Model Handle Called");
		if (authGame(exchange)) {
			sendJsonResponse(exchange, 200, "[\"Don't click me anoyance.\"]");
		} else {
			sendTextResponse(exchange, 400, "Missing required cookies.");
		}
		exchange.close();
	}
}
