package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.MonteCarloNode;
import nl.dke.boardgame.mcts.MonteCarloRootNode;
import nl.dke.boardgame.mcts.State;

/**
 * Created by nik on 31/12/16.
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

}
