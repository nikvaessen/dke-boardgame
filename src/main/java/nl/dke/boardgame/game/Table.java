package nl.dke.boardgame.game;

import nl.dke.boardgame.display.game.InputProcessor;
import nl.dke.boardgame.players.AlphaBetaElectrical;
import nl.dke.boardgame.players.AlphaBetaDijkstra;
import nl.dke.boardgame.players.HumanHexPlayer;
import nl.dke.boardgame.players.PossiblePlayers;
import nl.dke.boardgame.players.RandomHexPlayer;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.hex.HexBoardAction;
import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.mcts.hex.randomImpl.MultiThreadRandomHexBoardSimulation;
import nl.dke.boardgame.mcts.hex.randomImpl.OldMultiThreadRandomHexBoardSimulation;
import nl.dke.boardgame.mcts.hex.randomImpl.SingleThreadRandomHexBoardSimulation;
import nl.dke.boardgame.mcts.policy.AMAFPolicy;
import nl.dke.boardgame.mcts.policy.UCTTreePolicy;
import nl.dke.boardgame.players.*;

/**
 * The table encapsulates a HexGame and it's player. There should be able to
 * accept either human players or AI players.
 */
//// TODO: 21/09/16 document this class
public class Table
{
    public final static int AMOUNT_OF_SIMULATIONS_PER_ITERATION = 1;
    public final static int TIME_ALLOWED_FOR_MCTS_IN_MS = 1000;
    public final static double EXPLORATION_PARAMETER_FOR_UCT = 0.7d;
    public final static double AMAF_BIAS_VALUE = 1;
    public final static boolean REUSE_TREE = true;
    public final static int ALPHA_BETA_LIMIT = 1;

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

    public void setPlayer1(PossiblePlayers type)
    {
        player1 = createPlayer(type, TileState.PLAYER1);
    }

    public void setPlayer2(PossiblePlayers type)
    {
        player2 = createPlayer(type, TileState.PLAYER2);
    }

    public void setPlayer1(InputProcessor processor)
    {
        player1 = new HumanHexPlayer(TileState.PLAYER1, processor);
    }

    public void setPlayer2(InputProcessor processor)
    {
        player2 = new HumanHexPlayer(TileState.PLAYER2, processor);
    }

    private HexPlayer createPlayer(PossiblePlayers type, TileState player)
    {
        switch(type)
        {
            case random:
                return new RandomHexPlayer(player);
            case alphabetaDijkstra:
                return new AlphaBetaDijkstra(player);
            case alphabetaElectrical:
                return new AlphaBetaElectrical(player);
            case MCTS:
                return new MCTSPlayer(
                        player,
                        new UCTTreePolicy<>(EXPLORATION_PARAMETER_FOR_UCT),
                        new SingleThreadRandomHexBoardSimulation(),
                        AMOUNT_OF_SIMULATIONS_PER_ITERATION,
                        TIME_ALLOWED_FOR_MCTS_IN_MS,
                        PossiblePlayers.MCTS,
                        REUSE_TREE);
            case MCTSNoReuse:
                return new MCTSPlayer(player,
                        new UCTTreePolicy<>(EXPLORATION_PARAMETER_FOR_UCT),
                        new SingleThreadRandomHexBoardSimulation(),
                        AMOUNT_OF_SIMULATIONS_PER_ITERATION,
                        TIME_ALLOWED_FOR_MCTS_IN_MS,
                        PossiblePlayers.MCTSNoReuse,
                        false);
            case MCTSLeafPar:
                return new MCTSPlayer(
                        player,
                        new UCTTreePolicy<>(EXPLORATION_PARAMETER_FOR_UCT),
                        new MultiThreadRandomHexBoardSimulation(2),
                        AMOUNT_OF_SIMULATIONS_PER_ITERATION,
                        TIME_ALLOWED_FOR_MCTS_IN_MS,
                        PossiblePlayers.MCTSLeafPar,
                        REUSE_TREE);
            case MCTSLeafParBad:
                return new MCTSPlayer(
                        player,
                        new UCTTreePolicy<>(EXPLORATION_PARAMETER_FOR_UCT),
                        new OldMultiThreadRandomHexBoardSimulation(2),
                        AMOUNT_OF_SIMULATIONS_PER_ITERATION,
                        TIME_ALLOWED_FOR_MCTS_IN_MS,
                        PossiblePlayers.MCTSLeafParBad,
                        REUSE_TREE);
            case MCTSAMAF:
                    return new MCTSPlayer(
                            player,
                            new AMAFPolicy<>(0, AMAF_BIAS_VALUE),
                            new SingleThreadRandomHexBoardSimulation(),
                            AMOUNT_OF_SIMULATIONS_PER_ITERATION,
                            TIME_ALLOWED_FOR_MCTS_IN_MS,
                            PossiblePlayers.MCTSAMAF,
                            REUSE_TREE);
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
