package shared.model;

import shared.definitions.HexType;
import shared.definitions.PortType;
import shared.locations.*;

import java.util.*;

/**
 * A model for a GameMap.
 */
public class GameMap {
	private List<Hex> hexes;
	private List<Port> ports;
	private List<Road> roads;
	private List<VertexObject> settlements;
	private List<VertexObject> cities;
	private int radius;
	private HexLocation robber;

	private transient final HexLocation[] landLocations = {
			new HexLocation(-2,  0),
			new HexLocation(-2,  1),
			new HexLocation(-2,  2),
			new HexLocation(-1, -1),
			new HexLocation(-1,  0),
			new HexLocation(-1,  1),
			new HexLocation(-1,  2),
			new HexLocation( 0, -2),
			new HexLocation( 0, -1),
			new HexLocation( 0,  0),
			new HexLocation( 0,  1),
			new HexLocation( 0,  2),
			new HexLocation( 1, -2),
			new HexLocation( 1, -1),
			new HexLocation( 1,  0),
			new HexLocation( 1,  1),
			new HexLocation( 2, -2),
			new HexLocation( 2, -1),
			new HexLocation( 2,  0),
	};

	private transient final HexLocation[] portLocations = {
			new HexLocation( 3, -3),
			new HexLocation( 3, -1),
			new HexLocation( 2,  1),
			new HexLocation( 0,  3),
			new HexLocation(-2,  3),
			new HexLocation(-3,  2),
			new HexLocation(-3,  0),
			new HexLocation(-1, -2),
			new HexLocation( 1, -3),
	};

	private transient final EdgeDirection[] portDirections = {
			EdgeDirection.SouthWest,
			EdgeDirection.NorthWest,
			EdgeDirection.NorthWest,
			EdgeDirection.North,
			EdgeDirection.NorthEast,
			EdgeDirection.NorthEast,
			EdgeDirection.SouthEast,
			EdgeDirection.South,
			EdgeDirection.South,
	};

	private transient final int[] hexNumbers = {
			5, 2, 6,
			8, 10, 9, 3,
            // 0 would go here, but it's handled by the zeroOffset.
			3, 11, 4, 8,
			4, 9, 5, 10,
			11, 12, 6
	};

	private transient final PortType[] portTypes = {
			PortType.THREE,
			PortType.SHEEP,
			PortType.THREE,
			PortType.THREE,
			PortType.BRICK,
			PortType.WOOD,
			PortType.THREE,
			PortType.WHEAT,
			PortType.ORE,
	};

	private transient final HexType[] hexTypes = {
			HexType.ORE,
			HexType.WHEAT,
			HexType.WOOD,

			HexType.BRICK,
			HexType.SHEEP,
			HexType.SHEEP,
			HexType.ORE,

			HexType.DESERT,
			HexType.WOOD,
			HexType.WHEAT,
			HexType.WOOD,
			HexType.WHEAT,

			HexType.BRICK,
			HexType.ORE,
			HexType.BRICK,
			HexType.SHEEP,

			HexType.WOOD,
			HexType.SHEEP,
			HexType.WHEAT,
	};


	public GameMap() {
		this.hexes = new ArrayList<>();
		this.ports = new ArrayList<>();
		this.roads = new ArrayList<>();
		this.settlements = new ArrayList<>();
		this.cities = new ArrayList<>();
		this.radius = 3;
	}

	public GameMap(boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
		this.roads = new ArrayList<>();
		this.cities = new ArrayList<>();
		this.settlements = new ArrayList<>();
		this.hexes = new ArrayList<>();
		this.ports = new ArrayList<>();
		this.radius = 3;

		List<Integer> numbers = new ArrayList<>(this.hexNumbers.length);
		for (int n : this.hexNumbers) {
			numbers.add(n);
		}
		List<HexType> hexTypes = Arrays.asList(this.hexTypes);
		List<PortType> portTypes = Arrays.asList(this.portTypes);

		if (randomNumbers) {
			Collections.shuffle(numbers);
		}

		if (randomPorts) {
			Collections.shuffle(portTypes);
		}

		if (randomTiles) {
			Collections.shuffle(hexTypes);
		}


		// Add Land Hexes
        int zeroOffset = 0;
		for (int i = 0; i < landLocations.length; i++) {
			Hex tempHex = new Hex();
            tempHex.setLocation(landLocations[i]);
            tempHex.setType(hexTypes.get(i));
            if (hexTypes.get(i) == HexType.DESERT) {
                tempHex.setNumber(0);
                this.robber = landLocations[i];
                zeroOffset = -1;
            } else {
                tempHex.setNumber(numbers.get(i + zeroOffset));
            }
            this.hexes.add(tempHex);
        }

		// Add Ports
		for (int i = 0; i < portLocations.length; i++) {
			Port tempPort = new Port();
			tempPort.setType(portTypes.get(i));
			tempPort.setDirection(portDirections[i]);
			tempPort.setLocation(portLocations[i]);
			this.ports.add(tempPort);
		}
	}

