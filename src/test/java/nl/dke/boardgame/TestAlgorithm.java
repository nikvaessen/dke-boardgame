package nl.dke.boardgame;

import nl.dke.boardgame.game.GameState;
import nl.dke.boardgame.game.HexGame;
import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.hex.randomImpl.SingleThreadRandomHexBoardSimulation;
import nl.dke.boardgame.mcts.policy.UCTTreePolicy;
import nl.dke.boardgame.players.MCTSPlayer;
import nl.dke.boardgame.players.PossiblePlayers;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;

/**
 * Class to test methods
 */
public class TestAlgorithm
{
    private static final int width = 11;
    private static final int height = 11;
    private File logfile = new File("logs/test_" + System.currentTimeMillis());
    private PrintWriter writer;

    private void makeWriter()
    {
        try
        {
            writer = new PrintWriter(logfile);
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void writeToFile(String s)
    {
        if(writer == null)
        {
            makeWriter();
        }
        writer.println(s);
        writer.flush();
    }

    @Test
    public void testWritter()
    {
        writeToFile("Hello World");
    }

    @Test
    public void exploAndSipInMCTS()
    {
        //exploration parameters
        double[] Cp = {0, 0.2, 0.4, /*0.6, 0.8, 1.0, 3.0, 5.0*/};
        double[] Cpt = {0.6, 0.8, 1.0};

        int[] spis = {1, 10};

        ExecutorService threads = Executors.newFixedThreadPool(2);
        Collection<Callable<TestResult>> callables = new ArrayList<>();
        for(double c1 : Cpt)
        {
            for(int spi1 : spis)
            {
                for(double c2 : Cp)
                {
                    for(int spi2 : spis)
                    {

                        for(int i = 0; i < 10; i++)
                        {
                            HexPlayer player1 = new MCTSPlayer(TileState.PLAYER1, new UCTTreePolicy<>(c1),
                                    new SingleThreadRandomHexBoardSimulation(), spi1, 15000,
                                    PossiblePlayers.MCTS, false);

                            HexPlayer player2 = new MCTSPlayer(TileState.PLAYER2, new UCTTreePolicy<>(c2),
                                    new SingleThreadRandomHexBoardSimulation(), spi2, 15000,
                                    PossiblePlayers.MCTS, false);
                            String p1info = "Player 1 with exploration value of " + c1 + " with " + spi1 + " simulations per iteration";
                            String p2info = "Player 2 with exploration value of " + c2 + " with " + spi2 + " simulations per iteration";

                            callables.add(new TestCallable(player1, player2, p1info, p2info));
                        }

                    }
                }
            }
        }

        System.out.println("Amount of tests to do: " + callables.size());
        int p1counter = 0;
        int p2counter = 0;

        //add all takss to the threadpool to do them
        Collection<Future<TestResult>> futures = new ArrayList<>();
        for(Callable<TestResult> callable : callables)
        {
            futures.add(threads.submit(callable));
        }

        //every minute, see which tasks have completed and print their results
        while(!futures.isEmpty())
        {
            System.out.println("Amount of tests left to do: " + futures.size());
            //sleep for a minute
            try
            {
                Thread.sleep(600);

            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }

            //go over the list and see which ones are done
            Iterator<Future<TestResult>> iter = futures.iterator();
            while(iter.hasNext())
            {
                Future<TestResult> future = iter.next();
                if(future.isDone())
                {
                    try
                    {
                        TestResult result = future.get();
                        iter.remove();
                        handle(result);
                    }
                    catch(ExecutionException | InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private static int p1counter = 0;
    private static int p2counter = 0;
    private static int counter  =  0;
    private void handle(TestResult result)
    {
        counter++;
        GameState end = result.getState();
        String info = "#### Results of game " + counter + " ####\n";
        System.out.println(info);
        writeToFile(info);

        if(end.getWinner() == TileState.PLAYER1)
        {
            p1counter++;
        }
        else
        {
            p2counter++;
        }

        System.out.println(result.getPlayer1());
        System.out.println(result.getPlayer2());
        writeToFile(result.getPlayer1());
        writeToFile(result.getPlayer2());

        System.out.println(printBoard(end.getCurrentBoard()));
        writeToFile("\n" + printBoard(end.getCurrentBoard()));
        String win = "winner: " + result.getState().getWinner() + "\n";
        writeToFile(win);
        System.out.println(win);
    }

    private class TestResult
    {
        GameState state;
        String player1;
        String player2;

        public TestResult(GameState state, String player1, String player2)
        {
            this.state = state;
            this.player1 = player1;
            this.player2 = player2;
        }

        public GameState getState()
        {
            return state;
        }

        public String getPlayer1()
        {
            return player1;
        }

        public String getPlayer2()
        {
            return player2;
        }
    }

    private class TestCallable
        implements Callable<TestResult>
    {
        HexPlayer player1;
        HexPlayer player2;
        String player1info;
        String player2info;

        public TestCallable(HexPlayer player1, HexPlayer player2, String player1info, String player2info)
        {
            this.player1 = player1;
            this.player2 = player2;
            this.player1info = player1info;
            this.player2info = player2info;
        }

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public TestResult call() throws Exception
        {
            return new TestResult(testAlgorithm(player1, player2),  player1info, player2info);
        }

    }

    private GameState testAlgorithm(HexPlayer player1, HexPlayer player2)
    {
        HexGame.DELAY_BETWEEN_TURNS = 0;
        HexGame game = new HexGame(width, height, player1, player2);
        game.start();
        while(!game.isGameOver())
        {
            //System.out.println(printBoard(game.getGameState().getCurrentBoard()));
            try
            {
                Thread.sleep(1000);
            } catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return game.getGameState();
    }

    String printBoard(TileState[][] board)
    {
        String st = "";
        for(TileState[] tiles : board)
        {
            for(TileState tile : tiles)
            {
                if(tile == TileState.NEUTRAL)
                {
                    st += "0 ";
                }
                else if(tile == TileState.PLAYER1)
                {
                    st += "1 ";
                }
                else
                {
                    st += "2 ";
                }
            }
            st += "\n";
        }
        return st;
    }

}
