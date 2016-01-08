package server.handlers.util;

import com.sun.net.httpserver.HttpExchange;

import server.Server;
import server.handlers.Handler;
import shared.request.game.ChangeLogLevelRequest;

import java.io.IOException;

/**
 * For handling requests to /util/changeLogLevel
 */
public class ChangeLogLevelHandler extends Handler {

	/**
	 * Expects request body to contain a serialized ChangeLogLevelRequest object.
	 * Sends a serialized __ object as the response body.
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		logger.info("ChangeLogLevelHandler Called");
		String body = getRequestBody(exchange);
		ChangeLogLevelRequest request = gson.fromJson(body, ChangeLogLevelRequest.class);
		Boolean result = Server.facade.changeLogLevel(request);
		if (result) {
			sendTextResponse(exchange,200, "Success");
		}
		else {
			sendTextResponse(exchange, 400, "Invalid log level: " + request.getLogLevel());
		}
		exchange.close();
	}
}
