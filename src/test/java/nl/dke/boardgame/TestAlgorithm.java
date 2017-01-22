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

import java.util.Arrays;

/**
 * Class to test methods
 */
public class TestAlgorithm
{
    private static final int width  = 11;
    private static final int height = 11;

    @Test
    public void exploAndSipInMCTS(){
        //exploration parameters
        double[] Cp = {0, 0.2, 0.4, 0.6, 0.8, 1.0, 3.0, 5.0};

        int[] spis = {1, 10, 100};

        String st = "";

        for(double c1 : Cp){
            for(int spi1 : spis){
                st += "Exploration value of " + c1 + " with " + spi1 + " simulations per iteration \n";
                for(double c2 : Cp) {
                    for(int spi2 : spis){

                        HexPlayer player1 = new MCTSPlayer(TileState.PLAYER1, new UCTTreePolicy<>(c1),
                                new SingleThreadRandomHexBoardSimulation(), spi1, 20000,
                                PossiblePlayers.MCTS, false);

                        HexPlayer player2 = new MCTSPlayer(TileState.PLAYER2, new UCTTreePolicy<>(c2),
                                new SingleThreadRandomHexBoardSimulation(), spi2, 20000,
                                PossiblePlayers.MCTS, false);;

                        GameState end = testAlgorithm(player1,player2);

                        st += "\tExploration value of " + c2 + " with " + spi2 + " simulations per iteration \n";
                        st += "\t\tWinner is " + end.getWinner().toString();

                        System.out.print(st);
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
