package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.locations.EdgeLocation;
import shared.model.*;
import shared.request.move.BuildRoadRequest;

/**
 * Executes a Build Road request
 * Created by benja_000 on 3/13/2015.
 */
public class BuildRoadCommand extends MovesCommand implements Command {

	private EdgeLocation edgeLocation;
	private boolean free;
	BuildRoadRequest request;

	public BuildRoadCommand(int gameId, BuildRoadRequest request){
		super(gameId, request.getPlayerIndex());
		this.edgeLocation = request.getRoadLocation();
		this.free = request.isFree();
		this.request = request;
	}

	/**
	 * Executes a Build Road request
	 * @pre the road location is open
	 * @pre the road location is connected to another road owned by the player UNLESS SETUP ROUND
	 * @pre the road location is not on water
	 * @pre you have the required resources (1 wood, 1 brick, 1 road)
	 * @post you lost the resources required to build the road (1 wood, 1 brick, 1 road)
	 * @post the road is on the map at the specified location
	 * @post if applicable, "longest road" has been awarded to the player with the longest road
	 */
	public void execute(){
		log.fine("Executing BuildRoadCommand");
		if(!free){
			getPlayer().getResources().reduce(1, 0, 0, 0, 1);
			ResourceList bank = Server.state.getGame(gameId).getBank();
			bank.setBrick(bank.getBrick()+1);
			bank.setWood(bank.getWood()+1);
		}

		getPlayer().setRoads(getPlayer().getRoads() - 1);
		getGame().getMap().getRoads().add(new Road(playerIndex, edgeLocation));

		getGame().getTurnTracker().updateLongestRoad(gameId);
		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " built a road");
		return line;
	}
}
