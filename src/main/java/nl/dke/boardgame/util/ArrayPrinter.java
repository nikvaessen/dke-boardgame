package nl.dke.boardgame.util;

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
}
