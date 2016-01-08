package client.communication;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import shared.model.GameState;
import shared.request.move.SendChatRequest;
import client.base.*;
import client.misc.IMessageView;
import client.misc.MessageView;
import client.proxy.ProxyFacade;


/**
 * Chat controller implementation
 */
public class ChatController extends Controller implements IChatController, Observer {

	private IMessageView messageView = new MessageView();
	
	public ChatController(IChatView view) {
		
		super(view);
		GameState.get().addObserver(this);
		initFromModel();
	}

	@Override
	public IChatView getView() {
		return (IChatView)super.getView();
	}

	@Override
	public void sendMessage(String message) {
		
		String validMessage = "^[a-zA-Z0-9_\\- !.,-?()*&/<>:;^%$#@]+$";
		
		if(message.length() > 250) {
			//error
			messageView.setTitle("Error");
			messageView.setMessage("Max 250 characters allows in chat!");
			messageView.showModal();
			return;
		}
		else if(!message.matches(validMessage)) {
			messageView.setTitle("Error");
			messageView.setMessage("Invalid characters in chat!");
			messageView.showModal();
			return;
		}
		
		int playerIndex = ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex();
		SendChatRequest request = new SendChatRequest(playerIndex,message);
		GameState newState = ProxyFacade.getInstance().sendChat(request);
		GameState.get().updateGameState(newState);
	}
	
	private void initFromModel() {
		
		List<LogEntry> messages = GameState.get().getChatForView();
		getView().setEntries(messages);
	}

	@Override
	public void update(Observable o, Object arg) {
		initFromModel();		
	}

}

