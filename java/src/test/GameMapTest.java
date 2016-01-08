package test;

import junit.framework.TestCase;
import shared.definitions.TurnStatus;
import shared.locations.*;
import shared.model.*;

/**
 * Created by benja_000 on 2/20/2015.
 */
public class GameMapTest extends TestCase{

    public void testIsWaterHex() throws Exception{
        GameMap m = new GameMap();
        m.setRadius(3);

        // test some locations that are crazily off the map
        assertFalse(m.isWaterHex(new HexLocation(500, 500)));
        assertFalse(m.isWaterHex(new HexLocation(-500, -500)));
        assertFalse(m.isWaterHex(new HexLocation(500, 0)));
        assertFalse(m.isWaterHex(new HexLocation(0, 500)));
        assertFalse(m.isWaterHex(new HexLocation(5, 0)));
        assertFalse(m.isWaterHex(new HexLocation(0, 5)));

        // test all the edges just off the map
        // northeast
        assertFalse(m.isWaterHex(new HexLocation(0, -4)));
        assertFalse(m.isWaterHex(new HexLocation(1, -4)));
        assertFalse(m.isWaterHex(new HexLocation(2, -4)));
        assertFalse(m.isWaterHex(new HexLocation(3, -4)));
        assertFalse(m.isWaterHex(new HexLocation(4, -4)));
        // east
        assertFalse(m.isWaterHex(new HexLocation(4, -3)));
        assertFalse(m.isWaterHex(new HexLocation(4, -2)));
        assertFalse(m.isWaterHex(new HexLocation(4, -1)));
        assertFalse(m.isWaterHex(new HexLocation(4, 0)));
        // southeast
        assertFalse(m.isWaterHex(new HexLocation(3, 1)));
        assertFalse(m.isWaterHex(new HexLocation(2, 2)));
        assertFalse(m.isWaterHex(new HexLocation(1, 3)));
        assertFalse(m.isWaterHex(new HexLocation(0, 4)));
        // southwest
        assertFalse(m.isWaterHex(new HexLocation(-1, 4)));
        assertFalse(m.isWaterHex(new HexLocation(-2, 4)));
        assertFalse(m.isWaterHex(new HexLocation(-3, 4)));
        assertFalse(m.isWaterHex(new HexLocation(-4, 4)));
        // west
        assertFalse(m.isWaterHex(new HexLocation(-4, 3)));
        assertFalse(m.isWaterHex(new HexLocation(-4, 2)));
        assertFalse(m.isWaterHex(new HexLocation(-4, 1)));
        // northwest
        assertFalse(m.isWaterHex(new HexLocation(-3, -1)));
        assertFalse(m.isWaterHex(new HexLocation(-2, -2)));
        assertFalse(m.isWaterHex(new HexLocation(-1, -3)));

        // test all water edges
        assertTrue(m.isWaterHex(new HexLocation(0, -3)));
        assertTrue(m.isWaterHex(new HexLocation(1, -3)));
        assertTrue(m.isWaterHex(new HexLocation(2, -3)));
        assertTrue(m.isWaterHex(new HexLocation(3, -3)));
        assertTrue(m.isWaterHex(new HexLocation(3, -2)));
        assertTrue(m.isWaterHex(new HexLocation(3, -1)));
        assertTrue(m.isWaterHex(new HexLocation(3, 0)));
        assertTrue(m.isWaterHex(new HexLocation(2, 1)));
        assertTrue(m.isWaterHex(new HexLocation(1, 2)));
        assertTrue(m.isWaterHex(new HexLocation(0, 3)));
        assertTrue(m.isWaterHex(new HexLocation(-1, 3)));
        assertTrue(m.isWaterHex(new HexLocation(-2, 3)));
        assertTrue(m.isWaterHex(new HexLocation(-3, 3)));
        assertTrue(m.isWaterHex(new HexLocation(-3, 2)));
        assertTrue(m.isWaterHex(new HexLocation(-3, 1)));
        assertTrue(m.isWaterHex(new HexLocation(-3, 0)));
        assertTrue(m.isWaterHex(new HexLocation(-2, -1)));
        assertTrue(m.isWaterHex(new HexLocation(-1, -2)));

        // test a few land locations
        assertFalse(m.isWaterHex(new HexLocation(0, -2)));
        assertFalse(m.isWaterHex(new HexLocation(1, -2)));
        assertFalse(m.isWaterHex(new HexLocation(-2, 0)));
        assertFalse(m.isWaterHex(new HexLocation(0, 0)));
        assertFalse(m.isWaterHex(new HexLocation(0, 2)));
        assertFalse(m.isWaterHex(new HexLocation(-1, 2)));
    }

    public void testIsAdjacentToPlayerRoad() throws Exception{
        GameMap m;

        // road is next to water
        m = setupMapWithRoad(0, -2, EdgeDirection.North, 0);
        assertTrue(m.isAdjacentToPlayerRoad(0, new VertexLocation(new HexLocation(0, -2), VertexDirection.NorthEast)));
        assertTrue(m.isAdjacentToPlayerRoad(0, new VertexLocation(new HexLocation(0, -2), VertexDirection.NorthWest)));
        assertFalse(m.isAdjacentToPlayerRoad(0, new VertexLocation(new HexLocation(0, -1), VertexDirection.NorthWest)));

        // road is not next to water
        m = setupMapWithRoad(0, -1, EdgeDirection.North, 0);
        assertTrue(m.isAdjacentToPlayerRoad(0, new VertexLocation(new HexLocation(0, -1), VertexDirection.NorthEast)));
        assertTrue(m.isAdjacentToPlayerRoad(0, new VertexLocation(new HexLocation(0, -1), VertexDirection.NorthWest)));
        assertFalse(m.isAdjacentToPlayerRoad(0, new VertexLocation(new HexLocation(0, 0), VertexDirection.NorthWest)));
    }

    private GameMap setupMapWithRoadAndSettlement(int rX, int rY, EdgeDirection rDir, int rOwner,
                                         int sX, int sY, VertexDirection sDir, int sOwner){
        GameMap m = new GameMap();
        m.setRadius(3);
        Road r = new Road(rOwner, new EdgeLocation(new HexLocation(rX, rY), rDir));
        VertexObject s = new VertexObject(sOwner, new VertexLocation(new HexLocation(sX, sY), sDir));
        m.getRoads().add(r);
        m.getSettlements().add(s);

        return m;

    }

    private GameMap setupMapWithRoad(int rX, int rY, EdgeDirection rDir, int rOwner){
        GameMap m = new GameMap();
        m.setRadius(3);
        Road r = new Road(rOwner, new EdgeLocation(new HexLocation(rX, rY), rDir));
        m.getRoads().add(r);

        return m;

    }
}
