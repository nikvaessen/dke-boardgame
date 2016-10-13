package nl.dke.boardgame.game;

import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.players.PossiblePlayers;
import nl.dke.boardgame.util.Watchable;
import nl.dke.boardgame.util.Watcher;

import java.util.ArrayList;
import java.util.List;

/**
 *  Holds all information about a HexGame
 */
public class GameState implements Watchable
{
    /**
     * All watchers, who need to be notified when a value changes
     */
    private List<Watcher> watchers;

    /**
     * The type of player (e.g human, random) of player 1
     */
    private PossiblePlayers player1;

    /**
     * The type of player (e.g human, random) of player 2
     */
    private PossiblePlayers player2;

    /**
     * Who is currently allowed to make a move
     */
    private TileState currentTurn;

    /**
     * The amount of turns player 1 had
     */
    private int player1Turns;

    /**
     * the amount of turns player 2 had
     */
    private int player2Turns;

    /**
     * Who won the game. Null when the game is still busy
     */
    private TileState winner;

    /**
     * if the pie rule is enabled
     */
    private boolean pieRule = false;

    /**
     * Holds a list of all board states from the beginning of the game until
     * the end
     */
    private List<TileState[][]> boardStateHistory;

    /**
     * Create a GameState for a perticuliar game
     * @param player1 the type of player for player 1
     * @param player2 the type of player for player 2
     */
    public GameState(TileState[][] initialBoard, PossiblePlayers player1, PossiblePlayers player2)
    {
        watchers = new ArrayList<>();
        this.player1 = player1;
        this.player2 = player2;

        boardStateHistory = new ArrayList<>();
        boardStateHistory.add(initialBoard);
        currentTurn = TileState.PLAYER1;
    }

    /**
     * Complete a turn
     * @param newBoard the new board due to the completion of a turn
     * @param completedByPlayer the player who completed the turn
     * @throws IllegalStateException when the game is already over
     * @throws IllegalArgumentException when the player who completed the turn
     * is not player1 or player2
     */
    public synchronized void completedTurn(TileState[][] newBoard,
                                           TileState completedByPlayer)
        throws IllegalStateException, IllegalArgumentException
    {
        if(hasWinner())
        {
            throw new IllegalStateException("Game is over, cannot complete" +
                    " another turn");
        }
        if(completedByPlayer != TileState.PLAYER1 &&
                completedByPlayer != TileState.PLAYER2)
        {
            throw new IllegalArgumentException("Turn can only be completed" +
                    " by player1 or player2");
        }

        boardStateHistory.add(newBoard);
        if(completedByPlayer == TileState.PLAYER1)
        {
            player1Turns++;
            currentTurn = TileState.PLAYER2;
        }
        else
        {
            player2Turns++;
            currentTurn = TileState.PLAYER1;
        }
        notifyWatchers();
    }

    /**
     * Set that a player has won the game
     * @param player the player who won the game
     * @throws IllegalStateException when the game is already won
     * @throws IllegalArgumentException when the player who won the game is not
     * player1 or player2
     */
    public synchronized void playerWon(TileState player)
        throws IllegalStateException, IllegalArgumentException
    {
        if(hasWinner())
        {
            throw new IllegalStateException("Game already has a winner");
        }
        if(!(player == TileState.PLAYER1 || player == TileState.PLAYER2))
        {
            throw new IllegalArgumentException("Game can only be won by " +
                    "player1 or player2");
        }
        winner = player;
        notifyWatchers();
    }

    /**
     * get the kind of player player1 is
     * @return the type of player1
     */
    public synchronized PossiblePlayers player1is()
    {
        return player1;
    }

    /**
     * get the kind of player player2 is
     * @return the type of player2
     */
    public synchronized PossiblePlayers player2is()
    {
        return player2;
    }

    /**
     * get who currently is allowed to make a move
     * @return the player who's turn it currently is
     */
    public synchronized TileState currentTurn()
    {
        return currentTurn;
    }

    /**
     * get the amount of turns player 1 has made
     * @return the amount of turns player 1 made
     */
    public synchronized int getPlayer1Turns()
    {
        return player1Turns;
    }

    /**
     * get the amount of turns player 2 has made
     * @return the amount of turns player 2 made
     */
    public synchronized int getPlayer2Turns()
    {
        return player2Turns;
    }

    /**
     * get the total amount of turns completed
     * @return the total amount of turns completed
     */
    public synchronized int getTotalTurns()
    {
        return player1Turns + player2Turns;
    }

    /**
     * Get who won the game
     * @return who won the game
     * @throws IllegalStateException when there is not yet a winner
     */
    public synchronized TileState getWinner()
        throws IllegalStateException
    {
        if(!hasWinner())
        {
            throw new IllegalStateException("game does not have a winner yet");
        }
        return winner;
    }

    /**
     * get if there is a winner
     * @return true if there is a winner, false if the game is still being
     * played
     */
    public synchronized boolean hasWinner()
    {
        return winner != null;
    }

    /**
     * Get the most recent board state. The current board is the board where
     * the player who's turn it is is playing on
     * @return the most recent board state
     */
    public synchronized TileState[][] getCurrentBoard()
    {
        return boardStateHistory.get(boardStateHistory.size() - 1);
    }

    /**
     * get a previous board state
     * @param turn the turn of the previous board state being requested
     * @return the board state in the specified turn
     * @throws IllegalArgumentException when the turn specified turn value is
     * invalid. (e.g negative or too big)
     */
    public synchronized TileState[][] getBoardInTurn(int turn)
        throws IllegalArgumentException
    {
        if(turn < 0 || turn >= boardStateHistory.size())
        {
            throw new IllegalArgumentException("turn " + turn + " does not " +
                    "exist");
        }
        return boardStateHistory.get(turn);
    }

    /**
     * set if the pie rule is enabled
     * @param flag whether the pie rule is enabled
     */
    public synchronized void setPieRuleEnabled(boolean flag)
    {
        this.pieRule = flag;
    }

    /**
     * get if the pie rule is enabled
     * @return wether the pie rule is enabled
     */
    public synchronized boolean isPieRuleEnabled()
    {
        return pieRule;
    }

    /**
     * Add a watcher to the list of watchers
     * @param watcher the watcher to add
     */
    @Override
    public void attachWatcher(Watcher watcher)
    {
        watchers.add(watcher);
    }

    /**
     * Delete a watcher from the list of watchers
     * @param watcher the watcher to delete
     */
    @Override
    public void detachWatcher(Watcher watcher)
    {
        watchers.remove(watcher);
    }

    /**
     * Notify all watchers that the GameState has changed
     */
    @Override
    public void notifyWatchers()
    {
        for(Watcher w: watchers)
        {
            w.update();
        }
    }
}
