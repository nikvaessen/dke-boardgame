package nl.dke.boardgame.util;

import java.util.List;

/**
 *
 */
public interface Watchable
{
    /**
     *
     */
    void attachWatcher(Watcher watcher);

    /**
     *
     */
    void detachWatcher(Watcher watcher);

    /**
     *
     */
    void notifyWatchers();

}
