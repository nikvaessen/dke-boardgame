package nl.dke.boardgame.mcts.mutliThread;

import nl.dke.boardgame.mcts.State;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;

import java.util.concurrent.Callable;

/**
 * Created by nik on 1/19/17.
 */
public abstract class CallableSimulations<S extends State>
    implements Callable<Integer>, SimulationPolicy<S>
{
    private S state;

    public CallableSimulations(S stateToSimulate)
    {
        this.state = stateToSimulate;
    }

    @Override
    public Integer call() throws Exception
    {
        return simulate(state, 1);
    }

    /**
     * apply simulation on a given state to determine the reward
     *
     * @param state the state to use simulation on
     * @param times the amount of simulations being done
     * @return the sum of the rewards of the simulation
     */
    @Override
    public abstract int simulate(S state, int times);
}
