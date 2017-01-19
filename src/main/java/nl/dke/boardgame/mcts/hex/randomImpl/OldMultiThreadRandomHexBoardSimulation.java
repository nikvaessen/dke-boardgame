package nl.dke.boardgame.mcts.hex.randomImpl;

import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.mcts.mutliThread.OldAbstractMultiThreadSimulation;
import nl.dke.boardgame.mcts.mutliThread.RewardTracker;
import nl.dke.boardgame.mcts.mutliThread.RunnableSimulation;

/**
 * Created by nik on 1/19/17.
 */
public class OldMultiThreadRandomHexBoardSimulation
    extends OldAbstractMultiThreadSimulation<HexBoardState, RunnableSimulation<HexBoardState>>
{
    public OldMultiThreadRandomHexBoardSimulation(int cores)
    {
        super(cores);
    }

    @Override
    public RunnableSimulation<HexBoardState> getRunnableSimulation(HexBoardState state, RewardTracker tracker)
    {
        return new RunnableRandomHexBoardSimulation(state, tracker);
    }
}
