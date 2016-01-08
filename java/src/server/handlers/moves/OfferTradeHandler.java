package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.OfferTradeRequest;

/**
 * For handling requests to /moves/offerTrade
 */
public class OfferTradeHandler extends MoveHandler {

	/**
	 * Specific implementation for offerTrade.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		OfferTradeRequest request = gson.fromJson(getRequestBody(exchange), OfferTradeRequest.class);
		return Server.facade.offerTrade(gameId, request);
	}
}
