package server;
import java.io.*;
import java.net.*;
import java.util.logging.*;

import com.sun.net.httpserver.*;

import server.facade.*;
import server.handlers.Swagger;
import server.model.ServerState;

public class Server {
	public static ServerState state = new ServerState();
    public static IServerFacade facade = new ServerFacade(
            new GameFacade(),
            new GamesFacade(),
            new MovesFacade(),
            new UserFacade(),
            new UtilFacade()
    );

	private int port;
	private String host;

	private static final int MAX_WAITING_CONNECTIONS = 10;

	private static Logger logger;

	static {
		try {
			initLog();
		}
		catch (IOException e) {
			System.out.println("Could not initialize log: " + e.getMessage());
		}
	}

	public static void changeLogLevel(Level logLevel) {
		logger.setLevel(logLevel);
		for(Handler h :logger.getHandlers()) {
			h.setLevel(logLevel);
		}
		return;
	}
	
	private static void initLog() throws IOException {

		Level logLevel = Level.FINE;

		logger = Logger.getLogger("catanserver");
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);

		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);

		FileHandler fileHandler = new FileHandler("log.txt", false);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}


	private HttpServer server;

	public Server() {
		return;
	}

	private Server(String _host, int _port) {
		this.host = _host;
		this.port = _port;
		return;
	}

	@SuppressWarnings("restriction")
	public void run() {

		try {
			server = HttpServer.create(new InetSocketAddress(port),
					MAX_WAITING_CONNECTIONS);
		}
		catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}

		server.setExecutor(null); // use the default executor

		//SWAGGER SHIZ
		server.createContext("/docs/api/data", new Swagger.JSONAppender(""));
		server.createContext("/docs/api/view", new Swagger.BasicFile(""));

		//USERS
		server.createContext("/user/login", login);
		server.createContext("/user/register", register);

		//GAMES
		server.createContext("/games/list", list);
		server.createContext("/games/create", create);
		server.createContext("/games/join", join);
		server.createContext("/games/save", save);
		server.createContext("/games/load", load);

		//GAME
		server.createContext("/game/model", model);
		server.createContext("/game/reset", reset);
		server.createContext("/game/commands", commands);

		//MOVES
		server.createContext("/moves/sendChat", sendChat);
		server.createContext("/moves/rollNumber", rollNumber);
		server.createContext("/moves/robPlayer", robPlayer);
		server.createContext("/moves/finishTurn", finishTurn);
		server.createContext("/moves/buyDevCard", buyDevCard);
		server.createContext("/moves/Year_of_Plenty", Year_of_Plenty);
		server.createContext("/moves/Road_Building", Road_Building);
		server.createContext("/moves/Soldier", Soldier);
		server.createContext("/moves/Monopoly", Monopoly);
		server.createContext("/moves/Monument", Monument);
		server.createContext("/moves/buildRoad", buildRoad);
		server.createContext("/moves/buildSettlement", buildSettlement);
		server.createContext("/moves/buildCity", buildCity);
		server.createContext("/moves/offerTrade", offerTrade);
		server.createContext("/moves/acceptTrade", acceptTrade);
		server.createContext("/moves/maritimeTrade", maritimeTrade);
		server.createContext("/moves/discardCards", discardCards);

		//UTIL
		server.createContext("/util/changeLogLevel", changeLogLevel);

		//ListAI because the client calls it
		server.createContext("/game/listAI", listAi);

		logger.info("Starting HTTP Server at " + this.host + " on " + this.port);

		server.start();
	}

	//User Handlers
	private HttpHandler login = new server.handlers.user.LoginHandler();
	private HttpHandler register = new server.handlers.user.RegisterHandler();

	//GameS Handlers
	private HttpHandler list = new server.handlers.games.ListHandler();
	private HttpHandler create = new server.handlers.games.CreateHandler();
	private HttpHandler join = new server.handlers.games.JoinHandler();
	private HttpHandler save = new server.handlers.games.SaveHandler();
	private HttpHandler load = new server.handlers.games.LoadHandler();


	//Game Handlers
	private HttpHandler model = new server.handlers.game.ModelHandler();
	private HttpHandler reset = new server.handlers.game.ResetHandler();
	private HttpHandler commands = new server.handlers.game.CommandsHandler();


	//Moves Handlers
	private HttpHandler sendChat = new server.handlers.moves.SendChatHandler();
	private HttpHandler rollNumber = new server.handlers.moves.RollNumberHandler();
	private HttpHandler robPlayer = new server.handlers.moves.RobPlayerHandler();
	private HttpHandler finishTurn = new server.handlers.moves.FinishTurnHandler();
	private HttpHandler buyDevCard = new server.handlers.moves.BuyDevCardHandler();
	private HttpHandler Year_of_Plenty = new server.handlers.moves.YearOfPlentyCardHandler();
	private HttpHandler Road_Building = new server.handlers.moves.RoadBuildingCardHandler();
	private HttpHandler Soldier = new server.handlers.moves.SoldierCardHandler();
	private HttpHandler Monopoly = new server.handlers.moves.MonopolyCardHandler();
	private HttpHandler Monument = new server.handlers.moves.MonumentCardHandler();
	private HttpHandler buildRoad = new server.handlers.moves.BuildRoadHandler();
	private HttpHandler buildSettlement = new server.handlers.moves.BuildSettlementHandler();
	private HttpHandler buildCity = new server.handlers.moves.BuildCityHandler();
	private HttpHandler offerTrade = new server.handlers.moves.OfferTradeHandler();
	private HttpHandler acceptTrade = new server.handlers.moves.AcceptTradeHandler();
	private HttpHandler maritimeTrade = new server.handlers.moves.MaritimeTradeHandler();
	private HttpHandler discardCards = new server.handlers.moves.DiscardCardsHandler();




	//Util Handlers
	private HttpHandler changeLogLevel= new server.handlers.util.ChangeLogLevelHandler();

	//List AI
	private HttpHandler listAi = new server.handlers.game.ListAiHandler();


	public static void main(String[] args) {
		int port = 8081;

		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}

		new Server("localhost", port).run();
	}

}