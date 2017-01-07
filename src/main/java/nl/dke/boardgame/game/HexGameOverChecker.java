package nl.dke.boardgame.game;

import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.HexTile;
import nl.dke.boardgame.game.board.TileState;

/**
 * Created by nik on 07/01/17.
 */
public class HexGameOverChecker
{

    /**
     * Given a Board of hex, calculate who won (if any)
     * @param board the board to check on
     * @return 0 if nobody won yet, 1 if player 1 won, 2 if player 2 won
     */
    public static int isGameOver(Board board)
    {
        return checkWin(board);
    }

    /**
     * Checks if one of the players has won the game.
     * @return true if someone has won, false otherwise
     */
    private static int checkWin(Board board)
    {
        boolean[][] map = new boolean[board.getHeight()][board.getWidth()];

        //check if player1 has won
        int won = 0;
        for(int i = 0; i < board.getHeight() && won == 0 ; i++)
        {
            board.getNeighbours(i, 0);
            won = isPathToOtherSide(board, i, 0, map, TileState.PLAYER1);
        }

        //check if player2 has won
        if(won == 0)
        {
            for(int i = 0; i < board.getWidth() && won == 0; i++)
            {
                won = isPathToOtherSide(board, 0, i, map, TileState.PLAYER2);
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
    private static int isPathToOtherSide(Board board, int row, int column, boolean[][] map,
                                      TileState player)
    {
        if(map[row][column] || board.getState(row, column ) != player)
        {
            return 0;
        }
        else if(column == board.getWidth() - 1 && player == TileState.PLAYER1)
        {
            return 1;
        }
        else if(row == board.getHeight() - 1 && player == TileState.PLAYER2)
        {
            return 2;
        }
        else
        {
            //set that board location has been visited
            map[row][column] = true;

            //go to each neighbour which the same owner
            int won = 0;
            for(HexTile neighbourTile : board.getNeighbours(row, column))
            {
                if(neighbourTile.getState() == player && won == 0)
                {
                    won = isPathToOtherSide(board, neighbourTile.getRow(), neighbourTile.getColumn(), map, player);
                }
            }
            return won;
        }
    }


}
