package nl.dke.boardgame.mcts;

import nl.dke.boardgame.mcts.policy.SimulationPolicy;
import nl.dke.boardgame.mcts.policy.TreePolicy;

/**
 * A general class which can do MonteCarlo Tree Search
 *
 * @author nik  on 29/12/16.
 */
public class MonteCarloTree<S extends State, A extends Action<S>>
{

    public static final boolean DEBUG = false;

    private TreePolicy<S, A> treePolicy;

    private SimulationPolicy<S> simulationPolicy;

    private int simulationsPerIteration;
    public static int numberNodes = 0;

    public MonteCarloTree(TreePolicy<S, A> treePolicy, SimulationPolicy<S> simulationPolicy, int simsPerIt)
    {
        this.treePolicy = treePolicy;
        this.simulationPolicy = simulationPolicy;
        this.simulationsPerIteration = simsPerIt;
        numberNodes = 0;
    }


    public A search(S initialState, long ms)
            throws IllegalArgumentException
    {
        if(ms <= 0)
        {
            throw new IllegalArgumentException(String.format("Cannot search for %d ms, which is <= 0", ms));
        }
        long startTime = System.currentTimeMillis(), start, end, count = 0;

        log("\n### Starting MCTS search ###\n");
        MonteCarloRootNode<S, A> root = new MonteCarloRootNode<>(initialState);
        log("root node:\n" + root + "\n");

        //first, expand the root node until all childs are explored
        while(!root.isFullyExpanded() && System.currentTimeMillis() - startTime < ms)
        {
            log(String.format("\n####### iteration %d ######%n", count));
            count++;
            start = System.nanoTime();

            //do an iteration
            iteration(root);

            end = System.nanoTime();
            log("\nCurrent Tree:\n");
            debugTree(root);
            log(String.format("\nIteration %d of MCTS took %d nano seconds\n", count, end - start));

        }

        //check if any child is a terminal child and wins the game. No search needed if there is
        S temp;
        for(MonteCarloNode<S, A> node : root)
        {
            temp = node.getState();
            if(temp.isTerminal() && simulationPolicy.simulate(temp, 1) == -1)
            {
                return node.getAction();
            }
        }

        // keep going until the allotted time has run out
        while(System.currentTimeMillis() - startTime < ms)
        {
            log(String.format("\n####### iteration %d ######%n", count));
            count++;
            start = System.nanoTime();

            //do an iteration of MCTS
            iteration(root);

            end = System.nanoTime();
            log("\nCurrent Tree:\n");
            debugTree(root);
            log(String.format("\nIteration %d of MCTS took %d nano seconds\n", count, end - start));
        }

        //print debug messages
        System.out.println("total visits to root: " + root.getVisits());
        if(DEBUG)
        {
            log("\nFinal Tree:\n");
            debugTree(root);
            MonteCarloNode<S, A> bestNode = treePolicy.bestRootChild((MonteCarloRootNode) root);
            log("\nBest node: \n" + bestNode + "\n");
        }

        //return the best child
        return treePolicy.bestRootChild(root).getAction();
    }

    private void iteration(MonteCarloRootNode<S, A> root)
    {
        // select the critical node in the Tree which needs expanding
        // expand this node and store the child
        // if critical node cannot be expanded, critical node is returned instead
        MonteCarloNode<S, A> expandedChild = treePolicy.choose(root);
        log("Expanded child:\n" + expandedChild + "\n");

        // simulate on the newly created child and backpropagate the results
        log("\nsimulation:\n");
        expandedChild.simulate(simulationPolicy, simulationsPerIteration);
    }

    public A search(S initialState, int ms)
            throws IllegalArgumentException
    {
        return search(initialState, Integer.toUnsignedLong(ms));
    }

    private void debugTree(MonteCarloRootNode<S, A> node)
    {
        log("Root node: \n" + node + "\n");
        int count = 0;
        log("\nchildren of root:\n\n");
        for(MonteCarloNode<S, A> child : node)
        {
            count++;
            log(child + "\n\n");
            debugChildren(child, 2);
        }
        log(String.format("\nRoot has %d children\n", count));
    }

    private void debugChildren(MonteCarloNode<S, A> node, int layer)
    {
        boolean loop = false;
        for(MonteCarloNode<S, A> child : node)
        {
            if(!loop)
            {
                log(String.format("\nchildren of above node in layer %d\n\n", layer));
                loop = true;
            }
            log(child + "\n");
            debugChildren(child, layer + 1);
        }
        if(loop)
        {
            log(String.format("______end of children in layer %d______\n\n", layer));
        }
    }

    private static void log(String message)
    {
        if(DEBUG)
        {
            System.out.print(message);
        }
    }
}