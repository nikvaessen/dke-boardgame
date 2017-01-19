package nl.dke.boardgame.mcts;

import nl.dke.boardgame.mcts.policy.SimulationPolicy;
import nl.dke.boardgame.mcts.policy.TreePolicy;
import nl.dke.boardgame.mcts.policy.UCTTreePolicy;
import nl.dke.boardgame.util.Attachable;
import nl.dke.boardgame.util.Counter;

import java.util.*;
import java.util.function.Consumer;

/**
 * This class implements a node in a MonteCarlo tree. It stores the amount of times it is visted, the total
 * reward it has retrieved over the whole tree search, and it represents a State in the MonteCarlo Tree.
 * The State is not stored due to memory constraints, but t is computed by every node storing the action,
 * and retrieving all actions and applying them to the initial state of the the root node.
 *
 * @author nik in 29/12/16.
 */
public class MonteCarloNode<S
        extends State, A extends Action<S>>
        implements Iterable<MonteCarloNode<S, A>>, Attachable<Counter>
{
    /**
     * Random number generator being shared on all nodes
     */
    private static Random rng = new Random(System.currentTimeMillis());

    /**
     * List containing all expanded children of this node
     */
    private LinkedList<MonteCarloNode<S, A>> children = new LinkedList<>();

    /**
     * List containing all attached counters on this node
     */
    private LinkedList<Counter> attached = new LinkedList<>();

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
     *
     * @param parent the parentof this node
     * @param action the action which leads to this node
     */
    public MonteCarloNode(MonteCarloNode<S, A> parent, A action)
    {
        this.parent = parent;
        this.action = action;
    }

    /**
     * Get the State created by applying all actions from this Node
     *
     * @return the state which this node is representing
     */
    public S getState()
    {
        return getRoot().getState().apply(getActions());
    }

    /**
     * Get an ArrayList of all the actions from this node back to the root Node
     *
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
     *
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
     *
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
     *
     * @return
     */
    public boolean isFullyExpanded()
    {
        return fullyExpanded;
    }

    /**
     * Set that this MonteCarloNode is fully expanded (has no more new potential children)
     *
     * @param fullyExpanded whether this node is fully expanded
     */
    public void setFullyExpanded(boolean fullyExpanded)
    {
        this.fullyExpanded = fullyExpanded;
    }

    /**
     * Simulate on the state of this MonteCarloNode and backpropagate the results of the simulation (e.g win/loss)
     * back to the root of the tree
     *
     * @param simulationPolicy the policy which is able to do a simulation on the State of this node
     */
    public int simulate(SimulationPolicy<S> simulationPolicy, int simPerNode)
    {
        int reward = simulationPolicy.simulate(getState(), simPerNode);
        if(getRoot().getState().nextActor() == getState().nextActor())
        {
            return reward;
        }
        else
        {
            return -reward;
        }
    }

    /**
     * get the root node of the tree
     * @return the root node of the tree of this node
     */
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
     * returns whether this node is a root node
     *
     * @return whether this is the root node of the tree
     */
    public boolean isRoot()
    {
        return parent == null;
    }

    /**
     * Get the parent node of this MonteCarloNode. Will be null if it's the root node
     *
     * @return the parent node or null if it's the root node
     */
    public MonteCarloNode<S, A> getParent()
    {
        return parent;
    }

    /**
     * Get the action which lead to State of this MonteCarloNode
     *
     * @return the action of this node
     */
    public A getAction()
    {
        return action;
    }

    /**
     * Attach a counter to this node to use for keeping track of scores for this node
     * Every counter is expected to have an unique name
     * @param counter a counter object to attach to this node
     */
    @Override
    public void attach(Counter counter)
        throws IllegalArgumentException
    {
        if(has(counter.toString()))
        {
            throw new IllegalArgumentException("already has a object with the unique identifier of" +
                    "the given object");
        }
        attached.add(counter);
    }

    /**
     * Detach a counter with a specific name from this object
     * @param name the unique name of the counter
     * @return true is succesfully deleted, false if not found
     */
    @Override
    public boolean detach(String name)
    {
        for(Iterator<Counter> iterable = attached.iterator(); iterable.hasNext(); /* not needed*/)
        {
            if(iterable.next().toString().equals(name))
            {
                iterable.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * returns true is there is an attached object with the given name
     * @param name the unique name of the object attached to a node
     * @return true is there is an object with the given name attached to this object,
     * false otherwise
     */
    @Override
    public boolean has(String name)
    {
        for(Iterator<Counter> iterable = attached.iterator(); iterable.hasNext(); /* not needed*/)
        {
            if(iterable.next().toString().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * get an attached object from this node
     * @param name the unique name of an object which is added to this node
     * @return the requested object
     * @throws IllegalArgumentException if there is no object with the given name added to this node
     */
    @Override
    public Counter getAttachable(String name)
        throws IllegalArgumentException
    {
        Counter temp;
        for(Counter c : attached)
        {
            if(c.toString().equals(name))
            {
                return c;
            }
        }
        throw new IllegalArgumentException("given unique name of object was not found in the list " +
                "of attached objects");
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
        return String.format("visits: %d\nq: %d\nUCT 0.0: %f\nUTC 0.5: %f\nUTC 3.0: %f\nstate:\n%s\naction: %s",
                getAttachable(UCTTreePolicy.TOTAL_REWARDS).getValue().intValue(),
                getAttachable(UCTTreePolicy.TOTAL_REWARDS).getValue().intValue(),
                UCTTreePolicy.getUCTValue(this, 0),
                UCTTreePolicy.getUCTValue(this, 0.5),
                UCTTreePolicy.getUCTValue(this, 3),
                getState().toString(),
                action.toString());
    }

    public void setParent(MonteCarloNode parent) {
        this.parent = parent;
    }

    /**
     * return the amount of children of this node
     * @return the amount of children
     */
    public int amountOfChildren()
    {
        return children.size();
    }

    /**
     * add a child to this node
     * @param child the child to add
     */
    public void addChild(MonteCarloNode<S, A> child)
    {
        children.add(child);
    }
}
