package nl.dke.boardgame.util;

import java.util.List;

/**
 * A watchable class will store every Watcher which "subscribes" itself to
 * it and when the watchable class changes, it will update every watcher
 * so they know the watchable changed, and can take action accordingly
 */
public interface Watchable
{
    /**
     * Add the watcher to the list of all watchers
     */
    void attachWatcher(Watcher watcher);

    /**
     * Remove the watcher from the list of all watchers
     */
    void detachWatcher(Watcher watcher);

    /**
     * Notify all watchers in the list by calling their update method
     */
    void notifyWatchers();

}
