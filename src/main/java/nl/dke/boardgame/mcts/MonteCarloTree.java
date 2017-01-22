package nl.dke.boardgame.mcts;

import nl.dke.boardgame.mcts.policy.SimulationPolicy;
import nl.dke.boardgame.mcts.policy.TreePolicy;
import nl.dke.boardgame.mcts.policy.UCTTreePolicy;

/**
 * A general class which can do MonteCarlo Tree Search
 *
 * @author nik  on 29/12/16.
 */
public class MonteCarloTree<S extends State, A extends Action<S>>
{

    public static final boolean DEEP_DEBUG = false;
    public static final boolean SEARCH_DEBUG = false;

    private TreePolicy<S, A> treePolicy;

    private SimulationPolicy<S> simulationPolicy;

    private int simulationsPerIteration;
    private MonteCarloRootNode<S, A> root;
    private MonteCarloNode<S, A> pastNode;
    private boolean treeReuse;

    public MonteCarloTree(TreePolicy<S, A> treePolicy, SimulationPolicy<S> simulationPolicy, int simsPerIt,
                          boolean treeReuse)
    {
        this.treePolicy = treePolicy;
        this.simulationPolicy = simulationPolicy;
        this.simulationsPerIteration = simsPerIt;
        this.treeReuse = treeReuse;
    }

    /**
     * This method makes sure the root exists and the most optimal root is chosen
     * It will try to use the tree from previous searches
     * @param initialState the initial state to use if no root exists
     */
    public void checkRoot(S initialState)
    {
        //MonteCarloRootNode<S,A> root = new MonteCarloRootNode<S, A>(initialState);
        if (pastNode == null || !treeReuse)
        {
            root = treePolicy.getNewRootNode(initialState);
//            System.out.printf("created new root. pastNode = null: %b\t !treeReuse: %b%n",
//                    pastNode == null, !treeReuse);
            return;
        }
//        System.out.printf("previous root was not null and had %d children\n", root.amountOfChildren());
//        System.out.printf("previous node has %d children%n", pastNode.amountOfChildren());
        MonteCarloRootNode<S, A> potentialNewRoot = null;
        MonteCarloNode<S, A> newRoot = null;
        for(MonteCarloNode<S, A> child : pastNode)
        {
            if(child.getState().equals(initialState))
            {
                potentialNewRoot = treePolicy.getNewRootNode(initialState);
                newRoot = child;
//                System.out.println("Found the child of past node which has the same state");
                break;
            }

        }
        if (potentialNewRoot == null)
        {
//            System.out.println("No grandchild with same state was found");
            root = treePolicy.getNewRootNode(initialState);
        }
        else
        {
//            System.out.printf("Transferring the children from the child of past node. It has %d children%n",
//                    newRoot.amountOfChildren());
            for (MonteCarloNode<S, A> child : newRoot)
            {
                child.setParent(potentialNewRoot);
                potentialNewRoot.addChild(child);
            }
            root = potentialNewRoot;
//            System.out.println(" FINISHED +and #children = " + root.amountOfChildren());
         }
    }

