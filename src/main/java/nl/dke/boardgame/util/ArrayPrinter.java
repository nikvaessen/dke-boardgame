package nl.dke.boardgame.util;

import nl.dke.boardgame.game.board.TileState;

/**
 * Created by nik on 08/01/17.
 */
public class ArrayPrinter
{
    public static String toString(boolean[][] array)
    {
        String s = "";
        for(int i = 0; i < array.length; i++)
        {
            for(int j = 0; i < array[i].length; j++)
            {
                if(array[i][j])
                {
                    s += "t ";
                }
                else
                {
                    s += "f ";
                }
            }
            s += "\n";
        }
        return s;
    }

    public static String toString(TileState[][] array)
    {
        String s = "";
        for(int i = 0; i < array.length; i++)
        {
            for(int j = 0; j < array[i].length; j++)
            {
                if(array[i][j].equals(TileState.NEUTRAL))
                {
                    s += "n ";
                }
                else if(array[i][j].equals(TileState.PLAYER1))
                {
                    s += "1 ";
                }
                else
                {
                    s += "2 ";
                }
            }
            s += "\n";
        }
        return s;
    }
}
