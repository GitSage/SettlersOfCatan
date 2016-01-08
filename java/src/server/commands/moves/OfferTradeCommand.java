package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.model.*;
import shared.request.move.OfferTradeRequest;

/**
 * Executes the Offer Trade command
 * Created by benja_000 on 3/13/2015.
 */
public class OfferTradeCommand extends MovesCommand implements Command{

	private ResourceList offer;
	private int receiver;
	private OfferTradeRequest request;

	public OfferTradeCommand(int gameId, OfferTradeRequest request){
		super(gameId, request.getPlayerIndex());
		this.offer = request.getOffer();
		this.receiver = request.getReceiver();
		this.request = request;
	}

	/**
	 * Offers a trade
	 * @pre server.commands.PreconditionChecker.areGeneralPlayingPreconditionsMet() returns true
	 * @pre the player has the resources he is offering
	 * @post the trade is offered to the other player (stored in the server model)
	 */
	public void execute(){
		log.fine("Executing OfferTradeCommand");
		TradeOffer o = new TradeOffer();
		o.setOffer(offer);
		o.setReceiver(receiver);
		o.setSender(playerIndex);

        getGame().setTradeOffer(o);

		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		Player receiver = state.getPlayer(request.getReceiver());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " Offered to trade with " + receiver.getName());
		return line;
	}
}
