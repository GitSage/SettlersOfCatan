package client.main;

import javax.swing.*;

import client.catan.*;
import client.login.*;
import client.join.*;
import client.misc.*;
import client.base.*;
import client.poller.ServerPoller;
import client.proxy.HttpProxy;
import client.proxy.ProxyFacade;
import shared.model.GameState;

/**
 * Main entry point for the Catan program
 */
@SuppressWarnings("serial")
public class Catan extends JFrame
{
		
	private static ServerPoller poller;
	private static JoinGameController joinController;
	private static PlayerWaitingController waitingController;
	
	private static CatanPanel catanPanel;

	public static PlayerWaitingController getWaitingController() {
		return waitingController;
	}
	
	public static ServerPoller getPoller() {
		return poller;
	}

	public static JoinGameController getJoinController() {
		return joinController;
	}
	
	public CatanPanel getCatanPanel() {
		return catanPanel;
	}
	
	public static void setCatanPanel(CatanPanel cp) {
		catanPanel = cp;
	}
	
	public Catan()
	{
		
		client.base.OverlayView.setWindow(this);
		
		this.setTitle("Settlers of Catan");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		catanPanel = new CatanPanel();
		this.setContentPane(catanPanel);
		
		display();
	}
	
	private void display()
	{
		pack();
		setVisible(true);
	}
	
	//
	// Main
	//
	
	public static void main(final String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		String host = "localhost";
		String port = "8081";

		if (args.length >= 2) {
			host = args[0];
			port = args[1];
		}

		System.out.println("Host: " + host + ", Port: " + port);
        ProxyFacade.init(new HttpProxy(host, port));
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				new Catan();
				poller = new ServerPoller(500, GameState.get());
				
				PlayerWaitingView playerWaitingView = new PlayerWaitingView();
				waitingController = new PlayerWaitingController(playerWaitingView);
				playerWaitingView.setController(waitingController);
				
				JoinGameView joinView = new JoinGameView();
				NewGameView newGameView = new NewGameView();
				SelectColorView selectColorView = new SelectColorView();
				MessageView joinMessageView = new MessageView();
				joinController = new JoinGameController(joinView,
										newGameView,
										selectColorView,
										joinMessageView);
				joinController.setJoinAction(new IAction() {
					@Override
					public void execute()
					{
						waitingController.start();
					}
				});
				joinView.setController(joinController);
				newGameView.setController(joinController);
				selectColorView.setController(joinController);
				joinMessageView.setController(joinController);
				
				LoginView loginView = new LoginView();
				MessageView loginMessageView = new MessageView();
				LoginController loginController = new LoginController(
																	  loginView,
																	  loginMessageView);
				loginController.setLoginAction(new IAction() {
					@Override
					public void execute()
					{
						joinController.start();
					}
				});
				loginView.setController(loginController);
				loginView.setController(loginController);
				
				loginController.start();
			}
		});
	}
}

