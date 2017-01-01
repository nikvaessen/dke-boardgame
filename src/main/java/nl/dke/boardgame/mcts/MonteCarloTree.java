package nl.dke.boardgame.mcts;

import nl.dke.boardgame.mcts.policy.ActionPolicy;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;
import nl.dke.boardgame.mcts.policy.TreePolicy;

/**
 * Created by nik on 29/12/16.
 */
public class MonteCarloTree<S extends State, A extends Action<S> >
{

    private TreePolicy<S, A> treePolicy;

    private SimulationPolicy<S> simulationPolicy;

    private ActionPolicy<S, A> actionPolicy;

    public MonteCarloTree(TreePolicy<S, A> treePolicy, SimulationPolicy<S> simulationPolicy,
                          ActionPolicy<S, A> actionPolicy)
    {
        this.treePolicy = treePolicy;
        this.simulationPolicy = simulationPolicy;
        this.actionPolicy = actionPolicy;
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
        while(System.currentTimeMillis() - startTime < ms)
        {
            MonteCarloNode<S, A> criticalNode = treePolicy.choose(root);
            //criticalNode.expand()
            int reward = simulationPolicy.simulate(criticalNode.getState());
            criticalNode.backPropagate(reward);
        }
        return actionPolicy.select(root);
    }

    public A search(S initialState, int ms)
            throws IllegalArgumentException
    {
        return search(initialState, Integer.toUnsignedLong(ms));
    }

}
