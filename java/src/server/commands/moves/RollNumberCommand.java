package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.TurnStatus;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.request.move.RollNumberRequest;

/**
 * Executes a Roll Number request
 * Created by benja_000 on 3/13/2015.
 */
public class RollNumberCommand extends MovesCommand implements Command {

	int number;
	RollNumberRequest request;

	public RollNumberCommand(int gameId, RollNumberRequest request){
		super(gameId, request.getPlayerIndex());
		this.number = request.getNumber();
		this.request = request;
	}

	/**
	 * Executes a Roll Number request
	 * @pre it is the player's turn
	 * @pre the client model's status is "Rolling"
	 * @post the client model's status is now "Discarding" or "Robbing" or "Playing"
	 * @post the roller has been given extra resources
	 */
	public void execute(){
		log.fine("Executing RollNumberCommand");
		if(number == 7){ //robbing
			log.fine("A seven was rolled");
			// check to see if we need to discard
			boolean needToDiscard = false;
			for(Player p : getGame().getPlayers()){
				if(p.getTotalNumCards() > 7){
					log.fine("a player needs to discard");
					needToDiscard = true;
				}
			}

			if(needToDiscard){
				log.finest("Setting TurnStatus to DISCARDING");
				getGame().getTurnTracker().setStatus(TurnStatus.DISCARDING);
			}
			else{
				log.finest("Setting TurnStatus to ROBBING");
				getGame().getTurnTracker().setStatus(TurnStatus.ROBBING);
			}
			shizThatMakesOtherShizWork();
			return;
		}
		log.finest("Setting TurnStatus to PLAYING");
		getGame().getTurnTracker().setStatus(TurnStatus.PLAYING);

		// give resources
		getGame().updatePlayerResources(number);

		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " rolled a " + request.getNumber());
		return line;
	}

}
