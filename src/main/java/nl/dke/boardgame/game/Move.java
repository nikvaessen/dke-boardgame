package nl.dke.boardgame.game;

import nl.dke.boardgame.exceptions.MoveNotCompletedException;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;

/**
 * Encapsulated a move a Player can make in a Hex game
 */
public class Move
{
    /**
     * If the move is made by either player 1 or player 2 (NEUTRAL is forbidden)
     */
    private TileState player;

    /**
     * The board the move will be played on. It's presumed that the board
     * is a copy of the real board so changing a tile in this board
     * will not affect the game
     */
    private Board board;

    /**
     * In which row a tile will be claimed
     */
    private int row = -1;

    /**
     * In which column a tile will be claimed
     */
    private int column = -1;

    /**
     * Create a move on a board for a player to make
     * @param board the board to decide on which move to make
     * @param player which player needs to make a move
     */
    public Move(Board board, TileState player)
        throws IllegalArgumentException
    {
        if(player == TileState.NEUTRAL)
        {
            throw new IllegalArgumentException("Neutral cannot make moves");
        }
        this.player = player;
        this.board = board.clone();
    }

    /**
     * Gets which player is making the move
     * @return player1 or player2
     */
    public TileState getPlayer()
    {
        return player;
    }

    /**
     * Gets the board-state to make the move on. The move should not be
     * made on this board, but passed along in this instance of the class
     * @return the board to decide the move on
     */
    public Board getBoard()
    {
        return board;
    }

    /**
     * Sets on which row a tile needs to be claimed
     * @param row the row on which the tile to claim is on
     * @throws IllegalArgumentException if the specified row does not exist
     */
    public void setRow(int row)
            throws IllegalArgumentException
    {
        if(board.canAccessRow(row))
        {
            this.row = row;
        }
        else
        {
            throw new IllegalArgumentException(String.format(
                    "given row %d is outside of the row dimension", row
            ));
        }
    }

    /**
     * Gets on which row a tile needs to be claimed
     * @return the row on which the tile to claim is on
     * @throws MoveNotCompletedException when the row has not been set
     */
    public int getRow()
            throws MoveNotCompletedException
    {
        if(row == -1)
        {
            throw new MoveNotCompletedException("row was not set");
        }
        return row;
    }

    /**
     * Sets on which column a tile needs to be claimed
     * @param column the column on which the tile to claim is on
     */
    public void setColumn(int column)
        throws IllegalArgumentException
    {
        if(board.canAccessColumn(column))
        {
            this.column = column;
        }
        else
        {
            throw new IllegalArgumentException(String.format(
                    "given column %d is outside of the column dimension", column
            ));
        }
    }

    /**
     * Checks whether the given row and column are a valid move
     * @return true if it is a valid move, false otherwise
     */
    public boolean verify()
    {
        if(column == -1 && row == -1)
        {
            return false;
        }
        if(board.getState(row, column) != TileState.NEUTRAL)
        {
            return false;
        }
        return true;
    }

    /**
     * Gets on which column a tile needs to be claimed
     * @return the column on which the tile to claim is on
     * @throws MoveNotCompletedException when the column has not been set
     */
    public int getColumn()
            throws MoveNotCompletedException
    {
        if(column == -1)
        {
            throw new MoveNotCompletedException("Column was not set");
        }
        return column;
    }

    /**
     * Checks whether the row and column have been set
     * @return true if both have been set, false otherwise
     */
    public boolean isSet()
    {
        return column > -1 && row > -1;
    }
}
