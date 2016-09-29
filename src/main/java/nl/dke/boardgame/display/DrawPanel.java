package nl.dke.boardgame.display;

import java.awt.*;

public class DrawPanel
{

    private static final int LENGTH = 20;
    private static final int OFFSET_X = 50;
    private static final int OFFSET_Y = 40;

    public void draw(Graphics g, Board board)
    {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (int x = 0; x < board.getWidth; x++)
        {
            for (int y = 0; y < board.getHeight; y++)
            {
                int xCoor = OFFSET_X + (y * LENGTH) + (x * LENGTH * 2);
                int yCoor = OFFSET_Y + (y * LENGTH * 2);
                int xPoints[] = {xCoor + LENGTH, xCoor + (2 * LENGTH), xCoor + (2 * LENGTH), xCoor + LENGTH, xCoor, xCoor};
                int yPoints[] = {yCoor, yCoor + LENGTH, yCoor + (2 * LENGTH), yCoor + (3 * LENGTH), yCoor + (2 * LENGTH), yCoor + LENGTH};
                switch(board.getTile[x][y].getState())
                {
                	case Tilestate.NEUTRAL: g.setColor(Color.WHITE); break;
                	case Tilestate.PLAYER1: g.setColor(Color.RED); break;
                	case Tilestate.PLAYER2: g.setColor(Color.BLUE); break;
                }
                g.fillPolygon(xPoints, yPoints, 6);
                g.setColor(Color.BLACK);
                g.drawPolygon(xPoints, yPoints, 6);
            }
        }
        for (int x = 0; x < 11; x++)
        {
            g.drawString(Character.toString(alphabet[x]),
                    (OFFSET_X + LENGTH + (x * LENGTH * 2)) - 5, OFFSET_Y - 5);
        }
        for (int y = 0; y < 11; y++)
        {
            g.drawString(Integer.toString(y), OFFSET_X / 2 + (y * LENGTH),
                    OFFSET_Y + (LENGTH * 2) + (y * LENGTH * 2));
        }
    }
}
