package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.SoldierCardRequest;

/**
 * For handling requests to /moves/Soldier
 */
public class SoldierCardHandler extends MoveHandler {

	/**
	 * Specific implementation for Soldier.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		SoldierCardRequest request = gson.fromJson(getRequestBody(exchange), SoldierCardRequest.class);
		return Server.facade.soldier(gameId, request);
	}
}
