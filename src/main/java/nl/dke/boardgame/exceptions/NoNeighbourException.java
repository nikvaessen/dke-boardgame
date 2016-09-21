package nl.dke.boardgame.exceptions;

/**
 * This exception gets thrown when a HexTile is asked for a neighbour when
 * it does not have this neighbour
 */
public class NoNeighbourException extends Exception
{
    public NoNeighbourException()
    {
        super();
    }

    public NoNeighbourException(String s)
    {
        super(s);
    }
}
