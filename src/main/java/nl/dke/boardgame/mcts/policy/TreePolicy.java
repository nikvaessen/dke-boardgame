package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.MonteCarloNode;
import nl.dke.boardgame.mcts.MonteCarloRootNode;
import nl.dke.boardgame.mcts.State;

/**
 * A tree policy implements a method which help choose MonteCarloNodes in a MonteCarloTree which need expanding,
 * a method which creates the best child to expand for a given MonteCarloNode and a method
 * for choosing the best MonteCarloNode in a the whole Tree when the search terminates
 *
 * @author nik on 31/12/16.
 */
public interface TreePolicy<S extends State, A extends Action<S>>
{
    /**
     * Given a root node for a MonteCarlo Tree, choose the node which needs to be
     * expanded in the current iteration
     * @param root the root node of a MonteCarlo Tree
     * @return the MonteCarloNode which needs to be expanded
     */
    MonteCarloNode<S, A> choose(MonteCarloRootNode<S, A> root);

    /**
     * Expand a Node by giving it a new child according to some rule determining the best child to create
     * @param node the MonteCarloNode to expand
     * @return a new MonteCarloNode which is a child node of the given MonteCarloNode
     * @throws IllegalArgumentException when the given node cannot be expanded
     */
    MonteCarloNode<S, A> expand(MonteCarloNode<S, A> node) throws IllegalArgumentException;

    /**
     * Given a root node of a MonteCarlo Tree, select the best action with the best Node
     * @param root the root node of the MonteCarlo Tree
     * @return the Node holding the best action
     */
    MonteCarloNode<S, A> bestNode(MonteCarloRootNode<S, A> root);

}
