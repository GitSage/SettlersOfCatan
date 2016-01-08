package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.TurnStatus;
import shared.locations.VertexLocation;
import shared.model.*;
import shared.request.move.BuildSettlementRequest;

/**
 * Executes a Build Settlement request
 * Created by benja_000 on 3/13/2015.
 */
public class BuildSettlementCommand extends MovesCommand implements Command {

	private VertexLocation vertexLocation;
	private boolean free;
	private BuildSettlementRequest request;

	public BuildSettlementCommand(int gameId, BuildSettlementRequest request){
		super(gameId, request.getPlayerIndex());

		this.vertexLocation = request.getVertexLocation();
		this.free = request.isFree();
		this.request = request;
	}

	/**
	 * Executes a Build Settlement request
	 * @pre the settlement location is open
	 * @pre the settlement location is not on water
	 * @pre the settlement location is connected to one of your roads except during setup
	 * @pre you have the required resources (1 wood, 1 brick, 1 wheat, 1 sheep, 1 settlement)
	 * @pre the settlement cannot be placed adjacent to another settlement
	 * @post you lost the resources required to build the settlement
	 * @post the settlement is on the map at the specified location
	 */
	public void execute(){
		log.fine("Executing BuildSettlementCommand");

		VertexObject newSettlement = new VertexObject(playerIndex, vertexLocation);

		if(!free){
			getPlayer().getResources().reduce(1, 0, 1, 1, 1);
			
			ResourceList bank = Server.state.getGame(gameId).getBank();
			bank.setBrick(bank.getBrick()+1);
			bank.setSheep(bank.getSheep()+1);
			bank.setWheat(bank.getWheat()+1);
			bank.setWood(bank.getWood()+1);
		}

		// if settlement built during second setup phase, give them resources
		TurnStatus currentStatus = getGame().getTurnTracker().getStatus();
		if(currentStatus == TurnStatus.SECONDROUND){
			// add player resources
			getGame().updatePlayerResources(getGame().getPlayer(playerIndex), newSettlement);
		}

		getPlayer().setSettlements(getPlayer().getSettlements() - 1);
		getGame().getMap().getSettlements().add(newSettlement);
		getPlayer().setVictoryPoints(getPlayer().getVictoryPoints() + 1);
		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " built a settlement");
		return line;
	}
}
