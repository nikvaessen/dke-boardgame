package nl.dke.boardgame;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.hex.RandomHexBoardSimulation;
import nl.dke.boardgame.mcts.hex.HexBoardState;
import org.junit.Test;

/**
 * Created by nik on 08/01/17.
 */
public class RandomHexBoardSimulationTest
{
    @Test
    public void testSimulation() throws AlreadyClaimedException
    {
        Board b = new Board(2,2);
/*        b.claim(0,0, TileState.PLAYER1);
        b.claim(1,0, TileState.PLAYER1);
        b.claim(0,1, TileState.PLAYER2);
        b.claim(1,1, TileState.PLAYER2);*/

        b.claim(0,0, TileState.PLAYER2);
/*        b.claim(1,0, TileState.PLAYER2);
        b.claim(0,1, TileState.PLAYER2);
        b.claim(1,1, TileState.PLAYER1);*/

        HexBoardState ib = new HexBoardState(b, TileState.PLAYER1);

        RandomHexBoardSimulation sim = new RandomHexBoardSimulation();
        System.out.println("STATE BEFORE SIM \n" + ib);
        int r = sim.simulate(ib);
        System.out.println("REWARD OF SIM = " + r);
    }
}
