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

    public synchronized void negative()
    {
        reward--;
        count++;
    }

    public synchronized void positive()
    {
        reward++;
        count++;
    }

    public synchronized void neutral()
    {
        count++;
    }

    public synchronized int getReward()
    {
        return reward;
    }

    public synchronized int getCount()
    {
        return count;
    }

}
