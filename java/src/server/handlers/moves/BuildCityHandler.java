package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.BuildCityRequest;

/**
 * For handling requests to /moves/buildCity
 */
public class BuildCityHandler extends MoveHandler {

	/**
	 * Specific implementation for buildCity.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		BuildCityRequest request = gson.fromJson(getRequestBody(exchange), BuildCityRequest.class);
		return Server.facade.buildCity(gameId, request);
	}
}
