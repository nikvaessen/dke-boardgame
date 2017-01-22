package nl.dke.boardgame;

import nl.dke.boardgame.game.GameState;
import nl.dke.boardgame.game.HexGame;
import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.hex.randomImpl.SingleThreadRandomHexBoardSimulation;
import nl.dke.boardgame.mcts.policy.UCTTreePolicy;
import nl.dke.boardgame.players.MCTSPlayer;
import nl.dke.boardgame.players.PossiblePlayers;
import nl.dke.boardgame.players.RandomHexPlayer;
import nl.dke.boardgame.util.ArrayPrinter;
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
        final HexGame game = new HexGame(width, height, player1, player2);
        Thread thread = new Thread(game::start);
        thread.start();
        while(!game.isGameOver())
        {
            try
            {
                thread.join();
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return game.getGameState();
    }

}
