package nl.dke.boardgame.exceptions;

/**
 *
 */
public class NoNeighbourException extends Exception
{
    public NoNeighbourException(){
        super();
    }

    public NoNeighbourException(String s){
        super(s);
    }
}
