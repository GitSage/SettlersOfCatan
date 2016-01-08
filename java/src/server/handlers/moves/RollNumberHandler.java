package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.RollNumberRequest;

/**
 * For handling requests to /moves/rollNumber
 */
public class RollNumberHandler extends MoveHandler {

	/**
	 * Specific implementation for rollNumber.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		RollNumberRequest request = gson.fromJson(getRequestBody(exchange), RollNumberRequest.class);
		return Server.facade.rollNumber(gameId, request);
	}
}
