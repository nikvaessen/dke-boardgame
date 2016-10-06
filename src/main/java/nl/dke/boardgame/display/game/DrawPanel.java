package nl.dke.boardgame.display.game;

import java.awt.*;

import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;

public class DrawPanel
{

    private static final int LENGTH = 20; // the length of one side
    private static final int OFFSET_X = 50; // the offset from the left edge of the frame
    private static final int OFFSET_Y = 40; //  the offest from the top of the frame

    public void draw(Graphics g, Board board)
    {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(); // to convert a for counter into letters
        for (int x = 0; x < board.getWidth(); x++)
        {
            for (int y = 0; y < board.getHeight(); y++)
            {
                int xCoor = OFFSET_X + (y * LENGTH) + (x * LENGTH * 2); // the x coordinate of a hex if it were a square
                int yCoor = OFFSET_Y + (y * LENGTH * 2); // the y coordinate of a hex if it were a square
                int xPoints[] = {xCoor + LENGTH, xCoor + (2 * LENGTH), xCoor + (2 * LENGTH), xCoor + LENGTH, xCoor, xCoor}; // list of x coordinates for the hex based on the intial x cooridanate
                int yPoints[] = {yCoor, yCoor + LENGTH, yCoor + (2 * LENGTH), yCoor + (3 * LENGTH), yCoor + (2 * LENGTH), yCoor + LENGTH}; // list of y coordinates for the hex based on the intial y cooridanate

                //// TODO: 06/10/16 The board is drawn rotated by 90 degrees
                //// which means the row and columns are inverted
                switch(board.getState(y, x)) // sets the colour of the hex
                {
                	case NEUTRAL: g.setColor(Color.WHITE); break;
                	case PLAYER1: g.setColor(Color.RED); break;
                	case PLAYER2: g.setColor(Color.BLUE); break;
                }
                g.fillPolygon(xPoints, yPoints, 6);
                g.setColor(Color.BLACK); //  draws an outline
                g.drawPolygon(xPoints, yPoints, 6);
            }
        }
        for (int x = 0; x < 11; x++)
        {
            g.drawString(Character.toString(alphabet[x]),
                    (OFFSET_X + LENGTH + (x * LENGTH * 2)) - 5, OFFSET_Y - 5); // draws the letter row at the top
        }
        for (int y = 0; y < 11; y++)
        {
            g.drawString(Integer.toString(y), OFFSET_X / 2 + (y * LENGTH),
                    OFFSET_Y + (LENGTH * 2) + (y * LENGTH * 2)); // draws the number column along the bottom
        }
    }
}
