package nl.dke.boardgame;

import nl.dke.boardgame.game.GameState;
import nl.dke.boardgame.game.HexGame;
import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.hex.randomImpl.SingleThreadRandomHexBoardSimulation;
import nl.dke.boardgame.mcts.policy.UCTTreePolicy;
import nl.dke.boardgame.players.MCTSPlayer;
import nl.dke.boardgame.players.PossiblePlayers;
import nl.dke.boardgame.players.RandomHexPlayer;
import org.junit.Test;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Class to test methods
 */
public class TestAlgorithm
{
    private static final int width  = 11;
    private static final int height = 11;
    private File logfile = new File("logs/test_" + System.currentTimeMillis());
    private PrintWriter writer;

    private void makeWriter()
    {
        try
        {
            writer = new PrintWriter(logfile);
        }
        catch(IOException e)
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
        writer.write(s);
        writer.flush();
    }

    @Test
    public void testWritter(){
        writeToFile("Hello World");
    }

    @Test
    public void exploAndSipInMCTS(){
        //exploration parameters
        double[] Cp = {0, 0.2, 0.4, 0.6, 0.8, 1.0, 3.0, 5.0};

        int[] spis = {1};

        for(double c1 : Cp){
            for(int spi1 : spis){

                for(double c2 : Cp) {
                    for(int spi2 : spis){
                        int p1counter = 0;
                        int p2counter = 0;

                        String st = "";
                        st += "Player 1 with exploration value of " + c1 + " with " + spi1 + " simulations per iteration \n";
                        st += "\tPlayer 2 with exploration value of " + c2 + " with " + spi2 + " simulations per iteration \n";
                        System.out.print(st);
                        writeToFile(st);

                        for(int i = 0; i<10; i++) {

                            HexPlayer player1 = new MCTSPlayer(TileState.PLAYER1, new UCTTreePolicy<>(c1),
                                    new SingleThreadRandomHexBoardSimulation(), spi1, 15000,
                                    PossiblePlayers.MCTS, false);

                            HexPlayer player2 = new MCTSPlayer(TileState.PLAYER2, new UCTTreePolicy<>(c2),
                                    new SingleThreadRandomHexBoardSimulation(), spi2, 15000,
                                    PossiblePlayers.MCTS, false);

                            GameState end = testAlgorithm(player1, player2);


                            String win = "\t\tWinner of game "+ (i+1) + " is: " + end.getWinner().toString();

                            if(end.getWinner() == TileState.PLAYER1)
                                p1counter++;
                            else
                                p2counter++;

                            writeToFile(win);
                            System.out.print(win);
                        }
                        String count1 = "Player1 won " + p1counter + " games";
                        String count2 = "Player2 won " + p2counter + " games";
                        writeToFile(count1 + "\n" + count2);
                        System.out.print(count1 + "\n" + count2);
                    }
                }
            }
        }
    }

    @Test
    public void testMCTSvsMCTS()
    {

        HexPlayer player1 = new MCTSPlayer(TileState.PLAYER1,
                new UCTTreePolicy<>(1),
                new SingleThreadRandomHexBoardSimulation(),
                1,
                1000,
                PossiblePlayers.MCTS,
                false);
        HexPlayer player2 = new RandomHexPlayer(TileState.PLAYER2);
        GameState gameState = testAlgorithm(player1, player2);
        System.out.printf("winner: " + gameState.getWinner());
    }


    private GameState testAlgorithm(HexPlayer player1, HexPlayer player2)
    {
        HexGame.DELAY_BETWEEN_TURNS = 0;
        HexGame game = new HexGame(width, height, player1, player2);
        game.start();
        while(!game.isGameOver())
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return game.getGameState();
    }

}
