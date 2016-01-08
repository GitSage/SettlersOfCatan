package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.model.ResourceList;
import shared.request.move.YearOfPlentyCardRequest;

/**
 * Command that responds to a Year of Plenty request.
 * Created by benja_000 on 3/13/2015.
 */
public class YearOfPlentyCommand extends MovesCommand implements Command {

	private ResourceType resource1;
	private ResourceType resource2;
	private YearOfPlentyCardRequest request;

	public YearOfPlentyCommand(int gameId, YearOfPlentyCardRequest request){
		super(gameId, request.getPlayerIndex());
		this.resource1 = request.getResource1();
		this.resource2 = request.getResource2();
		this.request = request;
	}

	/**
	 * Executes the Year of Command action.
	 * @pre server.commands.PreconditionChecker.canPlayDevCard() returns true
	 * #pre the two specified resources are in the bank
	 * @post Modifies the GameState in the following ways:
	 * <ul>
	 *	 <li>Decrements the player's Year of Plenty dev card count</li>
	 *	 <li>Increments the two resources specified by the player</li>
	 * </ul>
	 */
	public void execute(){
		log.fine("Executing YearOfPlentyCommand");
		getPlayer().setPlayedDevCard(true);

		getPlayer().getNewDevCards().decDevCard(DevCardType.YEAR_OF_PLENTY);

		ResourceList resources = getPlayer().getResources();
		resources.setResource(resource1, resources.getResource(resource1) + 1);
		resources.setResource(resource2, resources.getResource(resource2) + 1);

        getGame().getBank().setResource(resource1, resources.getResource(resource1) -1);
        getGame().getBank().setResource(resource2, resources.getResource(resource2) -1);

        shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		ResourceType resource1 = request.getResource1();
		ResourceType resource2 = request.getResource2();
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " used a Year of Plenty and got a " + resource1 + " and a " + resource2);
		return line;
	}
}
