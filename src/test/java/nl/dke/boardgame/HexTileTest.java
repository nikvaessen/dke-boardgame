package nl.dke.boardgame;

import junit.framework.TestCase;
import nl.dke.boardgame.game.board.HexTile;
import nl.dke.boardgame.game.board.TileState;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit Test for the HexTile
 */
public class HexTileTest extends TestCase
{
    @Test
    public void testCreation(){
        HexTile tile = new HexTile(1,2);
        System.out.println(tile);
        Assert.assertEquals(tile.toString(), "{( 1, 2);NEUTRAL}");

        System.out.println(tile.printNeighbors());
        Assert.assertEquals(tile.printNeighbors(),
                "{NULL}{NULL}{NULL}{NULL}{NULL}{NULL}");

        System.out.println(tile.getState());
        assertEquals(tile.getState(), TileState.NEUTRAL);
    }

    @Test
    public void testNeighbours(){
        HexTile tile = new HexTile(1,2);

    }

    @Test
    public void testClaiming(){

    }
}
