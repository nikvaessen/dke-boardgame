package nl.dke.boardgame.exceptions;

/**
 * This exception gets used the InputProcesser has not been given any input
 * @author Nik
 */
public class AlreadyProccesingMoveException extends Exception
{
    public AlreadyProccesingMoveException()
    {
        super();
    }

    public AlreadyProccesingMoveException(String s)
    {
        super(s);
    }
}
