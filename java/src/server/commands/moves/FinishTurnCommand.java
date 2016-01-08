package server.commands.moves;

import server.Server;
import server.commands.Command;
import shared.definitions.TurnStatus;
import shared.model.DevCardList;
import shared.model.GameState;
import shared.model.MessageLine;
import shared.model.Player;
import shared.request.move.FinishTurnRequest;

/**
 * Executes a Finish Turn request
 * Created by benja_000 on 3/13/2015.
 */
public class FinishTurnCommand extends MovesCommand implements Command{

	private FinishTurnRequest request;
	public FinishTurnCommand(int gameId, FinishTurnRequest request){
		super(gameId, request.getPlayerIndex());
		this.request = request;
	}

	/**
	 * Executes a Finish Turn request
	 * @pre server.commands.PreconditionChecker.areGeneralPlayingPreconditionsMet() returns true
	 * @post the cards in the player's new dev card hand have been transferred to the player's old dev card hand
	 * @post it is the next player's turn
	 */
	public void execute(){
		log.fine("Executing FinishTurnCommand");
		DevCardList newDevCards = getPlayer().getNewDevCards();
		DevCardList oldDevCards = getPlayer().getOldDevCards();
        getPlayer().setPlayedDevCard(false);

		oldDevCards.setYearOfPlenty(oldDevCards.getYearOfPlenty() + newDevCards.getYearOfPlenty());
		oldDevCards.setSoldier(oldDevCards.getSoldier() + newDevCards.getSoldier());
		oldDevCards.setRoadBuilding(oldDevCards.getRoadBuilding() + newDevCards.getRoadBuilding());
		oldDevCards.setMonopoly(oldDevCards.getMonopoly() + newDevCards.getMonopoly());
		oldDevCards.setMonument(oldDevCards.getMonument() + newDevCards.getMonument());

        getPlayer().setNewDevCards(new DevCardList(0,0,0,0,0));

        // set next player and status
		int nextPlayer = getNextPlayer();
		TurnStatus nextStatus = getNextStatus();
		getGame().getTurnTracker().setCurrentTurn(nextPlayer);
		getGame().getTurnTracker().setStatus(nextStatus);

		shizThatMakesOtherShizWork();
	}

	private int getNextPlayer(){
		TurnStatus currentStatus = getGame().getTurnTracker().getStatus();
		int currentTurn = getGame().getTurnTracker().getCurrentTurn();

		if(currentStatus == TurnStatus.FIRSTROUND &&
				currentTurn == 3){
			return 3;
		}
		if(getGame().getTurnTracker().getStatus() == TurnStatus.SECONDROUND){
			if(getGame().getTurnTracker().getCurrentTurn() > 0){
				return currentTurn - 1;
			}
            if(currentTurn == 0){
                return 0;
            }
		}
		if(playerIndex == 3){
			return 0;
		}
		return playerIndex + 1;
	}

	private TurnStatus getNextStatus(){
		TurnStatus currentStatus = getGame().getTurnTracker().getStatus();
		int currentTurn = getGame().getTurnTracker().getCurrentTurn();

		if(currentStatus == TurnStatus.FIRSTROUND){
			if(currentTurn < 3){
				return TurnStatus.FIRSTROUND;
			}
			if(currentTurn == 3){
				return TurnStatus.SECONDROUND;
			}
		}
		if(currentStatus == TurnStatus.SECONDROUND){
			if(currentTurn > 0){
				return TurnStatus.SECONDROUND;
			}
		}
		return TurnStatus.ROLLING;
	}

	@Override
	public MessageLine getLogLine() {
		MessageLine line = new MessageLine();
		GameState state = Server.state.getGame(gameId);
		Player daPlayer = state.getPlayer(request.getPlayerIndex());
		line.setSource(daPlayer.getName());
		line.setMessage(daPlayer.getName() + " ended their turn");
		return line;
	}
}
