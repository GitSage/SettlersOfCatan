package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.TurnStatus;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.model.ResourceList;
import shared.request.move.DiscardRequest;

/**
 * Executes a Discard Cards request
 * Created by benja_000 on 3/13/2015.
 */
public class DiscardCardsCommand extends MovesCommand implements Command {

	private ResourceList discardedCards;
	private boolean lastDiscarder;
	private DiscardRequest request;

	public DiscardCardsCommand(int gameId, DiscardRequest request, boolean lastDiscarder){
		super(gameId, request.getPlayerIndex());
		this.discardedCards = request.getDiscardCard();
		this.lastDiscarder = lastDiscarder;
		this.request = request;
	}

	/**
	 * Executes a DiscardCards command
	 * @pre the status of the client model is "discarding"
	 * @pre the player has over 7 cards
	 * @pre the player has the cards he's choosing to discard
	 * @post the player lost the specified resources
	 * @post if he was the last one to discard, the client model status changes to "robbing"
	 */
	public void execute(){
		log.fine("Executing DiscardCardsCommand");
		getPlayer().getResources().reduce(
				discardedCards.getBrick(),
				discardedCards.getOre(),
				discardedCards.getSheep(),
				discardedCards.getWheat(),
				discardedCards.getWood()
		);

		ResourceList bank = Server.state.getGame(gameId).getBank();
		bank.setBrick(bank.getBrick()+discardedCards.getBrick());
		bank.setOre(bank.getOre()+discardedCards.getOre());
		bank.setSheep(bank.getSheep()+discardedCards.getSheep());
		bank.setWheat(bank.getWheat()+discardedCards.getWheat());
		bank.setWood(bank.getWood()+discardedCards.getWood());

		if(lastDiscarder){
			getGame().getTurnTracker().setStatus(TurnStatus.ROBBING);
		}

		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " discarded");
		return line;
	}
}
