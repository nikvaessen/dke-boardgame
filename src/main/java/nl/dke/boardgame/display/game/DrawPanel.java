package nl.dke.boardgame.display.game;

import java.awt.*;

import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;

public class DrawPanel
{

    /**
     * the length of one side of the hexagon
     */
    public static final int LENGTH =   20;

    /**
     * the offset from the left edge of the frame
     */
    public static final int OFFSET_X = 50;

    /**
     * the offset from the top of the frame
     */
    public static final int OFFSET_Y = 40;

    public void draw(Graphics g, TileState[][] board)
    {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(); // to convert a for counter into letters
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board[x].length; y++)
            {
                // the x coordinate of a hex if it were a square
                int xCoor = OFFSET_X + (y * LENGTH) + (x * LENGTH * 2);

                // the y coordinate of a hex if it were a square
                int yCoor = OFFSET_Y + (y * LENGTH * 2);

                // list of x coordinates for the hex based on the initial x cooridanate
                int xPoints[] =
                        {
                                xCoor + LENGTH,
                                xCoor + (2 * LENGTH),
                                xCoor + (2 * LENGTH),
                                xCoor + LENGTH,
                                xCoor,
                                xCoor
                        };

                // list of y coordinates for the hex based on the initial y cooridanate
                int yPoints[] =
                        {
                                yCoor,
                                yCoor + LENGTH,
                                yCoor + (2 * LENGTH),
                                yCoor + (3 * LENGTH),
                                yCoor + (2 * LENGTH),
                                yCoor + LENGTH
                        };

                //// TODO: 06/10/16 The board is drawn rotated by 90 degrees
                //// which means the row and columns are inverted
                switch(board[y][x]) // sets the colour of the hex
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
        for (int x = 0; x < board.length; x++)
        {
            g.drawString(Character.toString(alphabet[x]),
                    (OFFSET_X + LENGTH + (x * LENGTH * 2)) - 5, OFFSET_Y - 5); // draws the letter row at the top
        }
        for (int y = 0; y < board[0].length; y++)
        {
            g.drawString(Integer.toString(y), OFFSET_X / 2 + (y * LENGTH),
                    OFFSET_Y + (LENGTH * 2) + (y * LENGTH * 2)); // draws the number column along the bottom
        }
    }
}
