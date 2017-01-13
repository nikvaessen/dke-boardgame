package nl.dke.boardgame.mcts.mutliThread;

import nl.dke.boardgame.mcts.State;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by nik on 12/01/17.
 */
public abstract class AbstractMultiThreadSimulation<S extends State, T extends RunnableSimulation>
        implements SimulationPolicy<S>
{
    private ExecutorService threadPool;

    public AbstractMultiThreadSimulation()
    {
        System.out.printf("MultiThreaded MCTS is using %d processors%n", Runtime.getRuntime().availableProcessors());
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Multithreaded simulation
     *
     * @param state the state to use simulation on
     * @param times the amount of simulations being done
     * @return the sum of the rewards of the simulations which have taken place
     */
    @Override
    public synchronized int simulate(S state, int times)
    {
        final RewardTracker rewards = new RewardTracker();

        ArrayList<Future> futures = new ArrayList<>();
        for(int i = 0; i < times; i++)
        {
            futures.add(threadPool.submit(getRunnableSimulation(state, rewards)));
        }

        Future temp;
        while(!futures.isEmpty())
        {
            Iterator<Future> it = futures.iterator();
            while(it.hasNext())
            {
                temp = it.next();
                if(temp.isDone())
                {
                    it.remove();
                }
            }
        }

        if(rewards.getCount() < times)
        {
            throw new IllegalStateException(String.format("Did not simulate enough times. Expected: %d Actual: %d",
                    times, rewards.getCount()));
        }
        return rewards.getReward();
    }

    /**
     * Constructs the Runnable for a simulation
     *
     * @return a runnable which can be ran in a thread to do a simulation
     */
    public abstract T getRunnableSimulation(S state, RewardTracker tracker);

}
