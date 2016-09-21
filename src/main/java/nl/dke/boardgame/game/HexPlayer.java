package nl.dke.boardgame.game;

import nl.dke.boardgame.game.board.TileState;

/**
 *
 */
//// TODO: 21/09/16 Document this abstract class
public abstract class HexPlayer
{
    private TileState claimsState;

    public TileState claimsAs()
    {
        return claimsState;
    }

    public void finishMove(Move move)
    {

    }
}
