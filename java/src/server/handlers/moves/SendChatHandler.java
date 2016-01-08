package server.handlers.moves;

import com.sun.net.httpserver.HttpExchange;
import server.Server;
import shared.model.GameState;
import shared.request.move.SendChatRequest;

/**
 * For handling requests to /moves/sendChat
 */
public class SendChatHandler extends MoveHandler {

	/**
	 * Specific implementation for sendChat.
	 * @param exchange
	 * @param gameId
	 * @return
	 */
	public GameState getResponse(HttpExchange exchange, int gameId) {
		SendChatRequest request = gson.fromJson(getRequestBody(exchange), SendChatRequest.class);
		return Server.facade.sendChat(gameId, request);
	}
}
