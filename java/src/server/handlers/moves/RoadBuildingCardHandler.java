package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.RoadBuildingCardRequest;

/**
 * For handling requests to /moves/Road_Building
 */
public class RoadBuildingCardHandler extends MoveHandler {

	/**
	 * Specific implementation for Road_Building.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		RoadBuildingCardRequest request = gson.fromJson(getRequestBody(exchange), RoadBuildingCardRequest.class);
		return Server.facade.roadBuilding(gameId, request);
	}
}
