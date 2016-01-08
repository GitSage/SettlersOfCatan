package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.DevCardType;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.request.move.MonumentCardRequest;

/**
 * Executes a Monument Card request
 * Created by benja_000 on 3/13/2015.
 */
public class MonumentCardCommand extends MovesCommand implements Command{

	private MonumentCardRequest request;

	public MonumentCardCommand(int gameId, MonumentCardRequest request){
		super(gameId, request.getPlayerIndex());
		this.request = request;
	}

	/**
	 * Executes a Monument Card request
	 * @pre server.commands.PreconditionChecker.canPlayDevCard() returns true
	 * @pre the player has at least one monument card
	 * @post the player gains a victory point
	 */
	public void execute(){
		log.fine("Executing MonumentCardCommand");
		getPlayer().setVictoryPoints(getPlayer().getVictoryPoints() + 1);
		getPlayer().getOldDevCards().decDevCard(DevCardType.MONUMENT);
		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " built a monument and gained a victory point!");
		return line;
	}
}
