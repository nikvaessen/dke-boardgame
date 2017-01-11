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
        System.out.println("\n### Starting MCTS search ###");
        MonteCarloRootNode<S, A> root = new MonteCarloRootNode<>(initialState);
        long startTime = System.currentTimeMillis() , start, end, count = 0;
        // keep going until the allotted time has run out
//        while(System.currentTimeMillis() - startTime < ms)
        System.out.println("root node:\n" + root);
        while (count < 30)
        {
            System.out.printf("\n####### iteration %d ######%n", count);
            count++;
            start = System.nanoTime();
            // select the critical node in the Tree which needs expanding
            // expand this node and store the child
            // if critical node cannot be expanded, critical node is returned instead
            MonteCarloNode<S, A> expandedChild = treePolicy.choose(root);
            System.out.println("Expanded child:\n" + expandedChild);
            // simulate on the newly created child and backpropagate the results
            System.out.println("\nsimulation:");
            expandedChild.simulate(simulationPolicy);
            end = System.nanoTime();
            System.out.println("\nCurrent Tree:");
            debugTree(root);
            System.out.printf("\nIteration %d of MCTS took %d nano seconds\n", count, end - start);
        }

        System.out.println("\nFinal Tree:");
        debugTree(root);
        MonteCarloNode<S, A> bestNode = treePolicy.bestRootChild((MonteCarloRootNode) root);
        System.out.println("\nBest node: \n" + bestNode);

        return treePolicy.bestRootChild(root).getAction();
    }

    public A search(S initialState, int ms)
            throws IllegalArgumentException
    {
        return search(initialState, Integer.toUnsignedLong(ms));
    }

    private void debugTree(MonteCarloRootNode<S, A> node)
    {
        System.out.println("Root node: \n" + node);
        int count = 0;
        System.out.println("\nchildren of root:\n");
        for(MonteCarloNode<S, A> child : node)
        {
            count++;
            System.out.println(child + "\n");
            debugChildren(child, 2);
        }

        System.out.printf("\nRoot has %d children\n", count);
    }

    private void debugChildren(MonteCarloNode<S, A> node, int layer)
    {
        boolean loop = false;
        for(MonteCarloNode<S, A> child : node)
        {
            if(!loop)
            {
                System.out.printf("\nchildren of above node in layer %d\n\n", layer);
                loop = true;
            }
            System.out.println(child);
            debugChildren(child, layer+1);
        }
        if(loop)
        {
            System.out.printf("______end of children in layer %d______\n\n",
                    layer);
        }
    }

}
