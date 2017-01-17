package nl.dke.boardgame.mcts.hex;

import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.HexTile;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nik on 07/01/17.
 */
public class HexBoardState
        implements State
{
    /**
     * The board of the state
     */
    private Board board;

    /**
     * The player currently allowed to make a move on this board
     */
    private TileState player;

    /**
     * Create the initial State
     *
     * @param width  the width of the board
     * @param height the height of the board
     */
    public HexBoardState(int width, int height)
            throws IllegalArgumentException
    {
        board = new Board(width, height);
        player = TileState.PLAYER1;
    }

    public HexBoardState(Board board, TileState player)
    {
        this.board = board;
        this.player = player;
    }

    /**
     * Get the board of this state
     *
     * @return the board of this state
     */
    public Board getBoard()
    {
        return board;
    }

    public TileState getPlayer()
    {
        return player;
    }

    public TileState getOtherPlayer()
    {
        return player == TileState.PLAYER1 ? TileState.PLAYER2 : TileState.PLAYER1;
    }

    public static TileState getOtherPlayer(TileState player)
    {
        return player == TileState.PLAYER1 ? TileState.PLAYER2 : TileState.PLAYER1;
    }

    @Override
    public List<HexBoardAction> possibleActions()
    {
        List<HexBoardAction> actions = new ArrayList<>();
        for(int i = 0; i < board.getWidth(); i++)
        {
            for(int j = 0; j < board.getHeight(); j++)
            {
                if(board.getState(i, j) == TileState.NEUTRAL)
                {
                    actions.add(new HexBoardAction(i, j, player));
                }
            }
        }
        return actions;
    }

    @Override
    public boolean isTerminal()
    {
        return checkWin();
    }

    /**
     * Checks if one of the players has won the game.
     *
     * @return true if someone has won, false otherwise
     */
    private boolean checkWin()
    {
        boolean[][] map = new boolean[board.getHeight()][board.getWidth()];

        //check if player1 has won
        boolean won = false;
        for(int i = 0; i < board.getHeight() && !won; i++)
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
     *
     * @param row    the row where the algorithm currently is
     * @param column the column where the algorithm currently is
     * @param map    the map of the whole board, which stores which
     *               tiles have already been visited
     * @param player for which player the algorithm is currently checking
     *               for a path
     * @return if a path is found
     */
    private boolean isPathToOtherSide(int row, int column, boolean[][] map,
                                      TileState player)
    {
        if(map[row][column] || board.getState(row, column) != player)
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

    /**
     * Get the state resulting from the current state with the given action applied to it
     *
     * @param action The action to apply to this state
     * @return the state resulting from the given action applied to the current state
     * @throws IllegalArgumentException when the given action cannot be applied to the current state, either because
     *                                  it does not correspond to the given state or the action cannot legally be
     *                                  applied to that state
     */
    @Override
    public HexBoardState next(Action action) throws IllegalArgumentException
    {
        if(!(action instanceof HexBoardAction))
        {
            throw new IllegalArgumentException("Action to be applied to a Board is not a HexBoardAction");
        }
        return ((HexBoardAction) action).apply(this);
    }

    /**
     * Give an integer number representing who can currently act on the given state
     *
     * @return the next actor to be allowed to move
     */
    @Override
    public int nextActor()
    {
        return player == TileState.PLAYER1 ? 1 : 2;
    }

    @Override
    public boolean equals(Object o)
    {

        if(o instanceof HexBoardState)
        {
            return board.equals(((HexBoardState) o).getBoard());
        }
        return false;
    }

    public String toString()
    {
        return String.format("%s%nPlayer: %s%nOther Player: %s",
                board.toString(),
                getPlayer(),
                getOtherPlayer());
    }
}
