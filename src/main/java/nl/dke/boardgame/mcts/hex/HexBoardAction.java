package nl.dke.boardgame.mcts.hex;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.Action;

/**
 * Created by nik on 07/01/17.
 */
public class HexBoardAction
        implements Action<HexBoardState>
{
    /**
     * The x and y locations of where a tile will be placed
     */
    private int x, y;

    /**
     * The player who will place the tile
     */
    private TileState player;

    /**
     * Construct an Action which can be applied on a HexState
     *
     * @param x      the row of the tile which will be selected
     * @param y      the column of the tile which will be selected
     * @param player the player who will claim a tile on the HexBoard
     */
    public HexBoardAction(int x, int y, TileState player)
    {
        if(player == TileState.NEUTRAL)
        {
            throw new IllegalArgumentException("player of a HexBoardAction cannot be " + player);
        }
        this.x = x;
        this.y = y;
        this.player = player;
    }

    /**
     * Apply the action to a given state, resulting in a new state
     *
     * @param before the state the action needs to be apllied on
     * @return the new state resulting for the action applied on the given state
     * @throws IllegalArgumentException when the action cannot be legally completed on the given state
     */
    @Override
    public HexBoardState apply(HexBoardState before) throws IllegalArgumentException
    {
        Board board = before.getBoard().clone();
        try
        {
            board.claim(x, y, player);
            return new HexBoardState(board, before.getOtherPlayer());
        } catch(AlreadyClaimedException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("Action cannot be applied on given board");
        }
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public TileState getPlayer()
    {
        return player;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof HexBoardAction)
        {
            HexBoardAction a = (HexBoardAction) o;
            return a.getPlayer() == player && a.getX() == x && a.getY() == y;
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {
        return String.format("x: %d y: %d player: %s", x, y, player);
    }

}
