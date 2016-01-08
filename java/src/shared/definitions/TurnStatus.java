package shared.definitions;

import com.google.gson.annotations.SerializedName;

/**
 * Created by benja_000 on 1/30/2015.
 */
public enum TurnStatus {
    /**
     * ROLLING: it is time for someone to roll.
     * ROBBING: someone is placing the robber.
     * PLAYING: someone is in the play phase in which they can trade, build, and play development cards.
     * DISCARDING: someone is discarding.
     * FIRSTROUND: the first initial round in which one road and one settlement is placed per player.
     * SECONDROUND: the second initial round in which one road and one settlement is placed per player.
     */
    @SerializedName("Rolling")
    ROLLING,

    @SerializedName("Playing")
    PLAYING,

    @SerializedName("Robbing")
    ROBBING,

    @SerializedName("Discarding")
    DISCARDING,

    @SerializedName("FirstRound")
    FIRSTROUND,

    @SerializedName("SecondRound")
    SECONDROUND
}
