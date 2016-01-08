package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.FinishTurnRequest;

/**
 * For handling requests to /moves/finishTurn
 */
public class FinishTurnHandler extends MoveHandler {

	/**
	 * Specific implementation for finishTurn.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		FinishTurnRequest request = gson.fromJson(getRequestBody(exchange), FinishTurnRequest.class);
		return Server.facade.finishTurn(gameId, request);
	}
}
