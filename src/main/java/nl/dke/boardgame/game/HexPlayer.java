package nl.dke.boardgame.game;

import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.players.PossiblePlayers;

/**
 *
 */
//// TODO: 21/09/16 Document this abstract class
public abstract class HexPlayer
{

    /**
     * Determines if the player plays as "player1" or "player2"
     */
    private TileState claimsState;

    /**
     * Create the HexPlayer
     * @param claimer if the player is player1 or player2
     * @throws IllegalArgumentException when neutral is given as argument
     */
    public HexPlayer(TileState claimer)
        throws IllegalArgumentException
    {
        if(claimer == TileState.NEUTRAL)
        {
            throw new IllegalArgumentException("Player cannot be Neutral");
        }
        claimsState = claimer;
    }

    /**
     * gets whether the player is player1 or player2
     * @return player1 or player2
     */
    public TileState claimsAs()
    {
        return claimsState;
    }

    /**
     * Method to decide which move the player makes
     * @param move the move the player needs to make
     */
    public abstract void finishMove(Move move);

    /**
     * Get the type of player the class implements
     * @return the type of player
     */
    public abstract PossiblePlayers getTypeOfPlayer();

}
