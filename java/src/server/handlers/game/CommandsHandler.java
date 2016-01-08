package server.handlers.game;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import server.commands.Command;
import server.handlers.Handler;
import shared.model.GameState;
import shared.request.move.MoveRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * For handling requests to /game/commands
 */
public class CommandsHandler extends Handler {

	/**
	 *
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		logger.info("Commands Handle Called");
		if (authGame(exchange)) {
			int gameId = getGameCookie(exchange);
			if (exchange.getRequestMethod().equals("POST")) {
				String bodyString = getRequestBody(exchange);
				Command[] commands = gson.fromJson(bodyString, Command[].class);
				GameState gameState = Server.facade.commands(gameId, Arrays.asList(commands));
				sendJsonResponse(exchange, 200, gson.toJson(gameState));
			} else {
				List<Command> commands = Server.facade.commands(gameId);
				sendJsonResponse(exchange, 200, gson.toJson(commands));
			}
		} else {
			sendTextResponse(exchange, 400, "Missing required cookies.");
		}
		exchange.close();

	}
}
