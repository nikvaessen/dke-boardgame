package nl.dke.boardgame.mcts.mutliThread;

import nl.dke.boardgame.mcts.State;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;

import java.util.concurrent.Callable;

/**
 * Created by nik on 12/01/17.
 */
public abstract class RunnableSimulation<S extends State>
    implements SimulationPolicy<S>, Runnable
{
    private RewardTracker tracker;
    private S state;

    public RunnableSimulation(S state, RewardTracker tracker)
    {
        this.tracker = tracker;
        this.state = state;
    }

    @Override
    public void run()
    {
        int reward = simulate(state, 1);
        if(reward < 0)
        {
            tracker.negative();
        }
        else if(reward > 0)
        {
            tracker.positive();
        }
        else
        {
            tracker.neutral();
        }
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
