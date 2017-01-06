package nl.dke.boardgame.mcts;

import nl.dke.boardgame.mcts.policy.SimulationPolicy;
import nl.dke.boardgame.mcts.policy.TreePolicy;

/**
 * A general class which can do MonteCarlo Tree Search
 *
 * @author nik  on 29/12/16.
 */
public class MonteCarloTree<S extends State, A extends Action<S> >
{

    private TreePolicy<S, A> treePolicy;

    private SimulationPolicy<S> simulationPolicy;

    public MonteCarloTree(TreePolicy<S, A> treePolicy, SimulationPolicy<S> simulationPolicy)
    {
        this.treePolicy = treePolicy;
        this.simulationPolicy = simulationPolicy;
    }


    public A search(S initialState, long ms)
            throws IllegalArgumentException
    {
        if(ms <= 0)
        {
            throw new IllegalArgumentException(String.format("Cannot search for %d ms, which is <= 0", ms));
        }

        MonteCarloRootNode<S, A> root = new MonteCarloRootNode<>(initialState);
        long startTime = System.currentTimeMillis();
        // keep going until the allotted time has run out
        while(System.currentTimeMillis() - startTime < ms)
        {
            // select the critical node in the Tree which needs expanding
            // expand this node and store the child
            // if critical node cannot be expanded, critical node is returned instead
            MonteCarloNode<S, A> expandedChild = treePolicy.choose(root);
            // simulate on the newly created child and backpropagate the results
            expandedChild.simulate(simulationPolicy);
        }
        return treePolicy.bestRootChild(root).getAction();
    }

    public A search(S initialState, int ms)
            throws IllegalArgumentException
    {
        return search(initialState, Integer.toUnsignedLong(ms));
    }

}
