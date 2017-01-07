package nl.dke.boardgame.mcts.hex;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.HexTile;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;

import java.util.Collections;
import java.util.List;

/**
 * Created by nik on 01/01/17.
 */
public class HexBoardSimulation
        implements SimulationPolicy<HexBoardState>
{
    private Board board;
    private boolean player1won;
    private boolean player2won;

    @Override
    public int simulate(HexBoardState state)
    {
        // reset variables
        this.board = state.getBoard().clone();
        this.player1won = false;
        this.player2won = false;
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
        }

        //see who is the winner and return the reward
        checkWin();
        TileState winner;
        if (player1won)
        {
            winner = TileState.PLAYER1;
        }
        else
        {
            winner = TileState.PLAYER2;
        }
        if (winner == currentPlayer)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    /**
     * Checks if one of the players has won the game.
     * @return true if someone has won, false otherwise
     */
    private boolean checkWin()
    {
        boolean[][] map = new boolean[board.getHeight()][board.getWidth()];

        //check if player1 has won
        boolean won = false;
        for(int i = 0; i < board.getHeight() && !won ; i++)
        {
            board.getNeighbours(i, 0);
            won = isPathToOtherSide(i, 0, map, TileState.PLAYER1);
        }

        //check if player2 has won
        if(!won)
        {
            for(int i = 0; i < board.getWidth() && !won; i++)
            {
                won = isPathToOtherSide(0, i, map, TileState.PLAYER2);
            }
        }

        return won;
    }

    /**
     * looks for a path from one side of the board to other by visiting a
     * tile on a specific tile and row and going to each neighbour until
     * it has reached the other side.
     * @param row the row where the algorithm currently is
     * @param column the column where the algorithm currently is
     * @param map the map of the whole board, which stores which
     *            tiles have already been visited
     * @param player for which player the algorithm is currently checking
     *               for a path
     * @return if a path is found
     */
    private boolean isPathToOtherSide(int row, int column, boolean[][] map,
                                      TileState player)
    {
        if(map[row][column] || board.getState(row, column ) != player)
        {
            return false;
        }
        else if(column == board.getWidth() - 1 && player == TileState.PLAYER1)
        {
            return true;
        }
        else if(row == board.getHeight() - 1 && player == TileState.PLAYER2)
        {
            return true;
        }
        else
        {
            //set that board location has been visited
            map[row][column] = true;

            //go to each neighbour which the same owner
            boolean won = false;
            for(HexTile neighbourTile : board.getNeighbours(row, column))
            {
                if(neighbourTile.getState() == player && !won)
                {
                    won = isPathToOtherSide(neighbourTile.getRow(), neighbourTile.getColumn(), map, player);
                }
            }
            return won;
        }
    }

}
