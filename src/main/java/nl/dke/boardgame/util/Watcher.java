package nl.dke.boardgame.util;

/**
 *
 */
public abstract class Watcher
{
    /**
     * The object which is being watched
     */
    private Watchable watching;

    /**
     * Construct the Watcher by saving who it is watching and attaching
     * itself ot it
     * @param watching the object to watch
     */
    public Watcher(Watchable watching)
    {
        this.watching = watching;
        watching.attachWatcher(this);
    }

    /**
     * Gets called by the watchable when something happens. This
     * method should do something with this information
     */
    public void update()
    {

    }
}
