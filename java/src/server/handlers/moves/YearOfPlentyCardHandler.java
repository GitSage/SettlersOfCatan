package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.YearOfPlentyCardRequest;

/**
 * For handling requests to /moves/Year_of_Plenty
 */
public class YearOfPlentyCardHandler extends MoveHandler {

	/**
	 * Specific implementation for Year_of_Plenty.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		YearOfPlentyCardRequest request = gson.fromJson(getRequestBody(exchange), YearOfPlentyCardRequest.class);
		return Server.facade.yearOfPlenty(gameId, request);
	}
}
