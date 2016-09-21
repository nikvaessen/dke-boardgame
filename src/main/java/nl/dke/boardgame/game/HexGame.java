package nl.dke.boardgame.game;

import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;

/**
 * This class has all the functionality to play a game of Hex, either with 2
 * players, 1 player against an AI or 2 AIs against each other
 */
public class HexGame
{
    /**
     * The width and height of the board when the constructor is not given any
     * arguments
     */
    public final static int DEFAULT_BOARD_DIMENSION = 11;

    /**
     * The minimum width and height of the board which are allowed to be given
     * as an argument to the constructor
     */
    public final static int MINIMUM_BOARD_DIMENSION = 9;

    /**
     * The maximum width and height of the board which are allowed to be given
     * as an argument to the constructor
     */
    public final static int MAXIMUM_BOARD_DIMENSION = 19;

    /**
     * The board of HexTiles to play the game on
     */
    private Board board;

    /**
     * A player of the HexGame. Player 1 is allowed to move first
     */
    private Player player1;

    /**
     * The other player of the HexGame. Player 2 moves second
     */
    private Player player2;

    /**
     * Flag for the game start
     */
    private boolean started = false;

    /**
     * Flag for when the game is over
     */
    private boolean ended = false;

    /**
     * Construct a board for a game of Hex with the default board dimension
     */
    public HexGame()
    {
        board = new Board(DEFAULT_BOARD_DIMENSION, DEFAULT_BOARD_DIMENSION);
    }

    /**
     * Construct a board for a game of Hex with the given board dimension
     * @param width the width of the board
     * @param height the height of the board
     */
    public HexGame(int width, int height)
        throws IllegalArgumentException
    {
        //throws an IllegalArgumentException when not valid
        validateWidthAndHeight(width, height);

        //then create the board
        board = new Board(width, height);
    }

    /**
     * Verifies that the given width and height are equal and between the
     * allowed range of board creation
     * @param width the width to validate
     * @param height the height to validate
     * @throws IllegalArgumentException when the dimensions are now equal or
     * not in the allowed range
     */
    private void validateWidthAndHeight(int width, int height)
        throws IllegalArgumentException
    {
        if(width != height)
        {
            throw new IllegalArgumentException(String.format(
                    "given width %d and height %d are not equal"
            ));
        }
        if(width < MINIMUM_BOARD_DIMENSION || width > MAXIMUM_BOARD_DIMENSION)
        {
            throw new IllegalArgumentException(String.format(
                    "given width %d and height %d are not in between allowed " +
                    "minimum value %d and maximum value %d",
                    width, height,
                    MINIMUM_BOARD_DIMENSION, MAXIMUM_BOARD_DIMENSION));
        }
    }

    /**
     * Ask to starts the game by allowing player 1 to make a move
     * @throws IllegalStateException when the game has already been started
     */
    public void start()
        throws IllegalStateException
    {
        if(!started)
        {
            startGame();
        }
        else
        {
            throw new IllegalStateException("The HexGame cannot be started" +
                    "because it has already been started in the past. " +
                    "Try to reset the game first");
        }
    }

    /**
     * check if the game has been completed or not
     * @return true of game is over, false if not
     */
    public boolean isGameOver()
    {
        return ended;
    }

    /**
     * Ask to reset the game. This can only be done when the game has been
     * completed
     * @throws IllegalStateException when the game has not been completed
     */
    public void reset()
        throws IllegalStateException
    {
       if(isGameOver()){
           resetGame();
       }
       else{
           throw new IllegalStateException("The HexGame cannot be reset" +
                   " because it has not yet been completed");
       }
    }

    /**
     * Starts the game by allowing player 1 to make a move
     */
    private void startGame()
    {
        started = true;
    }

    /**
     * Reset the game so it is possible to play it again
     */
    private void resetGame()
    {
        started = false;
        ended = false;
        board.resetTiles();
    }

    //// TODO: 21/09/16 document
    private void allowMove(Player player)
    {
        if(checkWin())
        {
            return;
        }
        else
        {
             //Move move = new Move(board.c, TileState.PLAYER1);
        }
    }

    /**
     * Checks if one of the players has won the game.
     * If someone has, it will set the boolean flag ended to true, so that
     * the isGameOver() method will return true as well
     * @return true if someone has won, false otherwise
     */
    private boolean checkWin()
    {
        return false;
    }
}
