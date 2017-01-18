package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.MonteCarloNode;
import nl.dke.boardgame.mcts.MonteCarloRootNode;
import nl.dke.boardgame.mcts.State;

import java.util.List;
import java.util.function.Function;

/**
 * Created by nik on 1/16/17.
 */
public class AMAFPolicy <S extends State, A extends Action<S>>
        extends UCTTreePolicy<S, A>
{

    public AMAFPolicy(double c)
    {
        super(c);
    }

    public AMAFPolicy()
    {
        super(0);
    }

    @Override
    protected Function<MonteCarloNode<S, A>, Double> getNodeValueFunction()
    {
        return new Function<MonteCarloNode<S, A>, Double>()
        {
            @Override
            public Double apply(MonteCarloNode<S, A> node)
            {
                double b = getBetaValue(node);
                return b * getUCTValue(node, getExplorationParameter()) + (1 -b) * getAMAFValue(node);
            }
        };
    }

    public static double getAMAFValue(MonteCarloNode node)
    {
        return 0;
    }

    public static double getBetaValue(MonteCarloNode node)
    {
        return 0;
    }

}
