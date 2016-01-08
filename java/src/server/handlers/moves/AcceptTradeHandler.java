package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.AcceptTradeRequest;

/**
 * For handling requests to /moves/acceptTrade
 */
public class AcceptTradeHandler extends MoveHandler {

	/**
	 * Specific implementation for acceptTrade.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		AcceptTradeRequest request = gson.fromJson(getRequestBody(exchange), AcceptTradeRequest.class);
		return Server.facade.acceptTrade(gameId, request);
	}
}
