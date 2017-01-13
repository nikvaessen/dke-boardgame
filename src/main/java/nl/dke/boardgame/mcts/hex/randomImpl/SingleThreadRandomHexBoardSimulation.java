package nl.dke.boardgame.mcts.hex.randomImpl;

import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.mcts.hex.RandomHexBoardSimulation;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;

/**
 * Created by nik on 12/01/17.
 */
public class SingleThreadRandomHexBoardSimulation
        implements SimulationPolicy<HexBoardState>
{

    /**
     * apply simulation on a given state do determine the reward
     *
     * @param state the state to use simulation on
     * @param times the amount of simulations being done
     * @return the sum of the rewards of the simulation
     */
    @Override
    public int simulate(HexBoardState state, int times)
    {
        int reward = 0;
        for(int i = 0; i < times; i++)
        {
            reward += RandomHexBoardSimulation.simulate(state);
        }
        return reward;
    }
}

