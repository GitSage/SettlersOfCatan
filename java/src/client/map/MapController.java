package client.map;

import java.util.*;
import java.util.List;

import client.proxy.ProxyFacade;
import shared.definitions.*;
import shared.locations.*;
import client.base.*;
import client.data.*;
import shared.model.*;
import shared.request.move.*;
import shared.states.playing.*;


/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer {
	
	private IRobView robView;
	private GameState gameState;
	
	public MapController(IMapView view, IRobView robView) {
		
		super(view);
		
		setRobView(robView);
		gameState = GameState.get();
		gameState.addObserver(this);
//		initFromModel();
	}

	public void resetView() {
		((MapView) getView()).resetView();
	}
	
	public void update(Observable state, Object obj) {
		gameState = (GameState) state;
		initFromModel();
		nextStep();
	}

	/**
	 * Uses the state to figure out what it should be doing
	 */
	public void nextStep() {
		ProxyFacade.getInstance().getSetupState().setMap(this);
		ProxyFacade.getInstance().getSetupState().advance();
		PlayerInfo player = ProxyFacade.getInstance().getLocalPlayer();
		if (gameState.isTurnAndStatus(player.getPlayerIndex(), TurnStatus.ROBBING)) {
			ProxyFacade.getInstance().setPlayingState(new RobState());
			getView().startDrop(PieceType.ROBBER, player.getColor(), false);
		}
	}


	
	public IMapView getView() {
		
		return (IMapView)super.getView();
	}
	
	private IRobView getRobView() {
		return robView;
	}
	private void setRobView(IRobView robView) {
		this.robView = robView;
	}

	protected void initFromModel() {

		List<Player> players = gameState.getPlayers();
		Map<Integer, CatanColor> colorMap = new HashMap<>();
		if (players != null) {
			for (Player player : players) {
				if (player != null) {
					colorMap.put(player.getPlayerIndex(), player.getColor());
				}
			}
		}

		GameMap map = gameState.getMap();

		//If it's null do nothing
		if (map == null) {
			return;
		}

		map.setOceanHexes();

		List<Hex> hexes = map.getHexes();
		if (hexes != null) {
			for (Hex hex : hexes) {
				if (hex != null) {
					if (hex.getNumber() != 0) {
						getView().addNumber(hex.getLocation(), hex.getNumber());
					} else {
						if (hex.getType() == null) {
							hex.setType(HexType.DESERT);
						}
					}
					getView().addHex(hex.getLocation(), hex.getType());
				}
			}
		}

		List<Road> roads = map.getRoads();
		if (roads != null) {
			for (Road road : roads) {
				if (road != null) {
					getView().placeRoad(road.getLocation(), colorMap.get(road.getOwner()));
				}
			}
		}

		List<VertexObject> settlements = map.getSettlements();
		if (settlements != null) {
			for (VertexObject settlement : settlements) {
				if (settlement != null) {
					getView().placeSettlement(settlement.getLocation(), colorMap.get(settlement.getOwner()));
				}
			}
		}

		List<VertexObject> cities = map.getCities();
		if (cities != null) {
			for (VertexObject city : cities) {
				if (city != null) {
					getView().placeCity(city.getLocation(), colorMap.get(city.getOwner()));
				}
			}
		}

		List<Port> ports = map.getPorts();
		if (ports != null) {
			for (Port port : ports) {
				if (port != null) {
					getView().addPort(new EdgeLocation(port.getLocation(), port.getDirection()), port.getType());
				}
			}
		}
		HexLocation robberLocation = map.getRobber();
		getView().placeRobber(robberLocation);

	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		return gameState.canBuildRoad(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), edgeLoc);
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		return gameState.canBuildSettlement(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), vertLoc);
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		return gameState.canBuildCity(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), vertLoc);
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		return GameState.get().canPlaceRobber(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), hexLoc);
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		getView().placeRoad(edgeLoc, ProxyFacade.getInstance().getLocalPlayer().getColor());

		if ((ProxyFacade.getInstance().getPlayingState()).placeRoad(edgeLoc)) {
			getView().startDrop(PieceType.ROAD, ProxyFacade.getInstance().getLocalPlayer().getColor(), true);
		}
	}

	public void placeSettlement(VertexLocation vertLoc) {
		getView().placeSettlement(vertLoc, ProxyFacade.getInstance().getLocalPlayer().getColor());

		TurnStatus turnStatus = gameState.getTurnTracker().getStatus();
		boolean isFree = turnStatus == TurnStatus.FIRSTROUND || turnStatus == TurnStatus.SECONDROUND;
		BuildSettlementRequest request = new BuildSettlementRequest(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), vertLoc, isFree);
		ProxyFacade.getInstance().buildSettlement(request);
		if (GameState.get().getTurnTracker().getStatus() == TurnStatus.FIRSTROUND || GameState.get().getTurnTracker().getStatus() == TurnStatus.SECONDROUND) {
			//End the players turn
			FinishTurnRequest endRequest = new FinishTurnRequest(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex());
			GameState newState = ProxyFacade.getInstance().finishTurn(endRequest);
			GameState.get().updateGameState(newState);
		}
	}

	public void placeCity(VertexLocation vertLoc) {
		getView().placeCity(vertLoc, ProxyFacade.getInstance().getLocalPlayer().getColor());

		TurnStatus turnStatus = gameState.getTurnTracker().getStatus();
		boolean isFree = turnStatus == TurnStatus.FIRSTROUND || turnStatus == TurnStatus.SECONDROUND;
		BuildCityRequest request = new BuildCityRequest(ProxyFacade.getInstance().getLocalPlayer().getPlayerIndex(), vertLoc, isFree);
		GameState newState = ProxyFacade.getInstance().buildCity(request);
		gameState.updateGameState(newState);
	}

	public void placeRobber(HexLocation hexLoc) {
		getView().placeRobber(hexLoc);
		//Use the state to store the hexLoc
		ProxyFacade.getInstance().getPlayingState().setRobHex(hexLoc);
		getRobView().setPlayers(GameState.get().getRobbablePlayers(hexLoc));
		System.out.println("Opening rob modal");
		getRobView().showModal();
	}
	
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {	
		getView().startDrop(pieceType, ProxyFacade.getInstance().getLocalPlayer().getColor(), true);
	}
	
	public void cancelMove() {
		ProxyFacade.getInstance().setPlayingState(new PlayingState());
	}
	
	public void playSoldierCard() {
		ProxyFacade.getInstance().setPlayingState(new SoldierState());
		getView().startDrop(PieceType.ROBBER, ProxyFacade.getInstance().getLocalPlayer().getColor(), true);
	}
	
	public void playRoadBuildingCard() {	
		ProxyFacade.getInstance().setPlayingState(new RoadBuildState());
		getView().startDrop(PieceType.ROAD, ProxyFacade.getInstance().getLocalPlayer().getColor(), true);
	}
	
	public void robPlayer(RobPlayerInfo victim) {
//		String state = ProxyFacade.getInstance().getPlayingState().getState();
		ProxyFacade.getInstance().getPlayingState().sendRobRequest(victim);
		ProxyFacade.getInstance().setPlayingState(new PlayingState());
		System.out.println("Closing rob modal");
		getRobView().closeModal();

	}
}

