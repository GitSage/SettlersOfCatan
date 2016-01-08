package server.model;

import server.commands.Command;
import shared.model.Game;
import shared.model.GameState;
import java.util.List;

/**
 * Created by benja_000 on 3/21/2015.
 */
public class SaveState {
	public List<Command> commands;
	public GameState gameState;
	public Game game;
	public int id;

	public SaveState(Game game, List<Command> commands, GameState gameState, int id) {
		this.game = game;
		this.commands = commands;
		this.gameState = gameState;
		this.id=id;
	}

	public SaveState(){}
}
