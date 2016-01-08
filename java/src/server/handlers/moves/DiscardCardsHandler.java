package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.DiscardRequest;

/**
 * For handling requests to /moves/discardCards
 */
public class DiscardCardsHandler extends MoveHandler {

	/**
	 * Specific implementation for discardCards.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		DiscardRequest request = gson.fromJson(getRequestBody(exchange), DiscardRequest.class);
		return Server.facade.discardCards(gameId, request);
	}
}
