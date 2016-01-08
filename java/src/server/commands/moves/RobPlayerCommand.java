package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.TurnStatus;
import shared.locations.HexLocation;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.request.move.RobPlayerRequest;

/**
 * Executes a Rob Player request
 * Created by benja_000 on 3/13/2015.
 */
public class RobPlayerCommand extends MovesCommand implements Command {

	private int victimIndex;
	private HexLocation location;
	private RobPlayerRequest request;

	public RobPlayerCommand(int gameId, RobPlayerRequest request){
		super(gameId, request.getPlayerIndex());
		this.victimIndex = request.getVictimIndex();
		this.location = request.getLocation();
		this.request = request;
	}

	/**
	 * Executes a Rob Player request.
	 * @pre server.commands.PreconditionChecker.areGeneralPlayingPreconditionsMet() returns true
	 * @pre the robber is not being kept in the same location
	 * @pre if a player is being robbed (i.e., victimIndex != -1), the player being robbed has resource cards
	 * @post the robber is in the new location
	 * @post the player being robbed (if any) gave you one of his resource cards (randomly selected)
	 */
	public void execute(){
		log.fine("Executing RobPlayerCommand");
		getMap().setRobber(location);

		if(victimIndex >= 0 && victimIndex <= 3) {
			Player player = getGame().getPlayer(victimIndex);
			getPlayer().robPlayer(player);	
		}

		log.finest("Setting TurnStatus to PLAYING");
		getGame().getTurnTracker().setStatus(TurnStatus.PLAYING);
		
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
			line.setMessage(daPlayer.getName() + " moved robber and robbed " + victim.getName());
		} else {
			line.setMessage(daPlayer.getName() + " moved robber and couldn't rob anyone");
		}
		return line;
	}
}
