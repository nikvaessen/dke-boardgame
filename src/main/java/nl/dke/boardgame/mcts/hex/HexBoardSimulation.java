package nl.dke.boardgame.mcts.hex;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.HexGameOverChecker;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.HexTile;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by nik on 01/01/17.
 */
public class HexBoardSimulation
        implements SimulationPolicy<HexBoardState>
{

    @Override
    public int simulate(HexBoardState state)
    {
        // reset variables
        Board board = state.getBoard().clone();
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
            }
            catch (AlreadyClaimedException e)
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

        System.out.println("Simulated board:\n" + board);
        System.out.println("Current Player = " + currentPlayer.toString());
        System.out.println("Winner Player = " + winner.toString());

        if (winner == currentPlayer)
        {

            System.out.println("reward: " + 1);
            return 1;
        }
        else
        {
            System.out.println("reward: " + -1);
            return -1;
        }
    }

}
