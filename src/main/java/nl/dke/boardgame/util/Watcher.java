package nl.dke.boardgame.util;

/**
 * A watcher will subscribe itself to a watchable, and the watchable will
 * call the update() method on the watcher whenever it is deemed necessary
 */
public interface Watcher
{
    /**
     * Gets called by the watchable when something happens. This
     * method should do something with this information
     */
    void update();
}