	public void reset() {
		settlements = new ArrayList<>();
		cities = new ArrayList<>();
		roads = new ArrayList<>();

		for (Hex theHex : hexes) {
			if (theHex.getType() == HexType.DESERT) {
				this.setRobber(theHex.getLocation());
			}
		}
	}

	/**
	 * Determines whether a road already exists at a chosen location.
	 * @pre none
	 * @post returns true if a road already exists at the specified location, false otherwise
	 * @param e EdgeLocation
	 * @return boolean
	 */
	public boolean isRoadAtLocation(EdgeLocation e){
		for(int i = 0; i < getRoads().size(); i++){
			if(getRoads().get(i).getLocation().getNormalizedLocation().equals(e)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines whether a city or settlement already exists at a chosen location.
	 * @pre none
	 * @post returns true if a road already exists at the specified location, false otherwise
	 * @param loc VertexLocation
	 * @return boolean
	 */
	public boolean isCityOrSettlementAtLocation(VertexLocation loc) {
		for(int i = 0; i < getCities().size(); i++){
			if(getCities().get(i).getLocation().getNormalizedLocation().equals(loc)){
				return true;
			}
		}

		for(int i = 0; i < getSettlements().size(); i++){
			if(getSettlements().get(i).getLocation().getNormalizedLocation().equals(loc)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if a particular location is adjacent to a city or a settlement.
	 * @pre loc is a valid map location.
	 * @pre loc is on land.
	 * @post returns true if any adjacent vertex holds a city or a settlement, false otherwise.
	 * @param loc VertexLocation
	 * @return boolean
	 */
	public boolean hasAdjacentCityOrSettlement(VertexLocation loc){
		assert(isValidHex(loc.getHexLoc()));
		VertexLocation nLoc = loc.getNormalizedLocation();

		if(nLoc.getDir() == VertexDirection.NorthWest){
			if(isCityOrSettlementAtLocation(nLoc.getAdjacentVertex(VertexDirection.East)) ||
					isCityOrSettlementAtLocation(nLoc.getAdjacentVertex(VertexDirection.NorthWest)) ||
					isCityOrSettlementAtLocation(nLoc.getAdjacentVertex(VertexDirection.SouthWest)))
				return true;
		}

		if(nLoc.getDir() == VertexDirection.NorthEast){
			if(isCityOrSettlementAtLocation(nLoc.getAdjacentVertex(VertexDirection.West)) ||
					isCityOrSettlementAtLocation(nLoc.getAdjacentVertex(VertexDirection.NorthEast)) ||
					isCityOrSettlementAtLocation(nLoc.getAdjacentVertex(VertexDirection.SouthEast)))
				return true;
		}

		return false;
	}

	/**
	 * Determines if a given VertexLocation is on land.
	 * "On land" means at least one adjacent hex is land.
	 * @pre loc is a valid location.
	 * @post returns true if the location is on land, false otherwise.
	 * @param loc VertexLocation
	 * @return boolean
	 */
	public boolean isVertexLocationOnLand(VertexLocation loc){
		VertexLocation nLoc = loc.getNormalizedLocation();

		// normalized hex location is valid
		if(!isValidHex(nLoc.getHexLoc()))
			return false;

		// NE edge of map, city never on land
		if(isNEWaterHex(nLoc.getHexLoc()))
			return false;

		// NW edge of map, city never on land
		if(isNWWaterHex(nLoc.getHexLoc()))
			return false;

		// west edge of map
		if(isWWaterHex(nLoc.getHexLoc()) && nLoc.getDir() == VertexDirection.NorthWest)
			return false;

		// east edge of map
		//noinspection RedundantIfStatement
		if(isEWaterHex(nLoc.getHexLoc()) && nLoc.getDir() == VertexDirection.NorthEast)
			return false;

		return true;
	}

	public boolean isEdgeOnLand(EdgeLocation location) {
		EdgeLocation nEdge = location.getNormalizedLocation();
		if (!isValidHex(nEdge.getHexLoc())) {
			////System.out.println("Invalid hex");
			return false;
		}

		if(isNEWaterHex(nEdge.getHexLoc())) {
			////System.out.println("NE water");
			return false;
		}
		if(isNWWaterHex(nEdge.getHexLoc())) {
			//.out.println("NW water");
			return false;
		}
		if(isWWaterHex(nEdge.getHexLoc()) && (nEdge.getDirection() == EdgeDirection.NorthWest || nEdge.getDirection() == EdgeDirection.North)) {
			////System.out.println("W water");
			return false;
		}
		if(isEWaterHex(nEdge.getHexLoc()) && (nEdge.getDirection() == EdgeDirection.NorthEast || nEdge.getDirection() == EdgeDirection.North)) {
			////System.out.println("E water");
			return false;
		}
		if(isSWWaterHex(nEdge.getHexLoc()) && nEdge.getDirection() == EdgeDirection.NorthWest) {
			////System.out.println("SW water");
			return false;
		}

		//noinspection RedundantIfStatement
		if(isSEWaterHex(nEdge.getHexLoc()) && nEdge.getDirection() == EdgeDirection.NorthEast) {
			////System.out.println("SE water");
			return false;
		}
		return true;
	}



	/**
	 * Determines if a given hex is on the map.
	 * @pre none
	 * @post returns true if the hex is on the map, false otherwise.
	 *       The following algorithm determines if a hex location is on the map.
	 *       x >= -r, x <= r
	 *       if x < 0
	 *         y >= -r+x, y <= r
	 *       if x >= 0
	 *         y >= -r, y <= r-x
	 * @param hex the HexLocation in question
	 * @return boolean
	 */
	public boolean isValidHex(HexLocation hex){
		if(hex.getX() < -1*radius || hex.getX() > radius)
			return false;
		if(hex.getX() < 0){
			return (hex.getY() >= -1*radius + Math.abs(hex.getX()) && hex.getY() <= radius);
		}
		else{
			return (hex.getY() >= -1*radius && hex.getY() <= radius-hex.getX());
		}
	}

	/**
	 * Checks if a VertexLocation is adjacent to a road owned by the player.
	 * @param player int
	 * @param loc VertexLocation
	 * @return boolean
	 */
	public boolean isAdjacentToPlayerRoad(int player, VertexLocation loc){
		VertexLocation nLoc = loc.getNormalizedLocation();
		if(nLoc.getDir() == VertexDirection.NorthWest){
			////System.out.println("City is in northwest position");
			for(int i = 0; i < roads.size(); i++){
				EdgeLocation nRoad = getRoads().get(i).getLocation().getNormalizedLocation();
				////System.out.println("Checking if next to road " + nRoad.toString());

				if(getRoads().get(i).getOwner() == player) {
					////System.out.println("Detected that player owns both road and city.");

					if ((nRoad.getHexLoc().equals(nLoc.getHexLoc()))) { // this hex
						////System.out.println("Detected that hex location is the same.");
						if (nRoad.getDirection() == EdgeDirection.North || // this hex, north edge
								nRoad.getDirection() == EdgeDirection.NorthWest) { // this hex, northwest edge
							return true;
						}
					} else if (nRoad.getHexLoc().equals(nLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest))) {
						if (nRoad.getDirection() == EdgeDirection.NorthEast) // northwest hex, northeast edge
							return true;
					}
				}
			}
		}
		else if(nLoc.getDir() == VertexDirection.NorthEast){
			////System.out.println("City is in northeast position");
			for(int i = 0; i < roads.size(); i++){
				EdgeLocation nRoad = getRoads().get(i).getLocation().getNormalizedLocation();
				////System.out.println("Checking if next to road " + nRoad.toString());

				if(getRoads().get(i).getOwner() == player) {
					////System.out.println("Detected that player owns both road and city.");

					if ((nRoad.getHexLoc().equals(nLoc.getHexLoc()))) { // this hex
						////System.out.println("Detected that hex location is the same.");
						if (nRoad.getDirection() == EdgeDirection.North || // this hex, north edge
								nRoad.getDirection() == EdgeDirection.NorthEast) { // this hex, northeast edge
							return true;
						}
					} else if (nRoad.getHexLoc().equals(nLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast))) {
						if (nRoad.getDirection() == EdgeDirection.NorthWest) // northeast hex, northwest edge
							return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if a location is a city belonging to a player.
	 * @pre A player's index and a VertexLocation is passed in.
	 * @post Returns true if the location is a settlement and the
	 *      settlement belongs to the player, false otherwise.
	 * @param player int
	 * @param loc VertexLocation
	 * @return boolean
	 */
	public boolean settlementBelongsToPlayer(int player, VertexLocation loc){
		loc = loc.getNormalizedLocation();
		////System.out.println(loc);
		////System.out.println(getSettlements().get(0).getLocation().getNormalizedLocation());
		for(int i = 0; i < getSettlements().size(); i++){
			// if the location is a city
			if (getSettlements().get(i).getLocation().getNormalizedLocation().equals(loc)){
				// if the city belongs to the player
				if(getSettlements().get(i).getOwner() == player)
					return true;

				// location is a city belonging to someone else
				return false;
			}
		}

		// location is not a city
		return false;
	}

	/**
	 * Gets the hexes.
	 * @return the List of hexes.
	 */
	public List<Hex> getHexes() {
		return hexes;
	}

	/**
	 * Sets the hexes.
	 * @param hexes a List of hexes
	 */
	public void setHexes(List<Hex> hexes) {
		this.hexes = hexes;
	}

	/**
	 * Adds a hex to the hexes
	 * @param hex the HexLocation to be added
	 */
	public void addHex(Hex hex) {
		this.hexes.add(hex);
	}

	/**
	 * Sets all of the ocean hexes and puts them into hexes
	 */
	public void setOceanHexes() {
		Hex oceanHex;
		HexLocation hexLocation;
		//Set the ocean pieces
		//Start with right side
		int y = -3;
		for (int x = 0; x < 4; x++) {
			oceanHex = new Hex();
			oceanHex.setType(HexType.WATER);
			hexLocation = new HexLocation(x, y);
			oceanHex.setLocation(hexLocation);
			oceanHex.setNumber(0);
			addHex(oceanHex);

			oceanHex = new Hex();
			oceanHex.setType(HexType.WATER);
			hexLocation = new HexLocation(x, (x+y)*-1);
			oceanHex.setLocation(hexLocation);
			oceanHex.setNumber(0);
			addHex(oceanHex);
		}

		//Set the left side
		for (int lx = -1; lx > -4; lx--) {
			oceanHex = new Hex();
			oceanHex.setType(HexType.WATER);
			hexLocation = new HexLocation(lx, (3+lx)*-1);
			oceanHex.setLocation(hexLocation);
			oceanHex.setNumber(0);
			addHex(oceanHex);

			oceanHex = new Hex();
			oceanHex.setType(HexType.WATER);
			hexLocation = new HexLocation(lx, 3);
			oceanHex.setLocation(hexLocation);
			oceanHex.setNumber(0);
			addHex(oceanHex);
		}

		oceanHex = new Hex();
		oceanHex.setType(HexType.WATER);
		hexLocation = new HexLocation(3, -2);
		oceanHex.setLocation(hexLocation);
		oceanHex.setNumber(0);
		addHex(oceanHex);

		oceanHex = new Hex();
		oceanHex.setType(HexType.WATER);
		hexLocation = new HexLocation(3, -1);
		oceanHex.setLocation(hexLocation);
		oceanHex.setNumber(0);
		addHex(oceanHex);

		oceanHex = new Hex();
		oceanHex.setType(HexType.WATER);
		hexLocation = new HexLocation(-3, 1);
		oceanHex.setLocation(hexLocation);
		oceanHex.setNumber(0);
		addHex(oceanHex);

		oceanHex = new Hex();
		oceanHex.setType(HexType.WATER);
		hexLocation = new HexLocation(-3, 2);
		oceanHex.setLocation(hexLocation);
		oceanHex.setNumber(0);
		addHex(oceanHex);
	}

	/**
	 * Gets the ports.
	 * @return the List of ports.
	 */
	public List<Port> getPorts() {
		return ports;
	}

	/**
	 * Sets the ports.
	 * @param ports a List of ports
	 */
	public void setPorts(List<Port> ports) {
		this.ports = ports;
	}

	/**
	 * Gets the roads.
	 * @return the List of EdgeValues.
	 */
	public List<Road> getRoads() {
		return roads;
	}

	/**
	 * Sets the roads.
	 * @param roads a List of EdgeValues
	 */
	public void setRoads(List<Road> roads) {
		this.roads = roads;
	}

	/**
	 * Gets the settlements.
	 * @return the List of VertexObjects.
	 */
	public List<VertexObject> getSettlements() {
		return settlements;
	}

	/**
	 * Sets the settlements.
	 * @param settlements a List of VertexObjects
	 */
	public void setSettlements(List<VertexObject> settlements) {
		this.settlements = settlements;
	}

	/**
	 * Gets the cities.
	 * @return the List of cities.
	 */
	public List<VertexObject> getCities() {
		return cities;
	}

	/**
	 * Sets the cities.
	 * @param cities a List of VertexObjects
	 */
	public void setCities(List<VertexObject> cities) {
		this.cities = cities;
	}

	/**
	 * Gets the radius.
	 * @return the integer radius.
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Sets the radius.
	 * @param radius an integer radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * Gets the hex location of the robber.
	 * @return the HexLocation of the robber
	 */
	public HexLocation getRobber() {
		return robber;
	}

	/**
	 * Sets the hex location of the robber.
	 * @param robber the HexLocation of the robber
	 */
	public void setRobber(HexLocation robber) {
		this.robber = robber;
	}


	/**
	 * Determines if a road location is adjacent to a road, settlement, or city owned by a player.
	 * @pre road is a valid road location
	 * @pre road is on land
	 * @post player is a valid player index (0-3)
	 * @post returns true if the road is adjacent to another city or settlement owned by the player. It also returns
	 *       true if the road is adjacent to another road owned by the player AND there is no foreign city or settlement
	 *       between the road and the player.
	 * @param player the player in question
	 * @param location the EdgeLocation in question
	 * @return boolean
	 */
	public boolean roadAdjToPlayerRoadSettCity(int player, EdgeLocation location) {
		if(roadAdjToPlayerCityOrSettlement(player, location)) {
			return true;
		}
		//noinspection RedundantIfStatement
		if(edgeAdjToPlayerRoad(player, location)) {
			return true;
		}
		return false;
	}

	public boolean edgeAdjToPlayerRoad(int player, EdgeLocation givenEdgeLoc){
		givenEdgeLoc = givenEdgeLoc.getNormalizedLocation();

		for (Road r : getRoadsAdjacentToRoad(givenEdgeLoc)) {
			// first, check to see if the player owns this particular road.
			if (r.getOwner() == player) {
				EdgeLocation nR = r.getLocation().getNormalizedLocation();

				// once we've established that the player owns the road, we have to make sure that the vertex
				// isn't blocked by a foreign city.
				if(!isForeignSettCityInLocation(player, getVertexBetweenEdges(givenEdgeLoc, nR))){
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Gets the vertex between two adjacent edges.
	 * If the edges are not adjacent, returns null.
	 * @param loc1 the first EdgeLocation
	 * @param loc2 the second EdgeLocation
	 * @return a VertexLocation showing the vertex between two edge, or null if the
	 *		 edges are not adjacent.
	 */
	public VertexLocation getVertexBetweenEdges(EdgeLocation loc1, EdgeLocation loc2){
		loc1 = loc1.getNormalizedLocation();
		loc2 = loc2.getNormalizedLocation();

		if (loc1.getDirection() == EdgeDirection.NorthEast) {
			// Draw or look at a board. You'll see where these directions come from.
			if (loc2.getHexLoc().equals(loc1.getHexLoc()) &&
					loc2.getDirection() == EdgeDirection.North) {

				return new VertexLocation(loc1.getHexLoc(), VertexDirection.NorthEast);
			}
			if (loc2.getHexLoc().equals(loc1.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast)) &&
					loc2.getDirection() == EdgeDirection.NorthWest) {

				return new VertexLocation(loc1.getHexLoc(), VertexDirection.NorthEast);

			}
			if (loc2.getHexLoc().equals(loc1.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast)) &&
					loc2.getDirection() == EdgeDirection.North) {

				return new VertexLocation(loc1.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast),
						VertexDirection.NorthWest);
			}
			if (loc2.getHexLoc().equals(loc1.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast)) &&
					loc2.getDirection() == EdgeDirection.NorthWest) {

				return new VertexLocation(loc1.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast),
						VertexDirection.NorthWest);
			}
		}

		else if (loc1.getDirection() == EdgeDirection.NorthWest) {
			if (loc2.getHexLoc().equals(loc1.getHexLoc()) &&
					loc2.getDirection() == EdgeDirection.North) {

				return new VertexLocation(loc1.getHexLoc(), VertexDirection.NorthWest);
			}
			if (loc2.getHexLoc().equals(loc1.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest)) &&
					loc2.getDirection() == EdgeDirection.NorthEast) {

				return new VertexLocation(loc1.getHexLoc(), VertexDirection.NorthWest);

			}
			if (loc2.getHexLoc().equals(loc1.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest)) &&
					loc2.getDirection() == EdgeDirection.North) {

				return new VertexLocation(loc1.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest),
						VertexDirection.NorthEast);
			}
			if (loc2.getHexLoc().equals(loc1.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest)) &&
					loc2.getDirection() == EdgeDirection.NorthEast) {

				return new VertexLocation(loc1.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest),
						VertexDirection.NorthEast);
			}
		}

		else if (loc1.getDirection() == EdgeDirection.North) {
			if (loc2.getHexLoc().equals(loc1.getHexLoc()) &&
					loc2.getDirection() == EdgeDirection.NorthEast) {

				return new VertexLocation(loc1.getHexLoc(), VertexDirection.NorthEast);
			}
			if (loc2.getHexLoc().equals(loc1.getHexLoc()) &&
					loc2.getDirection() == EdgeDirection.NorthWest) {

				return new VertexLocation(loc1.getHexLoc(), VertexDirection.NorthWest);
			}
			if (loc2.getHexLoc().equals(loc1.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest)) &&
					loc2.getDirection() == EdgeDirection.NorthEast) {

				return new VertexLocation(loc1.getHexLoc(), VertexDirection.NorthWest);
			}
			if (loc2.getHexLoc().equals(loc1.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast)) &&
					loc2.getDirection() == EdgeDirection.NorthWest) {

				return new VertexLocation(loc1.getHexLoc(), VertexDirection.NorthEast);
			}
		}
		// checked every possible combination of edges, all failed. The edges are not adjacent.
		return null;
	}

	/**
	 * Gets all the roads on the map that are adjacent to a certain EdgeLocation.
	 * Ignores ownership.
	 * Returns a Set of size 0, 1, 2, 3, or 4.
	 * @param givenEdgeLoc the EdgeLocation in question.
	 * @return Set<Road>
	 */
	public Set<Road> getRoadsAdjacentToRoad(EdgeLocation givenEdgeLoc){
		Set<Road> adjRoads = new HashSet<>();

		for (Road r : roads) {
			EdgeLocation nR = r.getLocation().getNormalizedLocation();
			if (givenEdgeLoc.getDirection() == EdgeDirection.NorthEast) {
				// Draw or look at a board. You'll see where these directions come from.
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc()) &&
						nR.getDirection() == EdgeDirection.North) {

					adjRoads.add(r);
				}
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast)) &&
						nR.getDirection() == EdgeDirection.NorthWest) {

					adjRoads.add(r);

				}
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast)) &&
						nR.getDirection() == EdgeDirection.North) {

					adjRoads.add(r);

				}
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast)) &&
						nR.getDirection() == EdgeDirection.NorthWest) {

					adjRoads.add(r);
				}
			}
			else if (givenEdgeLoc.getDirection() == EdgeDirection.NorthWest) {
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc()) &&
						nR.getDirection() == EdgeDirection.North) {

					adjRoads.add(r);
				}
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest)) &&
						nR.getDirection() == EdgeDirection.North) {

					adjRoads.add(r);
				}
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest)) &&
						nR.getDirection() == EdgeDirection.NorthEast) {

					adjRoads.add(r);
				}
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest)) &&
						nR.getDirection() == EdgeDirection.NorthEast) {
					adjRoads.add(r);
				}
			}
			else if (givenEdgeLoc.getDirection() == EdgeDirection.North) {
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc()) &&
						nR.getDirection() == EdgeDirection.NorthEast) {
					adjRoads.add(r);
				}
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc()) &&
						nR.getDirection() == EdgeDirection.NorthWest) {

					adjRoads.add(r);
				}
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest)) &&
						nR.getDirection() == EdgeDirection.NorthEast) {

					adjRoads.add(r);
				}
				if (nR.getHexLoc().equals(givenEdgeLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast)) &&
						nR.getDirection() == EdgeDirection.NorthWest) {

					adjRoads.add(r);
				}
			}
		}

		return adjRoads;
	}

	/**
	 * Determines if a foreign city or settlement is to be found on a particular location.
	 * @pre hex and dir represent the normalized location of a vertex
	 * @pre hex and dir are valid locations on the map
	 * @post returns true if a city or settlement NOT owned by the player can be found on the vertex.
	 * @param player the index of the Player who is NOT considered a foreigner
	 * @param loc the VertexLocation in question
	 * @return boolean
	 */
	private boolean isForeignSettCityInLocation(int player, VertexLocation loc){
		HexLocation hex = loc.getHexLoc();
		VertexDirection dir = loc.getDir();

		// check that VertexDirection is normalized
		if(dir != VertexDirection.NorthEast &&
				dir != VertexDirection.NorthWest){
			System.out.println("YOU PASSED IN A NON-NORMALIZED LOCATION IN " + "" +
					"shared.model.GameMap.isForeignSettCityInLocation(): " + dir.toString());
		}

		for(VertexObject vo:getAllVertexObjects()){
			VertexLocation nLoc = vo.getLocation().getNormalizedLocation();
			if(vo.getOwner() != player && // a foreigner owns the city
					nLoc.getDir() == dir && // the city has the same direction
					nLoc.getHexLoc().equals(hex)){ // the city is on the same hex location
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if an EdgeLocation is adjacent to a player city or settlement.
	 * @param player the index of the Player in question
	 * @param location the EdgeLocation
	 * @return boolean
	 */
	public boolean roadAdjToPlayerCityOrSettlement(int player, EdgeLocation location){
		EdgeLocation roadLocation = location.getNormalizedLocation();

		// compare each city and settlement to both VertexObjects adjacent to the road.
		for(VertexObject citySett:getAllVertexObjects()){
			VertexLocation citySettLocation = citySett.getLocation().getNormalizedLocation();
			VertexLocation[] verticesAdjacentToRoad = getAdjacentVertices(roadLocation);

			if(citySett.getOwner() == player &&
					(citySettLocation.equals(verticesAdjacentToRoad[0]) ||
							citySettLocation.equals(verticesAdjacentToRoad[1])
					)){
					return true;
			}
		}

		return false;
	}

	public boolean isNEWaterHex(HexLocation loc){
		////System.out.println("Testing isNEWaterHex");
		return loc.getY() == (-1 * radius) &&
				loc.getX() >= 0 &&
				loc.getX() <= radius;
	}

	public boolean isNWWaterHex(HexLocation loc){
		////System.out.println("Testing isNWWaterHex");
		return loc.getX() + loc.getY() == -1*radius &&
				loc.getX() <= 0 &&
				loc.getX() >= -1*radius &&
				loc.getY() <= 0 &&
				loc.getY() >= -1*radius;
	}

	public boolean isEWaterHex(HexLocation loc){
		////System.out.println("Testing isEWaterHex");
		return loc.getX() == radius && loc.getY() <= 0 && loc.getY() >= -1*radius ;
	}

	public boolean isWWaterHex(HexLocation loc){
		////System.out.println("Testing isWWaterHex");
		return (loc.getX() == (-1)*radius) && (loc.getY() >= 0) && (loc.getY() <= radius);
	}

	public boolean isSWWaterHex(HexLocation loc){
		////System.out.println("Testing isSWWaterHex");
		return loc.getY() == radius && loc.getX() <= 0 && loc.getX() >= -1*radius;
	}

	public boolean isSEWaterHex(HexLocation loc){
		////System.out.println("Testing isSEWaterHex");
		return loc.getX() + loc.getY() == radius &&
				loc.getX() >= 0 &&
				loc.getX() <= radius;
	}

	public boolean isWaterHex(HexLocation loc){
		return (isNWWaterHex(loc) ||
				isNEWaterHex(loc) ||
				isEWaterHex(loc) ||
				isWWaterHex(loc) ||
				isSEWaterHex(loc) ||
				isSWWaterHex(loc));
	}

	/**
	 * Gets the two vertices that touch any particular edge.
	 * @param edge the EdgeLocation in question
	 * @return VertexLocation[2]
	 */
	public VertexLocation[] getAdjacentVertices(EdgeLocation edge){
		VertexLocation[] vl = new VertexLocation[2];
		EdgeLocation nEdge = edge.getNormalizedLocation();

		if(nEdge.getDirection() == EdgeDirection.North){
			vl[0] = new VertexLocation(
					nEdge.getHexLoc(),
					VertexDirection.NorthEast
			);
			vl[1] = new VertexLocation(
					nEdge.getHexLoc(),
					VertexDirection.NorthWest
			);
		}
		else if(nEdge.getDirection() == EdgeDirection.NorthEast){
			vl[0] = new VertexLocation(
					nEdge.getHexLoc(),
					VertexDirection.NorthEast
			);
			vl[1] = new VertexLocation(
					nEdge.getHexLoc().getNeighborLoc(EdgeDirection.SouthEast),
					VertexDirection.NorthWest
			);
		}
		else if(nEdge.getDirection() == EdgeDirection.NorthWest){
			vl[0] = new VertexLocation(
					nEdge.getHexLoc(),
					VertexDirection.NorthWest
			);
			vl[1] = new VertexLocation(
					nEdge.getHexLoc().getNeighborLoc(EdgeDirection.SouthWest),
					VertexDirection.NorthEast
			);
		}
		return vl;
	}

	/**
	 * Gets all VertexObject objects that are adjacent to a given HexLocation.
	 * @param hexLoc the HexLocation in question.
	 * @return Set<VertexObject>
	 */
	public Set<VertexObject> getAllAdjacentVertexObjects(HexLocation hexLoc) {
		Set<VertexObject> adjacentVertexObjects = new HashSet<>();
		Set<VertexObject> allVertexObjects = getAllVertexObjects();

		for(VertexObject vo:allVertexObjects){
			if(isAdjacentTo(vo, hexLoc)){
				adjacentVertexObjects.add(vo);
			}
		}

		return adjacentVertexObjects;
	}

	/**
	 * Gets all VertexObjects (cities and settlements) that currently exist.
	 * @return Set<VertexObject>
	 */
	public Set<VertexObject> getAllVertexObjects(){
		// combine cities and settlements into a single list
		Set<VertexObject> allVertexObjects = new HashSet<>();
		allVertexObjects.addAll(getCities());
		allVertexObjects.addAll(getSettlements());

		return allVertexObjects;
	}

	/**
	 * Determines if a given VertexObject is adjacent to a given HexLocation
	 * @param vo the VertexObject in question
	 * @param hexLoc the HexLocation in quest
	 * @return boolean
	 */
	public boolean isAdjacentTo(VertexObject vo, HexLocation hexLoc){
		VertexLocation nVertLoc = vo.getLocation().getNormalizedLocation();

		if(nVertLoc.getHexLoc().equals(hexLoc) ||
				nVertLoc.getHexLoc().getNeighborLoc(EdgeDirection.North).equals(hexLoc)){
			return true;
		}
		else if(nVertLoc.getDir() == VertexDirection.NorthEast &&
					nVertLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast).equals(hexLoc)){
			return true;
		}
		else if(nVertLoc.getDir() == VertexDirection.NorthWest &&
				nVertLoc.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest).equals(hexLoc)){
			return true;
		}

		return false;
	}

	/**
	 * Checks that an EdgeLocation, when placed, will have an available space for a settlement next to it.
	 * Useful for FIRSTROUND and SECONDROUND when players are putting roads before settlements.
	 * @param location the EdgeLocation in question
	 * @return boolean
	 */
	public boolean edgeHasFreeVertex(EdgeLocation location) {
		VertexLocation[] adjVert = getAdjacentVertices(location);

		// one of the vertices is already occupied
		if(isCityOrSettlementAtLocation(adjVert[0]) ||
				isCityOrSettlementAtLocation(adjVert[1])){
			return false;
		}

		// both edges have adjacent cities or settlements
		//noinspection RedundantIfStatement
		if(hasAdjacentCityOrSettlement(adjVert[0]) &&
				hasAdjacentCityOrSettlement(adjVert[1])){
			return false;
		}
		return true;
	}

	/**
	 * Does as the name suggests.
	 * @pre loc has a settlement at that location
	 * @post the settlement has been removed
	 * @post the city has been added and is owned by the same player as the old settlement
	 * @param loc VertexLocation
	 */
	public void replaceSettlementWithCity(VertexLocation loc){
		VertexObject settlement = getVertexObjectAtLocation(loc, settlements);
		settlements.remove(settlement);
		cities.add(new VertexObject(settlement.getOwner(), loc));
	}

	/**
	 * Gets a vertex object at the specified location.
	 * This method allows you to specify which list (usually cities or settlements) you want to search.
	 * @param loc VertexLocation
	 * @param list List<VertexObject>
	 * @return the VertexObject if found, null otherwise.
	 */
	public VertexObject getVertexObjectAtLocation(VertexLocation loc, List<VertexObject> list){
		loc = loc.getNormalizedLocation();
		for(VertexObject sett : list){
			if(loc.equals(sett.getLocation().getNormalizedLocation())){
				return sett;
			}
		}
		return null;
	}



	public List<Hex> getHexesWithNumber(int number) {
		List<Hex> hexes = new ArrayList<>();

		for(Hex hex: this.getHexes()){
			if(hex.getNumber() == number){
				hexes.add(hex);
			}
		}
		return hexes;
	}

	public void getResourcesDue(int roll, Map<Integer, ResourceList> players) {
		List<Hex> hexes = getHexesWithNumber(roll);
		for(Hex hex: hexes){
			// skip the hex if it has the robber on it, is the desert, or is on water
			if(getRobber().equals(hex.getLocation()) ||
					hex.getType() == HexType.DESERT ||
					hex.getType() == HexType.WATER){
				continue;
			}

			Set<VertexObject> allAjdacentVertexObjects = getAllAdjacentVertexObjects(hex.getLocation());
			for(VertexObject sett:getSettlements()){
				// the settlement is adjacent to the hex
				if(allAjdacentVertexObjects.contains(sett)){
					// add resources to the user
					players.get(sett.getOwner()).setResource(hex.getResourceType(), players.get(sett.getOwner()).getResource(hex.getResourceType()) + 1);
				}
			}

			for(VertexObject city:getCities()){
				// the city is adjacent to the hex
				if(allAjdacentVertexObjects.contains(city)){
					// add resources to the user
					players.get(city.getOwner()).setResource(hex.getResourceType(), players.get(city.getOwner()).getResource(hex.getResourceType()) + 2);
				}
			}
		}
	}

	public ResourceList getResourcesDue(VertexObject vo){
		Set<HexLocation> adjHexes = getLandHexesByVertex(vo.getLocation());
		ResourceList resources = new ResourceList(0,0,0,0,0);

		for(HexLocation hexl : adjHexes){
			for(Hex hex: getHexes()){
                // water check is just a sanity check, adjHexes should never contain a water hex
				if(hexl.equals(hex.getLocation()) && hex.getType() != HexType.DESERT && hex.getType() != HexType.WATER){
					resources.setResource(hex.getResourceType(), resources.getResource(hex.getResourceType()) + 1);
				}
			}
		}

		return resources;
	}


	public Set<HexLocation> getLandHexesByVertex(VertexLocation vo){
		vo = vo.getNormalizedLocation();
		Set<HexLocation> adjacent = new HashSet<>();

		if(!isWaterHex(vo.getHexLoc())){
			adjacent.add(vo.getHexLoc());
		}
		if(!isWaterHex(vo.getHexLoc())){
			adjacent.add(vo.getHexLoc().getNeighborLoc(EdgeDirection.North));
		}

		if(vo.getDir() == VertexDirection.NorthEast){
			if(!isWaterHex(vo.getHexLoc())){
				adjacent.add(vo.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast));
			}
		}
		else if(vo.getDir() == VertexDirection.NorthWest){
			if(!isWaterHex(vo.getHexLoc())){
				adjacent.add(vo.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest));
			}
		}

		return adjacent;
	}
}
