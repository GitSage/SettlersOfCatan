package shared.model;

import client.data.PlayerInfo;

import java.util.List;

/**
 * Model class representing a game.
 */
public class Game {
    private String title;
    private int id;
    private List<PlayerInfo> players;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<PlayerInfo> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerInfo> players) {
        this.players = players;
    }
}
