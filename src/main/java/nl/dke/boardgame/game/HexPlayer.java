package nl.dke.boardgame.game;

import nl.dke.boardgame.game.board.TileState;

/**
 *
 */
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
