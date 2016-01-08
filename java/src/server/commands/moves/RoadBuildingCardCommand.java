package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.DevCardType;
import shared.locations.EdgeLocation;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.model.Road;
import shared.request.move.RoadBuildingCardRequest;

/**
 * Executes the Road Building card
 * Created by benja_000 on 3/13/2015.
 */
public class RoadBuildingCardCommand extends MovesCommand implements Command {

	private EdgeLocation spot1;
	private EdgeLocation spot2;
	private RoadBuildingCardRequest request;

	public RoadBuildingCardCommand(int gameId, RoadBuildingCardRequest request){
		super(gameId, request.getPlayerIndex());
		this.spot1 = request.getSpot1();
		this.spot2 = request.getSpot2();
		this.request = request;
	}

	/**
	 * Uses a Road Building card
	 * @pre server.commands.PreconditionChecker.canPlayDevCard() returns true
	 * @pre the first road location (spot1) is connected to one of the player's roads
	 * @pre the second road location (spot2) is connected to one of the player's roads or to spot1
	 * @pre neither location is on water
	 * @pre you have at least two unused roads
	 * @post you have two fewer unused roads
	 * @post two new roads appear on the map at the specified locations
	 * @post if applicable, "longest road" has been awarded to the player with the longest road
	 */
	public void execute(){
		log.fine("Executing RoadBuildingCardCommand");
		getPlayer().setRoads(getPlayer().getRoads()-2);

		getMap().getRoads().add(new Road(playerIndex, spot1));
		getMap().getRoads().add(new Road(playerIndex, spot2));

		getPlayer().setPlayedDevCard(true);
		getPlayer().getNewDevCards().decDevCard(DevCardType.ROAD_BUILD);

		getGame().getTurnTracker().updateLongestRoad(gameId);

		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " Played a build road card.");
		return line;
	}
}
