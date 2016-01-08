package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.model.TradeOffer;
import shared.request.move.AcceptTradeRequest;

/**
 * Executes a Accept Trade request
 * Created by benja_000 on 3/13/2015.
 */
public class AcceptTradeCommand extends MovesCommand implements Command {

	private boolean willAccept;
	private AcceptTradeRequest request;

	public AcceptTradeCommand(int gameId, AcceptTradeRequest request){
		super(gameId, request.getPlayerIndex());
		this.willAccept = request.isWillAccept();
		this.request = request;
	}

	/**
	 * Executes a Accept Trade request
	 * @pre you have been offered a domestic trade
	 * @pre to accept the offered trade, you have the required resources
	 * @post if you accepted, you and the player who offered swap the specified resources
	 * @post if you declined no resources are exchanged
	 * @post the trade offer is removed
	 */
	public void execute(){
		log.fine("Executing AcceptTradeCommand");
		TradeOffer offer = getGame().getTradeOffer();

		if(willAccept){
			getPlayer(offer.getSender(), gameId).getResources().reduce(offer);
			getPlayer(offer.getReceiver(), gameId).getResources().increase(offer);
		}

		getGame().setTradeOffer(null);

		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		if ( request.isWillAccept() ) {
			line.setMessage(daPlayer.getName() + " Accepted the trade. ");
		} else {
			line.setMessage(daPlayer.getName() + " Declined the trade. ");
		}
		return line;
	}
}
