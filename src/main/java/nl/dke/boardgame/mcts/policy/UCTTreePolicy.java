package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.MonteCarloNode;
import nl.dke.boardgame.mcts.MonteCarloRootNode;
import nl.dke.boardgame.mcts.State;
import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.util.IntegerCounter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Implements a tree policy based on the UCT algorithm
 *
 * @author nik
 */
public class UCTTreePolicy<S extends State, A extends Action<S>>
        implements TreePolicy<S, A>
{

    /**
     * Unique names to give the counter objects given to every node which is expanded
     */
    public static final String TOTAL_REWARDS = "r";
    public static final String TOTAL_VISITS  = "v";

    /**
     * Exploration value to use for selecting which node to simulate
     */
    private double explorationParameter;

    /**
     * Random number generator used in breaking ties of nodes with same UCT value
     */
    private Random rng = new Random(System.currentTimeMillis());

    /**
     * Create a UCTTreePolicy with a given value C, which is the exploration parameter to use
     *
     * @param explorationParameter the exploration parameter which defines how much exploration vs exploitation takes
     *                             place
     * @throws IllegalArgumentException when the given exploration value is less than 0
     */
    public UCTTreePolicy(double explorationParameter)
            throws IllegalArgumentException
    {
        if(explorationParameter < 0)
        {
            throw new IllegalArgumentException("The exploration parameter cannot be less than 0");
        }
        this.explorationParameter = explorationParameter;
    }

    /**
     * This method chooses the node to expand in a iteration of the MonteCarlo Tree search.
     * It will start of by selecting every possible child of the root node. After the root
     * node is fully expanded, it will return the best child. After that, every new best child will be fully
     * expanded before its new best child is returned
     *
     * @param root the root node of a MonteCarlo Tree
     * @return the node to expand in an iteration of MCTS
     */
    @Override
    public MonteCarloNode<S, A> choose(MonteCarloRootNode<S, A> root)
    {
        MonteCarloNode<S, A> node = root;
        while(!node.getState().isTerminal())
        {
            if(!node.isFullyExpanded())
            {
                return node.expand(this);
            }
            else
            {
                node = bestChild(node);
            }
        }
        return node;
    }

    /**
     * Give a legal child which can be used to expand a given MonteCarloNode by randomly creating new child
     * based on a possible action of the given node's State
     *
     * @param node the MonteCarloNode to expand
     * @return a MonteCarloNode which is a legal child of the given Node
     * @throws IllegalArgumentException when the given node cannot be expanded anymore (no valid legal actions left)
     */
    @Override
    public MonteCarloNode<S, A> expand(MonteCarloNode<S, A> node) throws IllegalArgumentException
    {
        List<A> actions = node.getState().possibleActions();

        //System.out.println("before action pruning: " + actions.size());
        //remove all actions which have already have been expanded
        removeUsedActions(node, actions);
        //System.out.println("after action pruning: " + actions.size());

        if(actions.isEmpty())
        {
            throw new IllegalArgumentException("This node cannot be expanded anymore");
        }
        else
        {
            A randomPossibleAction = actions.remove(rng.nextInt(actions.size()));
            if(actions.isEmpty())
            {
                node.setFullyExpanded(true);
            }
            MonteCarloNode<S, A> newNode = new MonteCarloNode<>(node, randomPossibleAction);
            attachCounters(newNode);
            return newNode;
        }
    }

    /**
     * Attach the necessary counters to the montecarlonode
     * @param node the node to attach the counters to
     */
    protected void attachCounters(MonteCarloNode<S, A> node)
    {
        try
        {
            node.attach(new IntegerCounter(TOTAL_REWARDS));
            node.attach(new IntegerCounter(TOTAL_VISITS));
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Selects the best child of a given node by using the UCT algorithm. If more than one children have the same
     * UCT value, among them one is randomly selected
     *
     * @param node the node of the MonteCarlo Tree to select a child on
     * @return The child with the highest UCT value
     * @throws IllegalArgumentException when the given node does not have any children
     */
    @Override
    public MonteCarloNode<S, A> bestChild(MonteCarloNode<S, A> node)
            throws IllegalArgumentException
    {
        return bestChild(node, getNodeValueFunction());
    }

    /**
     * Select the best child of the root node by going over all children and selecting the node
     * with the highest Q value
     *
     * @param root the root of the monte carlo tree
     * @return the node with the highest q value
     */
    @Override
    public MonteCarloNode<S, A> bestRootChild(MonteCarloRootNode<S, A> root)
    {
        return bestChild(root, getRootNodeValueFunction());
    }

    /**
     * After simulation of a node, a reward is given based on the winning chances of the state.
     * Alternatively add this reward and the negation of the reward back up the tree until the root is found.
     * @param node the node to start backpropagating on
     * @param reward the reward to backpropagate
     */
    @Override
    public void backpropagate(MonteCarloNode<S, A> node, int reward)
    {
        backpropagate(node, reward, 1);
    }

    /**
     * After simulation of a node, a reward is given based on the winning chances of the state.
     * Alternatively add this reward and the negation of the reward back up the tree until the root is found.
     *
     * @param node the node to start backpropagating on
     * @param reward the reward to backpropagate
     * @param times the amount of simulations which took place (>= 1)
     */
    @Override
    public void backpropagate(MonteCarloNode<S, A> node, int reward, int times)
    {
        //visits += simPerNode;
        node.getAttachable(TOTAL_VISITS).increment(times);
        //qValues += q;
        node.getAttachable(TOTAL_REWARDS).increment(reward);
        if(node.isRoot())
        {
            return;
        }
        backpropagate(node.getParent(), reward, times);
    }

    @Override
    public MonteCarloRootNode<S, A> getNewRootNode(S initialstate)
    {
        MonteCarloRootNode<S, A> root = new MonteCarloRootNode<>(initialstate);
        attachCounters(root);
        return root;
    }

    /**
     * select the best child of the given node by using the given value function to determine the value
     * of a node
     *
     * @param node the node to select the best child on
     * @param f    the function used to give a value to a child
     * @return the child with the highest UCT value. A child can be randomly selected if more than one node
     * have the same maximum UCT value
     */
    private MonteCarloNode<S, A> bestChild(MonteCarloNode<S, A> node, Function<MonteCarloNode<S, A>, Double> f)
    {
        // search for the node with the maximum UCT value ( or more than 1 if some have same value)
        List<MonteCarloNode<S, A>> maxNodes = findMaxChild(node, f);

        //return the best node (select randomly if more than one node have same maximum value)
        if(maxNodes.size() == 0)
        {
            throw new IllegalArgumentException("The given node does not have any children");
        }
        else if(maxNodes.size() == 1)
        {
            return maxNodes.get(0);
        }
        else
        {
            return maxNodes.get(rng.nextInt(maxNodes.size()));
        }
    }

    /**
     * given a MonteCarloNode, return a list with 1 or more children of the given node
     * which have the highest UCT value for all the children of the given node
     * @param node the node to find children for
     * @return a list of children of the node. List is empty if given node does not have any children,
     * size of 1 if one node has the maximum value, or larger if more nodes share the same maximum UCT value
     */
    private List<MonteCarloNode<S, A>> findMaxChild(MonteCarloNode<S, A> node,
                                                    Function<MonteCarloNode<S, A>, Double> f)
    {
        List<MonteCarloNode<S, A>> maxNodes = new ArrayList<>();
        double maxValue = 0, childValue;
        for(MonteCarloNode<S, A> child : node)
        {
            if(maxNodes.isEmpty())
            {
                maxNodes.add(child);
                maxValue = f.apply(child);
            }
            else
            {
                childValue = f.apply(child);
                if(Math.abs(childValue - maxValue) < 0.00001d) // they are equal
                {
                    maxNodes.add(child);
                }
                else if(childValue > maxValue)
                {
                    maxNodes.clear();
                    maxNodes.add(child);
                    maxValue = childValue;
                }
            }
        }
        return maxNodes;
    }

    /**
     * Gives the function to calculate the value of a node which is used to decide which nodes needs
     * to be selected in the tree search part of MCTS
     * @return the value of a node using the UCT value
     */
    protected Function<MonteCarloNode<S, A>, Double> getNodeValueFunction()
    {
        return (MonteCarloNode<S, A> node) -> getUCTValue(node, explorationParameter);
    }

    /**
     * Gives the function to calculate the value of a child of the root node when deciding which
     * action is best action after MCTS has terminated
     *
     * @return the value of a node using the UCT value with c = 0
     */
    protected Function<MonteCarloNode<S, A>, Double> getRootNodeValueFunction()
    {
        return (MonteCarloNode<S, A> node) -> getUCTValue(node, 0);
    }

    /**
     * Computes the UCT value of a node. The upper confidence bound is defined as:
     * <p>
     * Q(node)             ( 2 * ln(N(parent)))
     * -------  + C * sqrt ( ---------------  )
     * N(node)             (     N(Node)      )
     * <p>
     * where Q(node) = the total reward a Node has gotten
     * N(node) = the total amount a node is visited
     * C = a hyperparameter >= 0 which us used to to set how important exploration is, where 0 is no exploration
     * and 1 is a lot of exploration
     * <p>
     * if a node is visited 0 times, the value is set to infinity
     *
     * @param node the node to compute UCT on
     * @param c    the exploration value
     * @return the computed ICT value which is >= 0
     * @throws IllegalArgumentException when the given node does not have a parent
     */
    public static double getUCTValue(MonteCarloNode node, double c)
            throws IllegalArgumentException
    {
        double n = node.getAttachable(TOTAL_VISITS).getValue().doubleValue();
        double q = node.getAttachable(TOTAL_REWARDS).getValue().doubleValue();
        MonteCarloNode parent = node.getParent();
        if(parent == null)
        {
            throw new IllegalArgumentException("cannot compute UCT value, given node does not have a parent");
        }
        if(Math.abs(n - 0) < 0.00001d) // equal to 0
        {
            return Double.MAX_VALUE; //infinity
        }
        else
        {
            double nP = parent.getAttachable(TOTAL_VISITS).getValue().doubleValue();
            return (q / n) + c * Math.sqrt(2 * Math.log(nP) / n);
        }
    }

    /**
     * Removes all the actions which are already expanded on the given node from the given list
     *
     * @param node            the node
     * @param possibleActions the list of actions
     */
    private void removeUsedActions(MonteCarloNode<S, A> node, List<A> possibleActions)
    {
        //make a list of all used actions
        List<A> toRemove = new ArrayList<A>();
        for(MonteCarloNode<S, A> child : node)
        {
            toRemove.add(child.getAction());
        }

        //go over the possible action list and remove the element if it is in the list of used actions
        Iterator<A> iterator = possibleActions.listIterator();
        while(iterator.hasNext())
        {
            Action action = iterator.next();
            if(toRemove.contains(action))
            {
                iterator.remove();
            }
        }
    }

    /**
     * get exploration value
     * @return the exploration value
     */
    public double getExplorationParameter()
    {
        return explorationParameter;
    }
}
