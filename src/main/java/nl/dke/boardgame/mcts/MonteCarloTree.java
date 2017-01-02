package nl.dke.boardgame.mcts;

import nl.dke.boardgame.mcts.policy.SimulationPolicy;
import nl.dke.boardgame.mcts.policy.TreePolicy;

/**
 * Created by nik on 29/12/16.
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
            MonteCarloNode<S, A> criticalNode = treePolicy.choose(root);
            // expand this node and store the child
            MonteCarloNode<S, A> child = criticalNode.expand(treePolicy);
            // simulate on the newly created child and backpropagate the results
            child.simulate(simulationPolicy);
        }
        return treePolicy.bestNode(root).getAction();
    }

    public A search(S initialState, int ms)
            throws IllegalArgumentException
    {
        return search(initialState, Integer.toUnsignedLong(ms));
    }

}
