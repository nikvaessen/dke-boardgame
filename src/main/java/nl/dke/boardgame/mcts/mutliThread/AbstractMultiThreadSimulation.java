package nl.dke.boardgame.mcts.mutliThread;

import nl.dke.boardgame.mcts.State;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by nik on 12/01/17.
 */
public abstract class AbstractMultiThreadSimulation<S extends State,
        T extends RunnableSimulation, U extends CallableSimulations<S>>
        implements SimulationPolicy<S>
{
    private ExecutorService threadPool;

    public AbstractMultiThreadSimulation()
    {
        System.out.printf("MultiThreaded MCTS is using %d processors%n", Runtime.getRuntime().availableProcessors());
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        //threadGroup = new ThreadGroup("Leaf parrallilization");
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
        int reward = 0;

        Collection<CallableSimulations<S>> callables = new ArrayList<>();
        for(int i = 0; i < times; i++)
        {
            callables.add(getCallableSimulation(state));
        }

        try
        {
            List<Future<Integer>> futures = threadPool.invokeAll(callables);
            for(Future<Integer> f : futures)
            {
                reward += f.get();
            }
        }
        catch(InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }

        return reward;
    }

    /**
     * Constructs the Runnable for a simulation
     *
     * @return a runnable which can be ran in a thread to do a simulation
     */
    public abstract T getRunnableSimulation(S state, RewardTracker tracker);

    /**
     * Construct a callable for a simulation
     * @param state the state for the callable
     * @return the callable which can be simulated
     */
    public abstract U getCallableSimulation(S state);

}
