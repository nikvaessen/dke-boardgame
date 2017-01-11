package nl.dke.boardgame.players;

import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.PieMove;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.MonteCarloTree;
import nl.dke.boardgame.mcts.hex.HexBoardAction;
import nl.dke.boardgame.mcts.hex.HexBoardSimulation;
import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;
import nl.dke.boardgame.mcts.policy.TreePolicy;
import nl.dke.boardgame.mcts.policy.UCTTreePolicy;

/**
 * Created by nik on 24/12/16.
 */
public class MCTSPlayer extends HexPlayer
{
    private TreePolicy<HexBoardState, HexBoardAction> treePolicy;
    private SimulationPolicy<HexBoardState> simulationPolicy;
    private int ms;
    private int simulationsPerIteration;

    public MCTSPlayer(TileState claimer, double exploration, int simulationsPerIteration, int ms) throws IllegalArgumentException
    {
        super(claimer);
        treePolicy = new UCTTreePolicy<>(exploration);
        simulationPolicy = new HexBoardSimulation();
        this.ms = ms;
        this.simulationsPerIteration = simulationsPerIteration;
    }

    @Override
    public void finishMove(Move move)
    {
        MonteCarloTree<HexBoardState, HexBoardAction> monteCarloTree =
                new MonteCarloTree<>(treePolicy, simulationPolicy, simulationsPerIteration);


        HexBoardState state = new HexBoardState(move.getBoard().clone(), move.getPlayer());
        HexBoardAction action = monteCarloTree.search(state, ms);
        move.setRow(action.getX());
        move.setColumn(action.getY());
    }

    @Override
    public void finishPieMove(PieMove move)
    {
        finishMove(move);
    }

    @Override
    public PossiblePlayers getTypeOfPlayer()
    {
        return PossiblePlayers.MCTS;
    }
}
