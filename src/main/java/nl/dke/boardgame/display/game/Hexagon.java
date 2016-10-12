package nl.dke.boardgame.display.game;

import java.awt.*;

/**
 *
 */
public class Hexagon extends Polygon
{
    private int row;

    private int column;

    public Hexagon(int[] xpoint, int[] ypoints, int npoints, int row, int column)
    {
        super(xpoint, ypoints, npoints);
        this.row = row;
        this.column = column;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public String toString()
    {
        String tileString = String.valueOf(Character.toChars('A' + column))
                + Integer.toString(row);
        return String.format("row: %d column: %d tile: %s", row, column, tileString);
    }
}
