package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.BuildSettlementRequest;

/**
 * For handling requests to /moves/buildSettlement
 */
public class BuildSettlementHandler extends MoveHandler {

	/**
	 * Specific implementation for buildSettlement.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		BuildSettlementRequest request = gson.fromJson(getRequestBody(exchange), BuildSettlementRequest.class);
		return Server.facade.buildSettlement(gameId, request);
	}
}
