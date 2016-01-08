package server.handlers.games;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import server.handlers.Handler;
import shared.request.game.LoadGameRequest;

import java.io.IOException;

/**
 * For handling requests to /games/load
 */
public class LoadHandler extends Handler {

	/**
	 * Expects request body to contain a serialized LoadGameRequest object.
	 * Saves the current state of the specified game to a file.
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		logger.info("LoadHandler Called");
        String body = getRequestBody(exchange);
        LoadGameRequest request = gson.fromJson(body, LoadGameRequest.class);
        Server.facade.load(request);
        exchange.sendResponseHeaders(200, -1);
		exchange.close();
	}
}
