package server.commands.moves;

import java.util.logging.Logger;

import server.Server;
import server.commands.Command;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.request.move.SendChatRequest;

/**
 * Executes a Send Chat request.
 * Created by benja_000 on 3/13/2015.
 */
public class SendChatCommand extends MovesCommand implements Command{

	private String content;
	private SendChatRequest request;

	public SendChatCommand(int gameId, SendChatRequest request){
		super(gameId, request.getPlayerIndex());
		content = request.getContent();
		Logger.getLogger("catanserver").finer("send chat command has been created");
		this.request = request;
	}

	/**
	 * Executes a Send Chat request.
	 * @pre none (this command may be executed at any time by any player)
	 * @post the message has been added to the chat history
	 */
	public void execute(){
		log.fine("Executing SendChatCommand");
		MessageLine message = new MessageLine();
		message.setMessage(content);
		message.setSource(Server.state.getGame(gameId).getPlayer(playerIndex).getName());
		getGame().getChat().getLines().add(message);

		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		line.setIgnoreMe(true);
		return line;
	}
}
