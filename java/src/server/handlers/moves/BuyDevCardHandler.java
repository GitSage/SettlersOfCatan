package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.BuyDevCardRequest;

/**
 * For handling requests to /moves/buyDevCard
 */
public class BuyDevCardHandler extends MoveHandler {

	/**
	 * Specific implementation for buyDevCard.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		BuyDevCardRequest request = gson.fromJson(getRequestBody(exchange), BuyDevCardRequest.class);
		return Server.facade.buyDevCard(gameId, request);
	}
}
