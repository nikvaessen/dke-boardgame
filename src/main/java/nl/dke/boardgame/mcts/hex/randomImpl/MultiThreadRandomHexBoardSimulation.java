package nl.dke.boardgame.mcts.hex.randomImpl;

import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.mcts.mutliThread.AbstractMultiThreadSimulation;
import nl.dke.boardgame.mcts.mutliThread.RewardTracker;

/**
 * Created by nik on 12/01/17.
 */
public class MultiThreadRandomHexBoardSimulation
    extends AbstractMultiThreadSimulation<HexBoardState, RunnableRandomHexBoardSimulation>
{
    /**
     * Constructs the thread for a simulation
     *
     * @param tracker
     * @return a thread which can be started to do a simulation
     */
    @Override
    public RunnableRandomHexBoardSimulation getRunnableSimulation(HexBoardState state, RewardTracker tracker)
    {
        return new RunnableRandomHexBoardSimulation(state, tracker);
    }
}
