package nl.dke.boardgame.game;

import nl.dke.boardgame.display.game.InputProcessor;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.hex.randomImpl.MultiThreadRandomHexBoardSimulation;
import nl.dke.boardgame.mcts.hex.randomImpl.SingleThreadRandomHexBoardSimulation;
import nl.dke.boardgame.mcts.policy.UCTTreePolicy;
import nl.dke.boardgame.players.*;

/**
 * The table encapsulates a HexGame and it's player. There should be able to
 * accept either human players or AI players.
 */
//// TODO: 21/09/16 document this class
public class Table
{
    private HexPlayer player1;

    private HexPlayer player2;

    private int currentHeight = HexGame.DEFAULT_BOARD_DIMENSION;

    private int currentWidth = HexGame.DEFAULT_BOARD_DIMENSION;

    private boolean pieRule;


    public HexGame createNewGame()
    {
        HexGame game = new HexGame(currentWidth, currentHeight, player1,
                player2);
        game.enablePieRule(pieRule);
        return game;
    }

    public void setPlayer1(PossiblePlayers type, int limit)
    {
        player1 = createPlayer(type, TileState.PLAYER1, limit);
    }

    public void setPlayer2(PossiblePlayers type, int limit)
    {
        player2 = createPlayer(type, TileState.PLAYER2, limit);
    }

    public void setPlayer1(InputProcessor processor)
    {
        player1 = new HumanHexPlayer(TileState.PLAYER1, processor);
    }

    public void setPlayer2(InputProcessor processor)
    {
        player2 = new HumanHexPlayer(TileState.PLAYER2, processor);
    }

    private HexPlayer createPlayer(PossiblePlayers type, TileState player, int limit)
    {
        switch(type)
        {
            case random:
                return new RandomHexPlayer(player);
            case MCTS:
                return new MCTSPlayer(
                        player,
                        new UCTTreePolicy<>(0.7),
                        new SingleThreadRandomHexBoardSimulation(),
                        10,
                        20000);
            case MCTSLeafPar:
                return new MCTSPlayer(
                        player,
                        new UCTTreePolicy<>(0.7),
                        new MultiThreadRandomHexBoardSimulation(),
                        10,
                        20000);
            case alphabeta:
                return new AlphaBetaPlayer(player, limit);
            default:
                throw new IllegalArgumentException("couldn't create a" +
                        "HexPlayer, given argument" + type + " is not " +
                        "implemented");
        }
    }

    public void setBoardDimensions(int width, int height)
            throws IllegalArgumentException
    {
        if(width < HexGame.MINIMUM_BOARD_DIMENSION ||
                width > HexGame.MAXIMUM_BOARD_DIMENSION ||
                height < HexGame.MINIMUM_BOARD_DIMENSION ||
                height > HexGame.MAXIMUM_BOARD_DIMENSION)
        {
            throw new IllegalArgumentException();
        }
        this.currentHeight = height;
        this.currentWidth = width;
    }

    /**
     * set if the pie rule is enabled
     *
     * @param flag whether the pie rule is enabled
     */
    public void setPieRuleEnabled(boolean flag)
    {
        this.pieRule = flag;
    }

    /**
     * get if the pie rule is enabled
     *
     * @return wether the pie rule is enabled
     */
    public boolean isPieRuleEnabled()
    {
        return pieRule;
    }

}
