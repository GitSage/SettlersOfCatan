package server.handlers.games;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import server.handlers.Handler;
import shared.model.Game;

import java.io.IOException;
import java.util.List;

/**
 * For handling requests to /games/list
 */
public class ListHandler extends Handler {

	/**
	 * Sends a serialized array of Game objects as the response body.
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		logger.info("ListHandler Called");
        List<Game> games = Server.facade.list();
        sendJsonResponse(exchange, 200, gson.toJson(games));
		exchange.close();
	}
}
