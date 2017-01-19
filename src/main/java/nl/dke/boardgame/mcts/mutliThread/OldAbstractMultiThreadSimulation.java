package nl.dke.boardgame.mcts.mutliThread;


import nl.dke.boardgame.mcts.State;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;

/**
 * Created by nik on 12/01/17.
 */
public abstract class OldAbstractMultiThreadSimulation<S extends State, T extends RunnableSimulation<S>>
        implements SimulationPolicy<S>
{

    private int numberOfThreads;

    public OldAbstractMultiThreadSimulation(int cores)
    {
        int available = Runtime.getRuntime().availableProcessors();
        if(cores > available || cores <= 0)
        {
            throw new IllegalArgumentException(String.format("Cannot start %d threads as this computer only has" +
                    " %d", cores, available));
        }
        numberOfThreads = cores;
        System.out.printf("MultiThreaded MCTS is using %d processors%n", cores);
    }

    /**
     * Multithreaded simulation
     *
     * @param state the state to use simulation on
     * @param times the amount of simulations being done
     * @return the sum of the rewards of the simulations which have taken place
     */
    @Override
    public int simulate(S state, int times)
    {
        final RewardTracker rewards = new RewardTracker();
        final ThreadGroup threadGroup = new ThreadGroup("Parralel");

        Runnable[] runnables = new Runnable[times];
        for(int i = 0; i < runnables.length; i++)
        {
            Thread t = new Thread(threadGroup, getRunnableSimulation(state, rewards));
            t.start();
            t = null;
        }

        synchronized(threadGroup)
        {
            while(threadGroup.activeCount() > 0)
            {
                try
                {
                    threadGroup.wait();
                } catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        threadGroup.destroy();

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

