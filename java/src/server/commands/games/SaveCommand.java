package server.commands.games;

import com.google.gson.Gson;
import server.Server;
import server.commands.Command;
import server.model.SaveState;
import shared.request.game.SaveGameRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Executes a Save Game request
 * Created by benja_000 on 3/13/2015.
 */
public class SaveCommand implements Command {
	private int id;
	private String name;

	public SaveCommand(SaveGameRequest request){
		this.id = request.getId();
		this.name = request.getName();
	}

	/**
	 * Executes a Save command.
	 * @pre the game ID is valid
	 * @pre the file name is valid
	 * @post the state of the game has been saved.
	 */
	public void execute(){
		Logger.getLogger("catanserver").fine("Executing SaveCommand");
		SaveState saveState = new SaveState(Server.state.getTitles().get(id),
				Server.state.getCommands().get(id),
				Server.state.getGame(id), id);
		String output = new Gson().toJson(saveState);
		PrintWriter out = null;
		try {
			new File("saves").mkdirs();
			out = new PrintWriter("saves"+ File.separator+name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		out.write(output);
		out.close();
	}
}
