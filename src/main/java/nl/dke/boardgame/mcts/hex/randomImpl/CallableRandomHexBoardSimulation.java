package nl.dke.boardgame.mcts.hex.randomImpl;

import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.mcts.hex.RandomHexBoardSimulation;
import nl.dke.boardgame.mcts.mutliThread.CallableSimulations;

/**
 * Created by nik on 1/19/17.
 */
public class CallableRandomHexBoardSimulation
    extends CallableSimulations<HexBoardState>
{

    public CallableRandomHexBoardSimulation(HexBoardState stateToSimulate)
    {
        super(stateToSimulate);
    }


    @Override
    public int simulate(HexBoardState state, int times)
    {
        int reward = 0;
        for(int i = 0; i < times; i++)
        {
            reward += RandomHexBoardSimulation.simulate(state);
        }
        return reward;
    }
}
