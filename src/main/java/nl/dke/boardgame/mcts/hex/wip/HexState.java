package nl.dke.boardgame.mcts.hex.wip;

import nl.dke.boardgame.mcts.Action;
import nl.dke.boardgame.mcts.State;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by nik on 31/12/16.
 */
public class HexState implements State
{
    private enum player
    {
        one,
        two,
    }

    /**
     * The state is defined as a sequence of actions
     */
    private ArrayList<HexAction> state;

    /**
     * The width and height of the Hex Board this state is representing
     */
    private int width, height;

    /**
     * The number of the player who is currently allowed to make an action
     * Can only be 1 or 2
     */
    private int player;

    /**
     * Create the initial HexState, where no actions have taken place
     * @param width the width of the board of the HexState
     * @param height the height of the board of the HexState
     * @param player the number of the player who is currently allowed to make an action
     * @throws IllegalArgumentException when the given player number is not 1 or 2
     */
    public HexState(int width, int height, int player)
        throws IllegalArgumentException
    {
        if(!(player == 1 | player == 2))
        {
            throw new IllegalArgumentException("player number of a hextstate cannot be " + player);
        }
        this.width = width;
        this.height = height;
        this.player = player;
        state = new ArrayList<>();
    }

    /**
     * Create a HexState by giving it a sequence of actions
     * @param width the width of the board of the HexState
     * @param height the height of the board of the HexState
     * @param player the number of the player who is currently allowed to make an action
     * @param actions the actions to get to the current state
     */
    public HexState(int width, int height, int player, ArrayList<HexAction> actions)
        throws IllegalArgumentException
    {
        this(width, height, player);
        state.addAll(actions);
    }

    /**
     * Get a iterable for the list of actions
     * @return the iterator of the actions
     */
    public Iterator<HexAction> getActions()
    {
        return state.iterator();
    }

    /**
     * get how many actions have led to this state
     * @return the amount of actions
     */
    public int amountOfActions()
    {
        return state.size();
    }

    /**
     * returns whether the given action is already a part of this State
     * @param action the action to check
     * @return whether the given action was needed to reach this State
     */
    public boolean includes(HexAction action)
    {
        return state.contains(action);
    }

    /**
     * Gives the HexState s' which appears after the given action is applied to this state s
     * @param action The action to apply to this state
     * @return the next State
     * @throws IllegalArgumentException when the given action is not a HexAction or when it cannot be applied to
     * the given state
     */
    @Override
    public HexState next(Action action) throws IllegalArgumentException
    {
        if(!(action instanceof HexAction))
        {
            throw new IllegalArgumentException("A HexState cannot be applied to a action which is not a HexAction");
        }
        return ((HexAction) action).apply(this);
    }

    /**
     * Give a list of possible actions on this state. It will filter out actions on tiles which
     * are already claimed
     * @return a list of legal actions on the given state
     */
    @Override
    public List<HexAction> possibleActions()
    {
        List<HexAction> actions = new ArrayList<>();
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                actions.add(new HexAction(i, j, player));
            }
        }
        actions.removeAll(state);
        return actions;
    }

    /**
     * Returns whether the hex state does not have any more mores to make
     * @return
     */
    @Override
    public boolean isTerminal()
    {
        return possibleActions().size() == 0; //FIXME check for a win condition!!!
    }

    /**
     * Give an integer number representing who can currently act on the given state
     *
     * @return the next actor to be allowed to move
     */
    @Override
    public int nextActor()
    {
        return 0;
    }

    /**
     * Get the width of the Hex Board
     * @return the width of the Hex Board
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Get the height of the Hex Board
     * @return the height of the hex Board
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Get who is allowed to make an action in this state
     * @return the number of the player who is allowed to make an action
     */
    public int getPlayer()
    {
        return player;
    }




}
