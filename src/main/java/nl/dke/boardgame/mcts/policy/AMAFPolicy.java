package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.MonteCarloNode;
import nl.dke.boardgame.mcts.MonteCarloRootNode;
import nl.dke.boardgame.mcts.State;
import nl.dke.boardgame.util.IntegerCounter;

import java.util.List;
import java.util.function.Function;

/**
 * Created by nik on 1/16/17.
 */
public class AMAFPolicy <S extends State, A extends Action<S>>
        extends UCTTreePolicy<S, A>
{
    public static final String TOTAL_AMAF_VISITS = "ac";
    public static final String TOTAL_AMAF_REWARD = "av";

    /**
     * The rave bias used in calculating the beta value
     */
    public double bias;

    public AMAFPolicy(double c, double b)
    {
        super(c);
    }

    public AMAFPolicy(double b)
    {
        super(0);
    }

    @Override
    protected void attachCounters(MonteCarloNode<S, A> node)
    {
        super.attachCounters(node);
        node.attach(new IntegerCounter(TOTAL_AMAF_VISITS));
        node.attach(new IntegerCounter(TOTAL_AMAF_REWARD));
    }

    @Override
    public void backpropagate(MonteCarloNode<S, A> node, int reward)
    {
        this.backpropagate(node, reward, 1);
    }

    @Override
    public void backpropagate(MonteCarloNode<S, A> node, int reward, int times)
    {
        this.backpropagate(node, reward, times, node.getState().nextActor());
    }

    private void backpropagate(MonteCarloNode<S, A> node, int reward, int times, int actor)
    {
        //visits += amount of simulations done;
        node.getAttachable(TOTAL_VISITS).increment(times);
        //qValues += q;
        node.getAttachable(TOTAL_REWARDS).increment(reward);

        //back propegate the amaf count and reward
        amafBackPropegation(node, reward, times, actor);

        if(!node.isRoot())
        {
            this.backpropagate(node.getParent(), reward, times, node.getState().nextActor());
        }
    }

    private void amafBackPropegation(MonteCarloNode<S, A> node, int reward, int times, int actor)
    {
        if(node.getState().nextActor() == actor)
        {
            //amaf count  += amount of simulations done
            node.getAttachable(TOTAL_AMAF_VISITS).increment(times);
            //amaf reward =+ q
            node.getAttachable(TOTAL_AMAF_REWARD).increment(reward);
         }
        //repeat this until root
        if(!node.isRoot())
        {
            amafBackPropegation(node.getParent(), reward, times, actor);
        }
    }

    @Override
    protected Function<MonteCarloNode<S, A>, Double> getNodeValueFunction()
    {
        return new Function<MonteCarloNode<S, A>, Double>()
        {
            @Override
            public Double apply(MonteCarloNode<S, A> node)
            {
                double uct = getExplorationTerm(node, getExplorationParameter());
                if(uct - Double.MAX_VALUE < 0.001d)
                {
                    return Double.MAX_VALUE;
                }
                double b = getBetaValue(node, bias);
                double q = b * getAverageReward(node) + (1 -b) * getAMAFValue(node);
                return q + uct;
            }
        };
    }

    public static double getAverageReward(MonteCarloNode node)
    {
        return node.getAttachable(TOTAL_REWARDS).getValue().doubleValue() /
                node.getAttachable(TOTAL_VISITS).getValue().doubleValue();
    }

    public static double getAMAFValue(MonteCarloNode node)
    {
        return node.getAttachable(TOTAL_AMAF_REWARD).getValue().doubleValue() /
                node.getAttachable(TOTAL_AMAF_VISITS).getValue().doubleValue();
    }

    public static double getBetaValue(MonteCarloNode node, double b)
    {
        //total normal visits (amounts of simulations)
        double n = node.getAttachable(TOTAL_VISITS).getValue().doubleValue();
        //total amaf visits
        double an = node.getAttachable(TOTAL_AMAF_VISITS).getValue().doubleValue();

        return an / (n + an + 4 * n * an * Math.sqrt(b));
    }

}
