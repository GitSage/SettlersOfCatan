package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.locations.VertexLocation;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.model.ResourceList;
import shared.request.move.BuildCityRequest;

/**
 * Executes a Build City request
 * Created by benja_000 on 3/13/2015.
 */
public class BuildCityCommand extends MovesCommand implements Command {

	private VertexLocation vertexLocation;
	private boolean free;
	private BuildCityRequest request;

	public BuildCityCommand(int gameId, BuildCityRequest request){
		super(gameId, request.getPlayerIndex());
		this.vertexLocation = request.getVertexLocation();
		this.free = request.isFree();
		this.request = request;
	}

	/**
	 * Executes a Build City request
	 * @pre the city location is where you currently have a settlement
	 * @pre you have the required resources (2 wheat, 3 ore, 1 city)
	 * @post you lost the resources required to build the city
	 * @post the city is on the map at the specified location
	 * @post you got a settlement back
	 */
	public void execute(){
		log.fine("Executing BuildCityCommand");

		// update resources
		if(!free){ // these resources are not reduced if free
			getPlayer().getResources().reduce(0, 3, 0, 2, 0);
			
			ResourceList bank = Server.state.getGame(gameId).getBank();
			bank.setOre(bank.getOre()+3);
			bank.setWheat(bank.getWheat()+2);
		}
		getPlayer().setCities(getPlayer().getCities() - 1);
		getPlayer().setSettlements(getPlayer().getSettlements() + 1);
		getPlayer().setVictoryPoints(getPlayer().getVictoryPoints() + 1);

		// remove settlement, add city
		getGame().getMap().replaceSettlementWithCity(vertexLocation);

		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " upgraded a city");
		return line;
	}
}
