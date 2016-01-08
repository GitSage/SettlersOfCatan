package shared.states.playing;

import client.data.RobPlayerInfo;
import client.proxy.ProxyFacade;
import shared.locations.HexLocation;
import shared.request.move.RobPlayerRequest;

/**
 * Created by Thomas on 2/28/2015.
 */
public class RobState extends State {
    @Override
    public void sendRobRequest(RobPlayerInfo victim) {
        RobPlayerRequest request = new RobPlayerRequest(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(),
                victim.getPlayerIndex(),
                ProxyFacade.getInstance().getPlayingState().getRobHex());
        ProxyFacade.getInstance().robPlayer(request);
    }
}
