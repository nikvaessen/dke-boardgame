package nl.dke.boardgame.mcts;

import nl.dke.boardgame.mcts.policy.SimulationPolicy;
import nl.dke.boardgame.mcts.policy.TreePolicy;

import java.util.*;
import java.util.function.Consumer;

/**
 * This class implements a node in a MonteCarlo tree. It stores the amount of times it is visted, the total reward
 * it has retrieved over the whole tree search, and it represents a State in the MonteCarlo Tree. The State is not
 * stored due to memory constraints, but t is computed by every node storing the action, and retrieving
 * all actions and applying them to the initial state of the the root node.
 *
 * @author nik in 29/12/16.
 */
public class MonteCarloNode<S extends State, A extends Action<S> >
        implements Iterable<MonteCarloNode<S, A>>
{
    /**
     * Random number generator being shared on all nodes
     */
    private static Random rng = new Random(System.currentTimeMillis());

    /**
     * sum of all q-values this node retrieves during backpropagation
     */
    private int qValues = 0;

    /**
     * counter of all visits of this node during search
     */
    private int visits = 0;

    /**
     * Action to get from the parent node to this node
     */
    private A action;

    /**
     * Boolean flag which becomes true when this node has all possible child nodes
     */
    private boolean fullyExpanded = false;

    /**
     * The parent of this node
     */
    private MonteCarloNode<S, A> parent;

    /**
     * Create a MonteCarloNode with a given parent and the action which can be applied on the parent
     * to get to this nodes' State
     * @param parent the parentof this node
     * @param action the action which leads to this node
     */
    public MonteCarloNode(MonteCarloNode<S, A> parent, A action)
    {
        this.parent = parent;
        this.action = action;
    }

    /**
     * List containing all expanded children of this node
     */
    private LinkedList<MonteCarloNode<S, A>> children = new LinkedList<>();

    /**
     * Get the State created by applying all actions from this Node
     * @return the state which this node is representing
     */
    public S getState()
    {
        return parent.getState().next(action);
    }

    /**
     * Get an ArrayList of all the actions from this node back to the root Node
     * @return an ArrayList of actions to to be applied to the initial state to get to this state
     */
    public ArrayList<A> getActions()
    {
        ArrayList<A> actions = new ArrayList<>();
        if(isRoot())
        {
            return actions;
        }
        else
        {
            actions.add(action);
            return parent.getActions(actions);
        }
    }

    /**
     * Get an ArrayList of all the actions from this node back to the root Node
     * @param futureActions The ArrayList of actions from child nodes
     * @return an ArrayList of actions to to be applied to the initial state to get to this state and
     * the actions already in the ArrayList to get to child nodes
     */
    private ArrayList<A> getActions(ArrayList<A> futureActions)
    {
        if(isRoot())
        {
            return futureActions;
        }
        else
        {
            futureActions.add(action);
            return parent.getActions(futureActions);
        }
    }

    /**
     * Makes this node create a child node. It does this by selecting a random action from the
     * possible actions. If there are no more children to create, nothing will be done
     * @return The new MonteCarloNode which is a child of this node
     * @throws IllegalArgumentException when this node cannot be expanded
     */
    public MonteCarloNode<S, A> expand(TreePolicy<S, A> treePolicy)
        throws IllegalArgumentException
    {
        MonteCarloNode<S, A> newNode = treePolicy.expand(this);
        children.add(newNode);
        return newNode;
    }

    /**
     * Returns whether this node has a potential child node to expand(add as a child)
     * @return
     */
    public boolean isFullyExpanded()
    {
        return fullyExpanded;
    }

    /**
     * Set that this MonteCarloNode is fully expanded (has no more new potential children)
     * @param fullyExpanded whether this node is fully expanded
     */
    public void setFullyExpanded(boolean fullyExpanded)
    {
        this.fullyExpanded = fullyExpanded;
    }

    /**
     * Simulate on the state of this MonteCarloNode and backpropagate the results of the simulation (e.g win/loss)
     * back to the root of the tree
     * @param simulationPolicy the policy which is able to do a simulation on the State of this node
     */
    public void simulate(SimulationPolicy<S> simulationPolicy)
    {
        int reward = simulationPolicy.simulate(getState());
        if(getRoot().getState().nextActor() == getState().nextActor())
        {
            backPropagate(reward);
        }
        else
        {
            backPropagate(-reward);
        }
    }

    public MonteCarloNode getRoot()
    {
        if(isRoot())
        {
            return this;
        }
        else
        {
            return parent.getRoot();
        }
    }

    /**
     * After simulation of a node, a reward is given based on the winning chances of the state.
     * Alternatively add this reward and the negation of the reward back up the tree until the root is found.
     * @param q the q-value to propagate back to the root of the tree
     */
    private void backPropagate(int q)
    {
        visits++;
        qValues += q;
        if(isRoot())
        {
            return;
        }
        parent.backPropagate(-q);
    }

    /**
     * returns whether this node is a root node
     * @return whether this is the root node of the tree
     */
    public boolean isRoot()
    {
        return parent == null;
    }

    /**
     * Get the parent node of this MonteCarloNode. Will be null if it's the root node
     * @return the parent node or null if it's the root node
     */
    public MonteCarloNode<S, A> getParent()
    {
        return parent;
    }

    /**
     * Get the action which lead to State of this MonteCarloNode
     * @return the action of this node
     */
    public A getAction()
    {
        return action;
    }

    /**
     * Get the cumulative rewards this MonteCarloNode has retrieved due to simulations on this MonteCarloNode or a child.
     * @return the reward value of this Node during the complete tree search
     */
    public int getqValues()
    {
        return qValues;
    }

    /**
     * Get the number of times this MonteCarloNode or a child has been visisted. A MonteCarloNode is visited
     * when the tree search expands and simulated on this MonteCarloNode or a child.
     * @return the number of times this MonteCarloNode has been visited
     */
    public int getVisits()
    {
        return visits;
    }

    /**
     * The following methods override the Iterator interface such that a for each loop can be used on
     * a object of this class to be able to loop over all expended children
     */
    @Override
    public Iterator<MonteCarloNode<S, A>> iterator()
    {
        return children.iterator();
    }

    @Override
    public void forEach(Consumer<? super MonteCarloNode<S, A>> consumer)
    {
        children.forEach(consumer);
    }

    @Override
    public Spliterator<MonteCarloNode<S, A>> spliterator()
    {
        return children.spliterator();
    }

    public String toString()
    {
        return String.format("visits: %d\nq: %d\nstate: %s\naction: %s",
                visits,
                qValues,
                getState().toString(),
                action.toString());
    }
}
