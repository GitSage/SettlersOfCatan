package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.MonumentCardRequest;

/**
 * For handling requests to /moves/Monument
 */
public class MonumentCardHandler extends MoveHandler {

	/**
	 * Specific implementation for Monument.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		MonumentCardRequest request = gson.fromJson(getRequestBody(exchange), MonumentCardRequest.class);
		return Server.facade.monument(gameId, request);
	}
}
