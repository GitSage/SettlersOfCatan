package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.ResourceType;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.model.ResourceList;
import shared.request.move.MaritimeTradeRequest;

/**
 * Executes a maritime trade
 * Created by benja_000 on 3/13/2015.
 */
public class MaritimeTradeCommand extends MovesCommand implements Command {

	private int ratio;
	private ResourceType inputResource;
	private ResourceType outputResource;
	private MaritimeTradeRequest request;

	public MaritimeTradeCommand(int gameId, MaritimeTradeRequest request){
		super(gameId, request.getPlayerIndex());
		this.ratio = request.getRatio();
		this.inputResource = request.getInputResource();
		this.outputResource = request.getOutputResource();
		this.request = request;
	}

	/**
	 * Executes the Maritime Trade command
	 * @pre server.commands.PreconditionChecker.areGeneralPlayingPreconditionsMet() returns true
	 * @pre you have the resources you are giving
	 * @pre for ratios less than 4, you have the correct port for trade
	 * @post the trade has been executed
	 */
	public void execute(){
		log.fine("Executing MaritimeTradeCommand");
		ResourceList rl = getPlayer().getResources();
		rl.setResource(inputResource, rl.getResource(inputResource) - ratio);

		//update bank
        getGame().getBank().setResource(inputResource, getGame().getBank().getResource(inputResource) + ratio);
        getGame().getBank().setResource(outputResource, getGame().getBank().getResource(outputResource) - 1);        
        
		rl.setResource(outputResource, rl.getResource(outputResource) + 1);
		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " did a maritime trade ");
		return line;
	}
}
