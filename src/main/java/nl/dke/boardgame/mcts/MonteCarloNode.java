package nl.dke.boardgame.mcts;

import nl.dke.boardgame.mcts.policy.SimulationPolicy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by nik on 29/12/16.
 */
public class MonteCarloNode<S extends State, A extends Action<S> >
        implements Iterable<MonteCarloNode>
{
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
     * The parent of this node
     */
    private MonteCarloNode<S, A> parent;

    /**
     *
     * @param parent
     */
    public MonteCarloNode(MonteCarloNode<S, A> parent)
    {
        this.parent = parent;
    }

    /**
     * List containing all expanded children of this node
     */
    private LinkedList<MonteCarloNode> children = new LinkedList<>();

    /**
     * Get the State created by applying all actions from this Node
     * @return
     */
    public S getState()
    {
        return parent.getState().next(action);
    }

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

    public ArrayList<A> getActions(ArrayList<A> futureActions)
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
     * After simulation of a node, a reward is given based on the winning chances of the state.
     * Alternatively add this reward and the negation of the reward back up the tree until the root is found.
     * @param q the q-value to propagate back to the root of the tree
     */
    public void backPropagate(int q)
    {
        if(isRoot())
        {
            return;
        }
        visits++;
        qValues += q;
        parent.backPropagate(-q);
    }

    /**
     * returns whether this node is a root node
     * @return
     */
    public boolean isRoot()
    {
        return parent == null;
    }

    /**
     * The following methods override the Iterator interface such that a for each loop can be used on
     * a object of this class to be able to loop over all expended children
     */
    @Override
    public Iterator<MonteCarloNode> iterator()
    {
        return children.iterator();
    }

    @Override
    public void forEach(Consumer<? super MonteCarloNode> consumer)
    {
        children.forEach(consumer);
    }

    @Override
    public Spliterator<MonteCarloNode> spliterator()
    {
        return children.spliterator();
    }
}
