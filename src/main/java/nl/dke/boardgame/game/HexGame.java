package nl.dke.boardgame.game;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.exceptions.MoveNotCompletedException;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.BoardWatcher;
import nl.dke.boardgame.game.board.HexTile;
import nl.dke.boardgame.game.board.TileState;

import java.util.List;

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
     * A editable delay between turn times
     */
    public static int DELAY_BETWEEN_TURNS = 100;

    /**
     * The board of HexTiles to play the game on
     */
    private Board board;

    /**
     * A player of the HexGame. Player 1 is allowed to move first
     */
    private HexPlayer player1;

    /**
     * The other player of the HexGame. Player 2 moves second
     */
    private HexPlayer player2;

    /**
     * Flag for the game start
     */
    private boolean started = false;

    /**
     * Flag for when the game is over
     */
    private boolean ended = false;

    /**
     * Construct a board for a game of Hex with the given board dimension
     * and the players
     *
     * @param width the width of the board
     * @param height the height of the board
     * @param player1 the player who moves first
     * @param player2 the player who moves second
     * @throws IllegalArgumentException when the given width and height are not
     * in between the bounds
     */
    public HexGame(int width, int height, HexPlayer player1, HexPlayer player2)
        throws IllegalArgumentException
    {
        //throws an IllegalArgumentException when not valid
        validateWidthAndHeight(width, height);

        //then create the board
        board = new Board(width, height);

        //and store the players
        this.player1 = player1;
        this.player2 = player2;
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
       if(isGameOver())
       {
           resetGame();
       }
       else
       {
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
        new Thread(new GameLoop()).start();
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

    /**
     * create a BoardWatcher which will get notified when the board changes
     * @return a BoardWatcher watching the Board of the game
     */
    public BoardWatcher getBoardWatcher()
    {
        return new BoardWatcher(board);
    }

    /**
     * This class runs in a separate Thread and controls player turn and
     * game over logic
     */
    private class GameLoop implements Runnable
    {

        private boolean player1Won = false;

        private boolean player2Won = false;

        @Override
        public void run()
        {
            long start = System.currentTimeMillis();
            allowMove(player1);
            long end = System.currentTimeMillis();
            System.out.println("Total game time: " + (end - start) + " ms");
        }

        /**
         * Recursive method which will let players make moves until the game
         * is over
         * @param player the player to currently make a move
         */
        //// TODO: 21/09/16 There needs to be a functionality that allows the second
        // to choose whether to switch positions with the first player after the
        // first player makes the first move.
        // this should also include changes in the Move class
        private void allowMove(HexPlayer player)
        {
            System.out.println("Turn: " + player.claimsAs().toString());
            //grace period
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            //check if game is over
            if(checkWin())
            {
                return;
            }

            //make the player make a move
            Move move = new Move(board, player.claimsAs());
            player.finishMove(move); //this method blocks until input has been given
            try
            {
                System.out.printf("Claiming row %d and column %d as %s%n",
                        move.getRow(), move.getColumn(),  player.claimsAs());
                board.claim(move.getRow(), move.getColumn(), player.claimsAs());
            }
            catch (MoveNotCompletedException | AlreadyClaimedException e)
            {
                e.printStackTrace();
                allowMove(player);
            }

            //and give the turn to the other player
            if(player == player1)
            {
                allowMove(player2);
            }
            else
            {
                allowMove(player1);
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
            //// TODO: 21/09/16 write an efficient loop/function to check for win

            boolean[][] map = new boolean[board.getHeight()][board.getWidth()];

            //check if player1 has won
            for(int i = 0; i < board.getHeight(); i++)
            {
                board.getNeighbours(i, 0);
                isPathToOtherSide(i, 0, map, TileState.PLAYER1);
            }

            //check if player2 has won
            if(!ended)
            {
                for(int i = 0; i < board.getWidth(); i++)
                {
                    isPathToOtherSide(0, i, map, TileState.PLAYER2);
                }
            }

            //if anyone one, make it be so
            if(player1Won || player2Won)
            {
                ended = true;
            }
            return ended;
        }

        /**
         *
         * @param row
         * @param column
         * @param map
         * @param player
         */
        private void isPathToOtherSide(int row, int column, boolean[][] map,
                                          TileState player)
        {
            if(map[row][column])
            {
               return;
            }
            else if(player == TileState.PLAYER1 && column == board.getWidth() - 1)
            {
                player1Won = true;
            }
            else if(player == TileState.PLAYER2 && row == board.getHeight() - 1)
            {
                player2Won = true;
            }
            else
            {
                //set that this location has been visited
                map[row][column] = true;

                //
                for(HexTile neighbourTile : board.getNeighbours(row, column))
                {
                    if(neighbourTile.getState() == player && (!player1Won || !player2Won) )
                    {
                        isPathToOtherSide(neighbourTile.getRow(),
                                neighbourTile.getColumn(), map, player);
                    }
                }
            }
        }

    }
}
