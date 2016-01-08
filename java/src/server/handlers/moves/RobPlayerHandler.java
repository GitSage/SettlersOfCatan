package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.RobPlayerRequest;

/**
 * For handling requests to /moves/robPlayer
 */
public class RobPlayerHandler extends MoveHandler {

	/**
	 * Specific implementation for robPlayer.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		RobPlayerRequest request = gson.fromJson(getRequestBody(exchange), RobPlayerRequest.class);
		return Server.facade.robPlayer(gameId, request);
	}
}
