package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.MonopolyCardRequest;

/**
 * For handling requests to /moves/Monopoly
 */
public class MonopolyCardHandler extends MoveHandler {

	/**
	 * Specific implementation for Monopoly.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		MonopolyCardRequest request = gson.fromJson(getRequestBody(exchange), MonopolyCardRequest.class);
		return Server.facade.monopoly(gameId, request);
	}
}
