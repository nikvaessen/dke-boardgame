package nl.dke.boardgame.mcts.hex.randomImpl;

import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.mcts.mutliThread.AbstractMultiThreadSimulation;
import nl.dke.boardgame.mcts.mutliThread.CallableSimulations;
import nl.dke.boardgame.mcts.mutliThread.RewardTracker;

/**
 * Created by nik on 12/01/17.
 */
public class MultiThreadRandomHexBoardSimulation
        extends AbstractMultiThreadSimulation<HexBoardState, CallableSimulations<HexBoardState>>
{
    public MultiThreadRandomHexBoardSimulation(int cores)
    {
        super(cores);
    }

    @Override
    public CallableSimulations<HexBoardState> getCallableSimulation(HexBoardState state)
    {
        return new CallableRandomHexBoardSimulation(state);
    }
}
