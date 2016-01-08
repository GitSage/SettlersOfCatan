package server.handlers.user;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import server.handlers.Handler;
import server.model.User;
import shared.request.game.UserRequest;

import java.io.IOException;

/**
 * For handling requests to /user/register
 */
public class RegisterHandler extends Handler {

	/**
	 * Expects request body to contain a serialized UserRequest object.
	 * Sets the catan.user HTTP cookie.
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
        logger.info("RegisterHandler Called");
        String body = getRequestBody(exchange);
        UserRequest userRequest = gson.fromJson(body, UserRequest.class);
        User user = Server.facade.register(userRequest);
        if (user == null) {
            sendTextResponse(exchange, 400, "Invalid username or password.");
        } else {
            setCookie(exchange, "catan.user", gson.toJson(user));
            sendTextResponse(exchange,200, "Success");
        }
        exchange.close();
	}
}
