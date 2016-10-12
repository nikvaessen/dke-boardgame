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

    public synchronized void in(int row, int column)
        throws IllegalArgumentException, NotAcceptingInputException
    {
        if(!accepting())
        {
            throw new NotAcceptingInputException("InputProcessor is not" +
                    "currently waiting for input");
        }
        process(row, column);
    }

    public synchronized void in(String textInput)
        throws IllegalArgumentException, NotAcceptingInputException
    {
        if(!accepting())
        {
            throw new NotAcceptingInputException("InputProcessor is not" +
                    "currently waiting for input");
        }
        try
        {
            String[] splittedText = new String[2];
            splittedText[0] = textInput.substring(0, 1);
            splittedText[1] = textInput.substring(1);
            if (splittedText.length != 2 || splittedText[0].length() != 1)
            {
                throw new IllegalArgumentException("Input has wrong syntax.  ");
            }
            else
            {
                char columnLetter = splittedText[0].toLowerCase().toCharArray()[0];
                int column = columnLetter - 'a';
                int row = Integer.parseInt(splittedText[1]);
                System.out.printf("Human entered: column: %d row %d%n", column, row);
                process(row, column);
            }
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Could not format the given" +
                    " row number");
        }
    }

    public synchronized boolean accepting()
    {
        return currentMove != null;
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
                System.out.println("was a correct move, move is now null");
                currentMove = null;
            }
            else
            {
                throw new IllegalArgumentException("Tile was already claimed");
            }
        }
    }

}

