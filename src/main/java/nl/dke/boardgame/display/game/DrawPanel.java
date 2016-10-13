package nl.dke.boardgame.display.game;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private List<Hexagon> hexagons;

    public DrawPanel()
    {
        hexagons = new ArrayList<>();
    }

    private void calculatePolygons(TileState[][] board)
    {
        for (int x = 0; x < board.length; x++)
        {
            for (int y = 0; y < board[x].length; y++)
            {
                // the x coordinate of a hex if it were a square
                int xCoor = OFFSET_X + (y * LENGTH * 2) + (x * LENGTH);

                // the y coordinate of a hex if it were a square
                int yCoor = OFFSET_Y + (x * LENGTH * 2);

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

                hexagons.add(new Hexagon(xPoints, yPoints, 6, x, y));
            }
        }
    }

    public Hexagon getHexagon(Point p)
    {
        for(Hexagon hex : hexagons)
        {
            if(hex.contains(p))
            {
                return hex;
            }
        }
        return null;
    }


    public void draw(Graphics g, TileState[][] board)
    {
        if(board.length * board[0].length != hexagons.size())
        {
            calculatePolygons(board);
        }

        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(); // to convert a for counter into letters
        for (Hexagon hex : hexagons)
        {
                switch(board[hex.getRow()][hex.getColumn()]) // sets the colour of the hex
                {
                    case NEUTRAL: g.setColor(Color.WHITE); break;
                    case PLAYER1: g.setColor(Color.RED); break;
                    case PLAYER2: g.setColor(Color.BLUE); break;
                }
                g.fillPolygon(hex);
                g.setColor(Color.BLACK); //  draws an outline
                g.drawPolygon(hex);

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
        drawLeftEdge(g, board);
        drawRightEdge(g, board);
        drawTopEdge(g, board);
        drawBotEdge(g, board);
    }
    
    public void drawLeftEdge(Graphics g, TileState[][] board ) {
    	for (int y = 0; y < board[0].length; y++) {
    		// the x coordinate of a hex if it were a square
    		int xCoor = OFFSET_X + (y * LENGTH);

	        // the y coordinate of a hex if it were a square
	        int yCoor = OFFSET_Y + (y * LENGTH * 2);
	        
	        // list of x coordinates for the hex based on the initial x cooridanate
            int xPoints[] =
                    {
                            xCoor,
                            xCoor,
                            xCoor + LENGTH
                    };

            // list of y coordinates for the hex based on the initial y cooridanate
            int yPoints[] =
                    {
                            yCoor + LENGTH,
                            yCoor + (2 * LENGTH),
                            yCoor + (3 * LENGTH)
                    };
            g.setColor(Color.RED);
            g.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            if (y != board[0].length - 1) g.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
    	}
    }
    
    public void drawRightEdge(Graphics g, TileState[][] board ) {
    	for (int y = 0; y < board[0].length; y++) {
    		// the x coordinate of a hex if it were a square
    		int xCoor = OFFSET_X + (y * LENGTH) + (board.length * LENGTH * 2);

	        // the y coordinate of a hex if it were a square
	        int yCoor = OFFSET_Y + (y * LENGTH * 2);
	        
	        // list of x coordinates for the hex based on the initial x cooridanate
            int xPoints[] =
                    {
                            xCoor,
                            xCoor,
                            xCoor + LENGTH
                    };

            // list of y coordinates for the hex based on the initial y cooridanate
            int yPoints[] =
                    {
                            yCoor + LENGTH,
                            yCoor + (2 * LENGTH),
                            yCoor + (3 * LENGTH)
                    };
            g.setColor(Color.RED);
            g.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            if (y != board[0].length - 1) g.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
    	}
    }
    
    public void drawTopEdge(Graphics g, TileState[][] board ) {
    	for (int x = 0; x < board.length; x++) {
    		// the x coordinate of a hex if it were a square
    		int xCoor = OFFSET_X + (x * LENGTH * 2);

	        // the y coordinate of a hex if it were a square
	        int yCoor = OFFSET_Y;
	        
	        // list of x coordinates for the hex based on the initial x cooridanate
            int xPoints[] =
                    {
                            xCoor,
                            xCoor + LENGTH,
                            xCoor + (LENGTH * 2)
                    };

            // list of y coordinates for the hex based on the initial y cooridanate
            int yPoints[] =
                    {
                            yCoor + LENGTH,
                            yCoor,
                            yCoor + LENGTH
                    };
            g.setColor(Color.BLUE);
            g.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            g.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
    	}
    }
    
    public void drawBotEdge(Graphics g, TileState[][] board ) {
    	for (int x = 0; x < board.length; x++) {
    		// the x coordinate of a hex if it were a square
    		int xCoor = OFFSET_X + ((board.length - 1) * LENGTH) +(x * LENGTH * 2);

	        // the y coordinate of a hex if it were a square
	        int yCoor = OFFSET_Y + ((board[0].length - 1) * LENGTH * 2);
	        
	        // list of x coordinates for the hex based on the initial x cooridanate
            int xPoints[] =
                    {
                            xCoor,
                            xCoor + LENGTH,
                            xCoor + (LENGTH * 2)
                    };

            // list of y coordinates for the hex based on the initial y cooridanate
            int yPoints[] =
                    {
                            yCoor + (LENGTH * 2),
                            yCoor + (LENGTH * 3),
                            yCoor + (LENGTH * 2)
                    };
            g.setColor(Color.BLUE);
            g.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            g.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
    	}
    }
}
