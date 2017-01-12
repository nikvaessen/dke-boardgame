package nl.dke.boardgame.mcts.mutliThread;

import nl.dke.boardgame.mcts.State;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nik on 12/01/17.
 */
public abstract class AbstractMultiThreadSimulation<S extends State, T extends RunnableSimulation>
    implements SimulationPolicy<S>
{
    private static int processors = Runtime.getRuntime().availableProcessors();

    /**
     * Multithreaded simulation
     * @param state the state to use simulation on
     * @param times the amount of simulations being done
     * @return the sum of the rewards of the simulations which have taken place
     */
    @Override
    public synchronized int simulate(S state, int times)
    {
        final RewardTracker rewards = new RewardTracker();
        ExecutorService threadPool = Executors.newFixedThreadPool(processors);

        for(int i = 0; i < times; i++)
        {
            threadPool.submit(getRunnableSimulation(state, rewards));
        }
        threadPool.shutdown();

        if(rewards.getCount() < times)
        {
            throw new IllegalStateException("Did not simulate enough times");
        }
        return rewards.getReward();
    }

    /**
     * Constructs the thread for a simulation
     * @return a thread which can be started to do a simulation
     */
    public abstract T getRunnableSimulation(S state, RewardTracker tracker);

}
