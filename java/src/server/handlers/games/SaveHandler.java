package server.handlers.games;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import server.handlers.Handler;
import shared.request.game.SaveGameRequest;

import java.io.IOException;

/**
 * For handling requests to /games/save
 */
public class SaveHandler extends Handler {

	/**
	 * Expects request body to contain a serialized SaveRequest object.
	 * Saves the current state of the specified game to a file.
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
        logger.info("SaveHandler Called");
        String body = getRequestBody(exchange);
        SaveGameRequest request = gson.fromJson(body, SaveGameRequest.class);
        Server.facade.save(request);
        exchange.sendResponseHeaders(200, -1);
        exchange.close();
	}
}
