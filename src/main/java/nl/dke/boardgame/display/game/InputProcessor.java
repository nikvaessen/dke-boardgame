package nl.dke.boardgame.display.game;

import nl.dke.boardgame.exceptions.NotAcceptingInputException;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.board.Board;

/**
 * This class handles the translation of user input to the HumanHexPlayer
 */
public class InputProcessor
{
    private Move currentMove;

    public synchronized void give(Move move)
            throws NotAcceptingInputException
    {
        if(currentMove != null)
        {
            throw new NotAcceptingInputException();
        }
        currentMove = move;
    }

    public synchronized void in(String textInput)
        throws IllegalArgumentException, NotAcceptingInputException
    {
        if(currentMove == null)
        {
            throw new NotAcceptingInputException();
        }
        try
        {
            String[] splittedText = textInput.split(":");
            if (splittedText.length != 2 || splittedText[1].length() != 1)
            {
                throw new IllegalArgumentException("input has wrong syntax");
            }
            else
            {
                int row = Integer.parseInt(splittedText[0]);
                char columnLetter = splittedText[1].toLowerCase().toCharArray()[0];
                int column = columnLetter - 'a';
                System.out.printf("Human entered: row: %d column %d%n", row, column);
                process(row, column);
            }
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Could not format the number " +
                    "given as a row" );
        }
    }

    public synchronized boolean isProccesing()
    {
        return currentMove == null;
    }

    private void process(int row, int column)
        throws IllegalArgumentException
    {
        System.out.println(String.format("received %d and %d", row, column));
        Board board = currentMove.getBoard();
        if(row < 0 || column < 0 ||
                row >= board.getHeight() || column >= board.getWidth())
        {
            throw new IllegalArgumentException("Given input is out of bounds");
        }
        else
        {
            currentMove.setRow(row);
            currentMove.setColumn(column);
            if(currentMove.verify())
            {
                currentMove = null;
            }
            else
            {
                throw new IllegalArgumentException("Tile was already claimed");
            }
        }
    }

}