    /**
     * Do MCTS to find the best move to make
     * @param initialState the state to search the best for for
     * @param ms the time allowed to search for a move
     * @return the move to make
     * @throws IllegalArgumentException when the given time is negative
     */
    public A search(S initialState, long ms)
            throws IllegalArgumentException
    {
        if(ms <= 0)
        {
            throw new IllegalArgumentException(String.format("Cannot search for %d ms, which is <= 0", ms));
        }
        long startTime = System.currentTimeMillis(), start, end, count = 0;

        //make sure the correct root is selected
        checkRoot(initialState);

        if(SEARCH_DEBUG)
        {
            System.out.printf("Root starts with %d children %n", countNodes(root));
        }
        if(DEEP_DEBUG)
        {
            log("\n### Starting MCTS search ###\n");
            log("root node:\n" + root + "\n");
        }
        //first, expand the root node until all childs are explored
        while (!root.isFullyExpanded() && System.currentTimeMillis() - startTime < ms)
        {
            count++;
            if(DEEP_DEBUG)
            {
                log(String.format("\n####### iteration %d ######%n", count));
                start = System.nanoTime();
            }

            //do an iteration
            iteration(root);

            if(DEEP_DEBUG)
            {
                end = System.nanoTime();
                log("\nCurrent Tree:\n");
                debugTree(root);
                log(String.format("\nIteration %d of MCTS took %d nano seconds\n", count, end - start));
            }
        }

        //check if any child is a terminal child and wins the game. No search needed if there is
        S temp;
        for (MonteCarloNode<S, A> node : root)
        {
            temp = node.getState();
            if (temp.isTerminal() && simulationPolicy.simulate(temp, 1) == -1)
            {
                return node.getAction();
            }
        }

        // keep going until the allotted time has run out
        while (count <= 25000)
        {
            count++;
            if(DEEP_DEBUG)
            {
                log(String.format("\n####### iteration %d ######%n", count));
                start = System.nanoTime();
            }
            //do an iteration of MCTS
            iteration(root);

            if(DEEP_DEBUG)
            {
                end = System.nanoTime();
                log("\nCurrent Tree:\n");
                debugTree(root);
                log(String.format("\nIteration %d of MCTS took %d nano seconds\n", count, end - start));
            }
        }

        //print debug messages
        //System.out.printf("amount of iterations in %d ms: %d%n", ms, count);
        if(SEARCH_DEBUG)
        {
            System.out.println("total visits to root: " + root.getAttachable(UCTTreePolicy.TOTAL_VISITS).getValue());
            System.out.printf("Amount of nodes in the tree: %d%n", countNodes(root));
            System.out.println("Amount of children of root: " + root.amountOfChildren());
        }
        if(DEEP_DEBUG)
        {
            log("\nFinal Tree:\n");
            debugTree(root);
            MonteCarloNode<S, A> bestNode = treePolicy.bestRootChild((MonteCarloRootNode) root);
            log("\nBest node: \n" + bestNode + "\n");
        }

        //return the best child
        MonteCarloNode<S, A> bestChild = treePolicy.bestRootChild(root);

        //store the node resulting from the action taken to potentially reuse the tree
        //the next time a search is requested, which is probably a child of the node which
        //is getting stored
        pastNode = bestChild;

        return bestChild.getAction();
    }

    /**
     * Do an iteration of MCTS, which consists of selection, expansion, simulation
     * and backpropagation. Choosing a node with the tree policy should do selection and expansion at the same
     * time
     * @param root the root of the tree to do an MCTS iteration on
     */
    private void iteration(MonteCarloRootNode<S, A> root)
    {
        // select the critical node in the Tree which needs expanding
        // expand this node and store the child
        // if critical node cannot be expanded, critical node is returned instead
        MonteCarloNode<S, A> expandedChild = treePolicy.choose(root);

        if(DEEP_DEBUG)
        {
            log("Expanded child:\n" + expandedChild + "\n");
            log("\nsimulation:\n");
        }

        // simulate on the newly created child and backpropagate the results
        int reward = expandedChild.simulate(simulationPolicy, simulationsPerIteration);

        log("\nbackpropagation:\n");
        //backpropagate
        treePolicy.backpropagate(expandedChild, reward, simulationsPerIteration);
    }

    /**
     * Do MCTS to find the best move to make
     * @param initialState the state to search the best for for
     * @param ms the time allowed to search for a move
     * @return the move to make
     * @throws IllegalArgumentException when the given time is negative
     */
    public A search(S initialState, int ms)
            throws IllegalArgumentException
    {
        return search(initialState, Integer.toUnsignedLong(ms));
    }

    private void debugTree(MonteCarloRootNode<S, A> node)
    {
        if(!DEEP_DEBUG)
        {
            return;
        }
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
        if(DEEP_DEBUG)
        {
            System.out.print(message);
        }
    }

    private long countNodes(MonteCarloRootNode<S, A> root)
    {
        long count = 0;
        for(MonteCarloNode<S, A> node : root)
        {
            count++;
            count += countNodes(node);
        }
        return count;
    }

    private long countNodes(MonteCarloNode<S, A> node)
    {
        long count = 0;
        for(MonteCarloNode<S, A> child: node)
        {
            count++;
            count += countNodes(child);
        }
        return count;
    }
}
