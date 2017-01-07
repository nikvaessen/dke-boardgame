package nl.dke.boardgame;

import nl.dke.boardgame.mcts.hex.HexBoardSimulation;
import nl.dke.boardgame.mcts.hex.HexBoardState;
import org.junit.Test;

/**
 * Created by nik on 08/01/17.
 */
public class HexBoardSimulationTest
{
    @Test
    public void testSimulation()
    {
        HexBoardState ib = new HexBoardState(11, 11);
        HexBoardSimulation sim = new HexBoardSimulation();
        int r = sim.simulate(ib);
        System.out.println(r);
    }
}
