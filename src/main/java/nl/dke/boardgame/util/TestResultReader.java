package nl.dke.boardgame.util;

import nl.dke.boardgame.game.board.TileState;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by nik on 1/23/17.
 */
public class TestResultReader
{
    public static void main(String[] args)
            throws Exception
    {
        File file = new File("/home/nik/workspace/dke-boardgame/results/test_1485109709956");
//        Scanner in = new Scanner(file);
//        String s = getOneResult(in);
//        System.out.println(s);
//        System.out.println();
//        System.out.println(Arrays.toString(extractPlayer1(s)));
//        System.out.println(Arrays.toString(extractPlayer2(s)));
//        System.out.println(extractWinner(s));
//        //System.out.println(Arrays.toString(findValues("blablabla 0.0 blabla 1")));
//        System.out.println("\n\n\n");

        //add every match to a result (store them as a Result class)
        Scanner in = new Scanner(file);
        ArrayList<Result> results = new ArrayList<>();
        while(in.hasNext())
        {
            String s = getOneResult(in);
            //System.out.println("Extracted:\n" + s);
            results.add(new Result(
                    extractPlayer1(s),
                    extractPlayer2(s),
                    extractWinner(s)
            ));

            //System.out.println("Added: \n" + results.get(results.size() - 1) + "\n");
        }
        //System.out.println("printed " + results.size() + " results");

        //go over every result and put them in the same array if they have the same parameters
        ArrayList<ArrayList<Result>> matches = new ArrayList<>();
        while(!results.isEmpty())
        {
            Result firstResult = results.remove(0);
            //System.out.println("Looking for results similar to:\n" + firstResult);
            ArrayList<Result> match = new ArrayList<>();
            match.add(firstResult);
            Iterator<Result> iter = results.iterator();
            //System.out.println("The following are similar to the result above:");
            while(iter.hasNext())
            {
                Result r = iter.next();
                if(parametersAreEqual(firstResult, r))
                {
              //      System.out.println(r);
                    iter.remove();
                    match.add(r);
                }
            }
            //System.out.println();
            matches.add(match);
        }

        for(ArrayList<Result> match : matches)
        {
            Result r = match.get(0);
            String[] p1 = r.getP1();
            String[] p2 = r.getP2();

            System.out.printf("c: %s, spi: %s vs c: %s, spi: %s%n%n",
                    p1[1], p1[2], p2[1], p2[2]);

            int p1count = 0;
            int p2count = 0;
            for(Result result : match)
            {
                if(result.getTileWinner() == TileState.PLAYER1)
                {
                    p1count++;
                }
                else if(result.getTileWinner() == TileState.PLAYER2)
                {
                    p2count++;
                }
                else
                {
                    System.out.println("\n\n\n\n\n######## SOMETHING WENT WRONG\n\n\n\n\n");
                }
                //System.out.println(result.toString());
            }
            System.out.println("Player 1 wins: " + p1count + " times\nPlayer 2 wins: " + p2count + " times");
            System.out.println("\n\n");
        }
    }

    private static boolean parametersAreEqual(Result match1, Result match2)
    {
        return (parametersAreEqual(match1.getP1(), match2.getP1()) &&
                parametersAreEqual(match1.getP2(), match2.getP2()))
                /*||
                (parametersAreEqual(match1.getP1(), match2.getP2()) &&
                parametersAreEqual(match1.getP2(), match2.getP1()))*/;
    }

    private static boolean parametersAreEqual(String[] p1, String[] p2)
    {
        return p1[1].equals(p2[1]) && p1[2].equals(p2[2]);
    }

    private static String getOneResult(Scanner in)
            throws Exception
    {
        String result = "";
        String line;
        do
        {
            line = in.nextLine();
            result += line + "\n";
            if(line.contains("winner"))
            {
                break;
            }
        }
        while(in.hasNext());
        return result;
    }

    private static String[] extractPlayer1(String s) throws Exception
    {
        try
        {
            String line = findLine(s, "Player 1");
            return findValues(line);
        }
        catch(Exception e)
        {
            System.out.println("Could not find Player 1 in:\n" + s );
            throw e;
        }
    }

    private static String[] extractPlayer2(String s)
            throws Exception
    {
        try
        {
            String line = findLine(s, "Player 2");
            return findValues(line);
        }
        catch(Exception e)
        {
            System.out.println("Could not find Player 2 in:\n" + s );
            throw e;
        }
    }

    private static String extractWinner(String s)
            throws Exception
    {
        try
        {
            String line = findLine(s, "winner:");
            if(line.contains("PLAYER1"))
            {
                return "PLAYER1";
            }
            else if(line.contains("PLAYER2"))
            {
                return "PLAYER2";
            }
            else
            {
                throw new Exception("Could not find winner");
            }
        }
        catch(Exception e)
        {
            System.out.println("Could not find winner in:\n" + s);
            throw e;
        }
    }

    private static String findLine(String s, String toFind)
        throws Exception
    {
        Scanner scan = new Scanner(s);
        while(scan.hasNext())
        {
            String line = scan.nextLine();
            if(line.contains(toFind))
            {
                return line;
            }
        }
        throw new Exception("Couldn't find string toFind");
    }

    private static String[] findValues(String s)
    {
        ArrayList<String> values = new ArrayList<>();
        for(String word : s.split(" "))
        {
            try
            {
                Double d = Double.parseDouble(word);
                values.add(word);
            }
            catch(NumberFormatException e)
            {
                //do nothing
            }
        }
        return values.toArray(new String[values.size()]);
    }

    private static class Result
    {
        private String[] p1;
        private String[] p2;
        private String winner;

        public Result(String[] p1, String[] p2, String winner)
        {
            this.p1 = p1;
            this.p2 = p2;
            this.winner = winner;
        }

        public String[] getP1()
        {
            return p1;
        }

        public String[] getP2()
        {
            return p2;
        }

        public String getWinner()
        {
            return winner;
        }

        public TileState getTileWinner()
        {
            if(getWinner().contains("PLAYER1"))
            {
                return TileState.PLAYER1;
            }
            else if(getWinner().contains("PLAYER2"))
            {
                return TileState.PLAYER2;
            }
            else
            {
                return TileState.NEUTRAL;
            }
        }

        public String toString()
        {
            String s = "";
            s += Arrays.toString(getP1());
            s += " vs ";
            s += Arrays.toString(getP2());
            s += "\nwinner: " + getWinner();
            return s;
        }
    }

}