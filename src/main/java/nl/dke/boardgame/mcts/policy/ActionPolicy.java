package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.MonteCarloNode;
import nl.dke.boardgame.mcts.MonteCarloRootNode;
import nl.dke.boardgame.mcts.State;

/**
 * Created by nik on 01/01/17.
 */
public interface ActionPolicy<S extends State, A extends Action<S>>
{
    A select(MonteCarloRootNode<S, A> rootNode);
}

