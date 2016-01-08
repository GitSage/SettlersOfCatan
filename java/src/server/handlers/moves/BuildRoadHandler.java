package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.BuildRoadRequest;

/**
 * For handling requests to /moves/buildRoad
 */
public class BuildRoadHandler extends MoveHandler {

	/**
	 * Specific implementation for buildRoad.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		BuildRoadRequest request = gson.fromJson(getRequestBody(exchange), BuildRoadRequest.class);
		return Server.facade.buildRoad(gameId, request);
	}
}
