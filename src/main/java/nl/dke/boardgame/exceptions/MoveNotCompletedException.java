package nl.dke.boardgame.exceptions;

/**
 * This exception is thrown when the Move class has not been given a move to
 * make
 */
public class MoveNotCompletedException extends Exception
{
    public MoveNotCompletedException()
    {
        super();
    }

    public MoveNotCompletedException(String s)
    {
        super(s);
    }
}
