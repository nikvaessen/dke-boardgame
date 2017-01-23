package nl.dke.boardgame.mcts.hex.randomImpl;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.HexGameOverChecker;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.MonteCarloTree;
import nl.dke.boardgame.mcts.hex.HexBoardAction;
import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.mcts.mutliThread.RewardTracker;
import nl.dke.boardgame.mcts.mutliThread.RunnableSimulation;

import java.util.Collections;
import java.util.List;

/**
 * Created by nik on 12/01/17.
 */
public class RunnableRandomHexBoardSimulation
        extends RunnableSimulation<HexBoardState>
{

    public RunnableRandomHexBoardSimulation(HexBoardState state, RewardTracker tracker)
    {
        super(state, tracker);
    }

    /**
     * apply simulation on a given state to determine the reward
     *
     * @param state the state to use simulation on
     * @param times the amount of simulations being done
     * @return the sum of the rewards of the simulation
     */
    @Override
    public int simulate(HexBoardState state, int times)
    {
        if(times > 1 || times <= 0)
        {
            throw new IllegalArgumentException("runnable simulation can only simulate once");
        }
        return simulate(state);
    }

    private int simulate(HexBoardState state)
    {
        // reset variables
        Board board = state.getBoard().clone(false);
        TileState currentPlayer = state.getPlayer();

        //get list of possible actions and apply them on the board in a random order
        List<HexBoardAction> actions = state.possibleActions();

        Collections.shuffle(actions);
        TileState player = currentPlayer;
        for(HexBoardAction action : actions)
        {
            try
            {
                board.claim(action.getX(), action.getY(), player);
                player = HexBoardState.getOtherPlayer(player);
            } catch(AlreadyClaimedException e)
            {
                e.printStackTrace();
            }
//            System.out.println("FORLOOP BOARD \n" + board);
//            System.out.println("ACTIONS FORLOOP = "+ actions);
//            System.out.println("Action picked FORLOOP = "+ action);
        }
        //see who is the winner and return the reward
        int wonPlayer = HexGameOverChecker.isGameOver(board);
        TileState winner = wonPlayer == 1 ? TileState.PLAYER1 : TileState.PLAYER2;

        int reward;
        if(winner == currentPlayer)
        {
            reward = 1;
        }
        else
        {
            reward = -1;
        }
        if(MonteCarloTree.DEEP_DEBUG)
        {
            System.out.println("Simulated board:\n" + board);
            System.out.println("Current Player = " + currentPlayer.toString());
            System.out.println("Winner Player = " + winner.toString());
            System.out.println("reward: " + reward);
        }
        return reward;
    }
}
