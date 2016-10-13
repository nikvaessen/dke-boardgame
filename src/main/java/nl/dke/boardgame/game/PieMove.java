package nl.dke.boardgame.game;

import nl.dke.boardgame.exceptions.MoveNotCompletedException;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;

/**
 * A pie move is where player 1 has completed his first turn and player 2 can
 * decide to take the position of the first player's tile or take a new tile
 */
public class PieMove extends Move
{

    public PieMove(Board board, TileState player)
            throws IllegalStateException
    {
        super(board, player);
        if(!hasCorrectBoardState(board))
        {
            throw new IllegalArgumentException("Board is not in the pie" +
                    "rule state!");
        }
    }

    /**
     * Checks whether the given row and column are a valid move
     * @return true if it is a valid move, false otherwise
     */
    @Override
    public boolean verify()
    {
        try
        {
            getColumn(); getRow();
            return true;
        }
        catch (MoveNotCompletedException e)
        {
            return false;
        }

    }

    /**
     * Checks if the Board state is legal such that it is a Pie move
     * @param board the board to check
     * @return whether the pie rule applies to this board
     */
    private boolean hasCorrectBoardState(Board board)
    {
        int player1Tiles = 0;
        int player2Tiles = 0;
        for(int i = 0; i < board.getHeight(); i++)
        {
            for(int j = 0; j < board.getWidth(); j++)
            {
                switch (board.getState(i, j))
                {
                    case PLAYER1:
                        player1Tiles++;
                        break;
                    case PLAYER2:
                        player2Tiles++;
                        break;
                }
            }
        }
        return player2Tiles == 0 && player1Tiles == 1;
    }
}
