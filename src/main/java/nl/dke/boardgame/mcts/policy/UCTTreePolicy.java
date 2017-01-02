package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.MonteCarloNode;
import nl.dke.boardgame.mcts.MonteCarloRootNode;
import nl.dke.boardgame.mcts.State;

/**
 * Created by nik on 02/01/17.
 */
public class UCTTreePolicy <S extends State, A extends Action<S> >
    implements TreePolicy<S, A>
{

    @Override
    public MonteCarloNode<S, A> choose(MonteCarloRootNode<S, A> root)
    {
        return null;
    }

    @Override
    public MonteCarloNode<S, A> expand(MonteCarloNode<S, A> node) throws IllegalArgumentException
    {
        return null;
    }

    @Override
    public MonteCarloNode<S, A> bestNode(MonteCarloRootNode<S, A> root)
    {
        return null;
    }
}
