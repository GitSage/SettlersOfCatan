package server.handlers.games;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import server.handlers.Handler;
import server.model.User;
import shared.request.game.JoinGameRequest;

import java.io.IOException;

/**
 * For handling requests to /games/join
 */
public class JoinHandler extends Handler {

	/**
	 * Expects request body to contain a serialized JoinGameRequest object.
	 * Sets the catan.game HTTP cookie.
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		logger.info("JoinHandler Called");
        if (authUser(exchange)) {
	        User user = getUserCookie(exchange);
            String body = getRequestBody(exchange);
            JoinGameRequest request = gson.fromJson(body, JoinGameRequest.class);
            Server.facade.join(user.getUserId(), request);
            setCookie(exchange, "catan.game", String.valueOf(request.getId()));
            sendTextResponse(exchange, 200, "Success");
        } else {
            sendTextResponse(exchange, 400, "Missing required cookie.");
		}
		exchange.close();
	}
}
