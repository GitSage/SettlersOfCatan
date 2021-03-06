package server.handlers.game;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import server.handlers.Handler;
import shared.model.GameState;

import java.io.IOException;

/**
 * For handling requests to /game/reset
 */
public class ResetHandler extends Handler {

	/**
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		logger.info("Reset Handle Called");
		if (authGame(exchange)) {
			int gameId = getGameCookie(exchange);
			GameState gameState = Server.facade.reset(gameId);
			sendJsonResponse(exchange, 200, gson.toJson(gameState));
		} else {
			sendTextResponse(exchange, 400, "Missing required cookies.");
		}
		exchange.close();
	}
}
