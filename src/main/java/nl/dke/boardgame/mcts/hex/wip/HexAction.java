package nl.dke.boardgame.mcts.hex.wip;

import nl.dke.boardgame.mcts.Action;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by nik on 31/12/16.
 */
public class HexAction implements Action<HexState>
{
    /**
     * The x and y locations of where a tile will be placed
     */
    private int x, y;

    /**
     * The player who will place the tile
     */
    private int player;

    /**
     * Construct an Action which can be applied on a HexState
     * @param x the row of the tile which will be selected
     * @param y the column of the tile which will be selected
     * @param player the player who will claim a tile on the HexBoard
     */
    public HexAction(int x, int y, int player)
    {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    /**
     *
     * @param before the state the action needs to be applied on
     * @return the new HexState following from this action
     * @throws IllegalArgumentException when the action cannot be applied on the given HexState
     */
    @Override
    public HexState apply(HexState before)
            throws IllegalArgumentException
    {
        if(before.includes(this))
        {
            throw new IllegalArgumentException(String.format("Action cannot be applied to given state. " +
                            "Action: x: %d y: %d. State has these x and y", x, y));
        }
        if(x > before.getWidth() - 1 || y > before.getHeight() - 1)
        {
            throw new IllegalArgumentException("cannot apply given action on board because x and y of HexAction" +
                    "are larger than the dimension of the board");
        }
        ArrayList<HexAction> after = new ArrayList<>(before.amountOfActions() + 1);
        Iterator<HexAction> iterator = before.getActions();
        while(iterator.hasNext())
        {
            //don't check for sameness as this action as that should not happen due to only
            //possible actions being selected
            after.add(iterator.next());
        }
        after.add(this);
        return new HexState(before.getWidth(), before.getHeight(), before.getPlayer() == 1 ? 2 : 1, after);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getPlayer()
    {
        return player;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof HexAction)
        {
            HexAction a = (HexAction) o;
            return a.getPlayer() == player && a.getX() == x && a.getY() == y;
        }
        else
        {
            return false;
        }
    }
}
