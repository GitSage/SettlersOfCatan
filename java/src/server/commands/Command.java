package server.commands;

/**
 * Interface for a Command.
 * Contains only one method, which executes the required action.
 * A Command will be called in response to a request sent by a client.
 * Created by benja_000 on 3/13/2015.
 */
public interface Command {
    /**
     * Executes the required action. Most of the time this will modify a GameState in some way.
     */
    public void execute();
}
