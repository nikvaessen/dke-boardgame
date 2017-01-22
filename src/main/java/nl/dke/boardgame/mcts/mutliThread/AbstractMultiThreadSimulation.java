package nl.dke.boardgame.mcts.mutliThread;

import nl.dke.boardgame.mcts.State;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by nik on 12/01/17.
 */
public abstract class AbstractMultiThreadSimulation<S extends State,U extends CallableSimulations<S>>
        implements SimulationPolicy<S>
{
    private ExecutorService threadPool;

    public AbstractMultiThreadSimulation(int cores)
    {
        int available = Runtime.getRuntime().availableProcessors();
        if(cores > available || cores <= 0)
        {
            throw new IllegalArgumentException(String.format("Cannot start %d threads as this computer only has" +
                    " %d", cores, available));
        }
        threadPool = Executors.newFixedThreadPool(cores);
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
     * Construct a callable for a simulation
     * @param state the state for the callable
     * @return the callable which can be simulated
     */
    public abstract U getCallableSimulation(S state);

}
