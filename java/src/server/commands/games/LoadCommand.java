package server.commands.games;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.Server;
import server.commands.Command;
import server.model.SaveState;
import shared.adapter.*;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.Hex;
import shared.model.Port;
import shared.request.game.LoadGameRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Executes a Load Game request
 * Created by benja_000 on 3/13/2015.
 */
public class LoadCommand implements Command {
	private String name;

	public LoadCommand(LoadGameRequest request){
		this.name = request.getName();
	}

	/**
	 * Executes a Load command.
	 * @pre a saved game file with the specified name already exists.
	 * @post the specified game has been loaded and its state restored (including its ID).
	 */
	public void execute(){
		Logger.getLogger("catanserver").fine("Executing a LoadCommand");
		String content = null;
		try {
			content = new Scanner(new File("saves"+File.separator+name)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Command.class, new CommandTypeAdapter())
				.create();
		try {
			SaveState saveState = gson.fromJson(content, SaveState.class);
			Server.state.getTitles().remove(saveState.id);
			Server.state.getCommands().remove(saveState.id);
			Server.state.getGames().remove(saveState.id);

			Server.state.getTitles().put(saveState.id, saveState.game);
			Server.state.getCommands().put(saveState.id, saveState.commands);
			Server.state.getGames().put(saveState.id, saveState.gameState);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
