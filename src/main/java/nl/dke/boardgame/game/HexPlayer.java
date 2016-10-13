package nl.dke.boardgame.game;

import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.players.PossiblePlayers;

/**
 * A basis class for an AI or human class being able to play the HexGame
 */
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
     * Method to decide what the pie move should be made
     * @param move the pie move to make
     */
    public abstract void finishPieMove(PieMove move);

    /**
     * Get the type of player the class implements
     * @return the type of player
     */
    public abstract PossiblePlayers getTypeOfPlayer();

}
