package server.handlers.games;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import server.handlers.Handler;
import shared.model.Game;
import shared.request.game.CreateGameRequest;

import java.io.IOException;

/**
 * For handling requests to /games/create
 */
public class CreateHandler extends Handler {

	/**
	 * Expects request body to contain a serialized CreateGameRequest object.
	 * Sends a serialized Game object as the response body.
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		logger.info("CreateHandler Called");
        String body = getRequestBody(exchange);
        CreateGameRequest request = gson.fromJson(body, CreateGameRequest.class);
        Game game = Server.facade.create(request);
        if (game == null) {
            sendTextResponse(exchange, 400, "Invalid game name.");
        } else {
            sendJsonResponse(exchange, 200, gson.toJson(game));
        }
		exchange.close();
	}
}
