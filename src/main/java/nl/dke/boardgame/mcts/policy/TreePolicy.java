package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.*;

/**
 * A tree policy implements a method which help choose MonteCarloNodes in a MonteCarloTree which need simulating,
 * a method which creates the best child to expand for a given MonteCarloNode and a method
 * for choosing the best MonteCarloNode in a the whole Tree when the search terminates
 *
 * @author nik on 31/12/16.
 */
public interface TreePolicy<S extends State, A extends Action<S>>
{
    /**
     * Given a root node for a MonteCarlo Tree, choose the node which needs to be
     * simulated in the given iteration
     * @param root the root node of a MonteCarlo Tree
     * @return the MonteCarloNode which needs to be simulated
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
     * Given a node of a MonteCarlo Tree, select the best child
     * @param node the node of the MonteCarlo Tree to select a child on
     * @return The best node, which is a child of the given Node
     * @throws IllegalArgumentException when the given Node does not have children
     */
    MonteCarloNode<S, A> bestChild(MonteCarloNode<S, A> node) throws IllegalArgumentException;

    /**
     * This method selects the best node of the root after the tree search has terminated
     * @param root the root of the monte carlo tree
     * @return the best child of the root after MCTS is done
     */
    MonteCarloNode<S, A> bestRootChild(MonteCarloRootNode<S, A> root);

}
