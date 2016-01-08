package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.MaritimeTradeRequest;

/**
 * For handling requests to /moves/maritimeTrade
 */
public class MaritimeTradeHandler extends MoveHandler {

	/**
	 * Specific implementation for maritimeTrade.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		MaritimeTradeRequest request = gson.fromJson(getRequestBody(exchange), MaritimeTradeRequest.class);
		return Server.facade.maritimeTrade(gameId, request);
	}
}
