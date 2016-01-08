package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.DevCardType;
import shared.locations.HexLocation;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.request.move.SoldierCardRequest;

/**
 * Command that responds to a Play Soldier Card request.
 * Created by benja_000 on 3/13/2015.
 */
public class SoldierCardCommand extends MovesCommand implements Command {

	private int victimIndex;
	private HexLocation location;
	private SoldierCardRequest request;

	public SoldierCardCommand(int gameId, SoldierCardRequest request) {
		super(gameId, request.getPlayerIndex());
		this.victimIndex = request.getVictimIndex();
		this.location = request.getLocation();
		this.request = request;
	}

	/**
	 * Executes the Play Soldier Card action.
	 * @pre server.commands.PreconditionChecker.canPlayDevCard() returns true
	 * @pre The robber is not being kept in the same location
	 * @pre If someone is being robbed (victimIndex != -1), the player being robbed has at least one resource card
	 * @post the GameState is modified in the following ways:
	 * <ul>
	 *	 <li>The robber is in the new location</li>
	 *	 <li>The player being robbed (if any) gave you one of his resource cards (randomly selected)</li>
	 *	 <li>If applicable, "largest army" has been awarded to the player who has played the most solider cards</li>
	 *	 <li>You are not allowed to play other development cards during this turn, except for monument cards</li>
	 *	 <li>The player has lost one dev card</li>
	 * </ul>
	 */
	public void execute(){
		log.fine("Executing SoldierCardCommand");
		getGame().getPlayer(playerIndex).setSoldiers(getGame().getPlayer(playerIndex).getSoldiers()+1);
		getMap().setRobber(location);
		if(victimIndex != -1){
			getPlayer().robPlayer(getGame().getPlayer(victimIndex));
		}

		getGame().getTurnTracker().updateLargestArmy(gameId);

		getPlayer().setPlayedDevCard(true);

		getPlayer().getNewDevCards().decDevCard(DevCardType.SOLDIER);

		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		if (request.getVictimIndex() > -1) {
			Player victim = state.getPlayer(request.getVictimIndex());
			line.setMessage(daPlayer.getName() + " played a soldier card and robbed " + victim.getName());
		} else {
			line.setMessage(daPlayer.getName() + " played a soldier card and couldn't rob anyone");
		}
		return line;
	}
}