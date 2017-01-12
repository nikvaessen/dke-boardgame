package nl.dke.boardgame.mcts.mutliThread;

/**
 * Created by nik on 12/01/17.
 */
public class RewardTracker
{
    private int reward;
    private int count;

    public RewardTracker()
    {
        reward = 0;
        count = 0;
    }

    public void negative()
    {
        reward--;
        count++;
    }

    public void positive()
    {
        reward++;
        count++;
    }

    public void neutral()
    {
        count++;
    }

    public int getReward()
    {
        return reward;
    }

    public int getCount()
    {
        return count;
    }

}
