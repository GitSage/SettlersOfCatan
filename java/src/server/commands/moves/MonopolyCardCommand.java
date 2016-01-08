package	server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.request.move.MonopolyCardRequest;

/**
 * Executes a Monopoly Card request
 * Created by benja_000 on 3/13/2015.
 */
public class MonopolyCardCommand extends MovesCommand implements Command{

	private ResourceType resource;
	private MonopolyCardRequest request;

	public MonopolyCardCommand(int gameId, MonopolyCardRequest request){
		super(gameId, request.getPlayerIndex());
		this.resource = request.getResource();
		this.request = request;
	}

	/**
	 * Executes a Monopoly Card request
	 * @pre server.commands.PreconditionChecker.canPlayDevCard() returns true
	 * @post all the other players have given the player all their resource cards of the specified type
	 */
	public void execute(){
		log.fine("Executing MonopolyCardCommand");
		getPlayer().setPlayedDevCard(true);
		getPlayer().getNewDevCards().decDevCard(DevCardType.MONOPOLY);

		// get resource from all other players
		for(Player p:getGame().getPlayers()){
			if(p.getPlayerIndex() == playerIndex){
				continue;
			}
			int toAdd = p.getResources().getResource(resource);
			int newAmount = getPlayer().getResources().getResource(resource) + toAdd;
			p.getResources().setResource(resource, 0);

			log.finest("Adding " + toAdd + " to playerIndex " + playerIndex);
			getPlayer().getResources().setResource(resource, newAmount);
		}

		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		ResourceType resource = request.getResource();
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " played a monopoly and stole everyones " + resource );
		return line;
	}
}
