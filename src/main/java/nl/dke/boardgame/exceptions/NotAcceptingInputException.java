package nl.dke.boardgame.exceptions;

/**
 * This exception gets used the InputProcesser has not been given any input
 * @author Nik
 */
public class NotAcceptingInputException extends Exception
{
    public NotAcceptingInputException()
    {
        super();
    }

    public NotAcceptingInputException(String s)
    {
        super(s);
    }
}
