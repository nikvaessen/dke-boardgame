package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.MonteCarloNode;
import nl.dke.boardgame.mcts.MonteCarloRootNode;
import nl.dke.boardgame.mcts.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implements a tree policy based on the UCT algorithm
 *
 * @author nik
 */
public class UCTTreePolicy <S extends State, A extends Action<S> >
    implements TreePolicy<S, A>
{

    private double explorationParameter;
    private Random rng = new Random(System.currentTimeMillis());

    /**
     * Create a UCTTreePolicy with a given value C, which is the exploration parameter to use
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
     * It will start of by selecting selecting every possible child of the root node. After the root
     * node is fully expanded, it will return the best child. After that, every new best child will be fully
     * expanded before its new best child is returned
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
                return node;
            }
            else
            {
                node = bestNode(node);
            }
        }
        return node;
    }

    /**
     * Give a legal child which can be used to expand a given MonteCarloNode by randommly creating new child
     * based on a possible action of the given node's State
     * @param node the MonteCarloNode to expand
     * @return a MonteCarloNode which is a legal child of the given Node
     * @throws IllegalArgumentException when the given node cannot be expanded anymore (no valid legal actions left)
     */
    @Override
    public MonteCarloNode<S, A> expand(MonteCarloNode<S, A> node) throws IllegalArgumentException
    {
        List<A> actions = node.getState().possibleActions();
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
            return new MonteCarloNode<S, A>(node, randomPossibleAction);
        }
    }

    /**
     * Selects the best child of a given node by using the UCT algorithm. If more than one children have the same
     * UCT value, among them one is randomly selected
     * @param node the node of the MonteCarlo Tree to select a child on
     * @return The child with the highest UCT value
     * @throws IllegalArgumentException when the given node does not have any children
     */
    @Override
    public MonteCarloNode<S, A> bestNode(MonteCarloNode<S, A> node)
            throws IllegalArgumentException
    {
        ArrayList<MonteCarloNode<S, A>> maxNodes = new ArrayList<>();
        double maxUCT = 0, childUCT;
        for(MonteCarloNode<S, A> child : node)
        {
            if(maxNodes.isEmpty())
            {
                maxNodes.add(child);
                maxUCT = getUCTValue(child);
            }
            else
            {
                childUCT = getUCTValue(child);
                if(Math.abs(childUCT - maxUCT) < 0.00001d) // they are equal
                {
                    maxNodes.add(child);
                }
                else if(childUCT > maxUCT)
                {
                    maxNodes.clear();
                    maxNodes.add(child);
                    maxUCT = childUCT;
                }
            }
        }

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
     * Computes the UCT value of a node. The upper confidence bound is defined as:
     *
     *          Q(node)             ( 2 * ln(N(node))  )
     *          -------  + C * sqrt ( ---------------  )
     *          N(node)             (     N(Node)      )
     *
     * where Q(node) = the total reward a Node has gotten
     *       N(node) = the total amount a node is visited
     *       C = a hyperparameter >= 0 which us used to to set how important exploration is, where 0 is no exploration
     *           and 1 is a lot of exploration
     *
     * if a node is visited 0 times, the value is set to infinity
     *
     * @param node the node to compute UCT on
     * @return the computed ICT value which is >= 0
     */
    private double getUCTValue(MonteCarloNode node)
    {
        double n = node.getVisits();
        double q = node.getqValues();
        if(Math.abs(n - 0) < 0.00001d) // equal to 0
        {
            return Double.MAX_VALUE; //infinity
        }
        else
        {
            return (q/n) + explorationParameter * Math.sqrt( 2 * Math.log(n) / n );
        }
    }

}
