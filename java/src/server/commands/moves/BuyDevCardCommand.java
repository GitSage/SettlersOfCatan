package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.DevCardType;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.model.ResourceList;
import shared.request.move.BuyDevCardRequest;

/**
 * Executes a BuyDevCard request
 * Created by benja_000 on 3/13/2015.
 */
public class BuyDevCardCommand extends MovesCommand implements Command{

	private BuyDevCardRequest request;
	public BuyDevCardCommand(int gameId, BuyDevCardRequest request){
		super(gameId, request.getPlayerIndex());
		this.request = request;
	}

	/**
	 * @pre the player has the required resources (1 ore, 1 wheat, 1 sheep)
	 * @pre there are dev cards left in the deck
	 * @post the player has a new dev card
	 */
	public void execute(){
		log.fine("Executing BuyDevCardCommand");
		DevCardType newCard = getGame().getRandomDevCardFromBank();
		getPlayer().getNewDevCards().incDevCard(newCard);
		
		ResourceList playerResources = getPlayer().getResources();
		playerResources.setOre(playerResources.getOre() - 1);
		playerResources.setWheat(playerResources.getWheat() - 1);
		playerResources.setSheep(playerResources.getSheep() - 1);
		
		ResourceList bank = Server.state.getGame(gameId).getBank();
		bank.setOre(bank.getOre()+1);
		bank.setWheat(bank.getWheat()+1);
		bank.setSheep(bank.getSheep()+1);
		
		shizThatMakesOtherShizWork();
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " bought a dev card");
		return line;
	}
}
