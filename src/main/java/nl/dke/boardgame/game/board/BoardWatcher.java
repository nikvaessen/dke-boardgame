package nl.dke.boardgame.game.board;

import nl.dke.boardgame.util.Watcher;

/**
 * Simple watcher which can tell you if the board has been changed since
 * the last time the method changed has been called
 */
public class BoardWatcher extends Watcher
{

    /**
     * boolean which will be toggled between true and false
     */
    private boolean changed = false;

    /**
     * Construct the BoardWatcher
     * @param board the board object to watch
     */
    public BoardWatcher(Board board)
    {
        super(board);
    }

    /**
     * Sets the boolean to true because the board will this method when it
     * changes itself
     */
    @Override
    public void update()
    {
        changed = true;
    }

    /**
     * Returns whether the board has been changed. If it has, it will set
     * the boolean back to false.
     * @return whether the board has been changed since the last
     * time the method was called
     */
    public boolean changed()
    {
        if(changed)
        {
            changed = false;
            return true;
        }
        return false;
    }
}
