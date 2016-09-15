package nl.dke.boardgame.exceptions;

/**
 * This exception gets used when a Tile has already been claimed by either
 * player 1 or player 2 and it claimed again
 *
 * @author Nik
 */
public class AlreadyClaimedException extends Exception
{
    public AlreadyClaimedException()
    {
        super();
    }

    public AlreadyClaimedException(String s)
    {
        super(s);
    }
}
