package nl.dke.boardgame.mcts;

import nl.dke.boardgame.mcts.hex.HexBoardSimulation;
import nl.dke.boardgame.mcts.hex.HexBoardState;

/**
 * Created by Jeroen on 11-1-2017.
 */
public class HexSimulationThread extends Thread {
    private int reward;
    private HexBoardState state;

    public HexSimulationThread(HexBoardState state)
    {
        this.state = state;
    }

    @Override
    public void run() {
        reward = HexBoardSimulation.staticSimulate(state);
    }

    public int getReward() {
        return reward;
    }
}
