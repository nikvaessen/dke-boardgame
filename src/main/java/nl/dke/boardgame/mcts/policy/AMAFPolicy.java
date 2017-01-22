package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.MonteCarloNode;
import nl.dke.boardgame.mcts.MonteCarloRootNode;
import nl.dke.boardgame.mcts.State;
import nl.dke.boardgame.util.DoubleCounter;
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
        innerBackpropagate(node, reward, times);
        for(MonteCarloNode<S, A> child: node.getParent())
        {
            amafForwardPropegation(child, reward, times, node.getAction());
        }
    }

    private void innerBackpropagate(MonteCarloNode<S, A> node,  int reward, int times)
    {
        //visits += simPerNode;
        node.getAttachable(TOTAL_VISITS).increment(times);
        //qValues += q;
        node.getAttachable(TOTAL_REWARDS).increment(reward);

        if(!node.isRoot())
        {
            this.innerBackpropagate(node.getParent(), reward, times);
        }
    }

    private void amafForwardPropegation(MonteCarloNode<S, A> node, int reward, int times, A action)
    {
        if(node.getAction().equals(action))
        {
            node.getAttachable(TOTAL_AMAF_VISITS).increment(reward);
            node.getAttachable(TOTAL_AMAF_REWARD).increment(times);
        }
        else
        {
            for(MonteCarloNode<S, A> child : node)
            {
                amafForwardPropegation(child, reward, times, action);
            }
        }
    }

    @Override
    protected Function<MonteCarloNode<S, A>, Double> getNodeValueFunction()
    {
        return (MonteCarloNode<S, A> node) -> getNodeValue(node, getExplorationParameter(), bias);
    }

    public static double getNodeValue(MonteCarloNode node, double c, double bias)
    {
        double uct = getExplorationTerm(node, c);
        if(doubleIsInfinity(uct))
        {
            return Double.MAX_VALUE;
        }
        double b = getBetaValue(node, bias);
        double reward = getAverageReward(node);
        double amaf = getAMAFValue(node);
        if(doubleIsInfinity(b) || doubleIsInfinity(reward) || doubleIsInfinity(amaf))
        {
            return Double.MAX_VALUE;
        }
        double q = b * getAverageReward(node) + (1 -b) * getAMAFValue(node);
        return q + uct;
    }

    private static boolean doubleIsInfinity(double d)
    {
        return Math.abs(d - Double.MAX_VALUE) < 0.001d;
    }

    public static double getAverageReward(MonteCarloNode node)
    {
        double r = node.getAttachable(TOTAL_REWARDS).getValue().doubleValue();
        double v = node.getAttachable(TOTAL_VISITS).getValue().doubleValue();
        if(v == 0)
        {
            return Double.MAX_VALUE;
        }
        return r / v;
    }

    public static double getAMAFValue(MonteCarloNode node)
    {
        double r = node.getAttachable(TOTAL_AMAF_REWARD).getValue().doubleValue();
        double v = node.getAttachable(TOTAL_AMAF_VISITS).getValue().doubleValue();
        if(v == 0)
        {
            return 0;
        }
        return r / v;
    }

    public static double getBetaValue(MonteCarloNode node, double b)
    {
        //total normal visits (amounts of simulations)
        double n = node.getAttachable(TOTAL_VISITS).getValue().doubleValue();
        //total amaf visits
        double an = node.getAttachable(TOTAL_AMAF_VISITS).getValue().doubleValue();
        if(n == 0 && an == 0)
        {
            return 1;
        }
        return an / (n + an + 4 * n * an * Math.sqrt(b));
    }

}
