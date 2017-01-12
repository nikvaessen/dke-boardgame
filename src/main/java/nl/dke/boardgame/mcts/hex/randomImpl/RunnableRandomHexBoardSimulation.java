package nl.dke.boardgame.mcts.hex.randomImpl;

import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.mcts.hex.RandomHexBoardSimulation;
import nl.dke.boardgame.mcts.mutliThread.RewardTracker;
import nl.dke.boardgame.mcts.mutliThread.RunnableSimulation;

/**
 * Created by nik on 12/01/17.
 */
public class RunnableRandomHexBoardSimulation
        extends RunnableSimulation<HexBoardState>
{

    public RunnableRandomHexBoardSimulation(HexBoardState state, RewardTracker tracker)
    {
        super(state, tracker);
    }

    /**
     * apply simulation on a given state to determine the reward
     *
     * @param state the state to use simulation on
     * @param times the amount of simulations being done
     * @return the sum of the rewards of the simulation
     */
    @Override
    public int simulate(HexBoardState state, int times)
    {
        if(times > 1 || times <= 0)
        {
            throw new IllegalArgumentException("runnable simulation can only simulate once");
        }
        return RandomHexBoardSimulation.simulate(state);
    }
}
