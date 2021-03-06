package nl.dke.boardgame.players;

import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.PieMove;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.mcts.MonteCarloTree;
import nl.dke.boardgame.mcts.hex.HexBoardAction;
import nl.dke.boardgame.mcts.hex.HexBoardState;
import nl.dke.boardgame.mcts.policy.SimulationPolicy;
import nl.dke.boardgame.mcts.policy.TreePolicy;

/**
 * Created by nik on 24/12/16.
 */
public class MCTSPlayer extends HexPlayer {


    private TreePolicy<HexBoardState, HexBoardAction> treePolicy;
    private SimulationPolicy<HexBoardState> simulationPolicy;
    private int ms;
    private int simulationsPerIteration;
    private MonteCarloTree<HexBoardState, HexBoardAction> monteCarloTree;
    private PossiblePlayers name;

    public MCTSPlayer(TileState claimer,
                      TreePolicy<HexBoardState, HexBoardAction> treePolicy,
                      SimulationPolicy<HexBoardState> simulationPolicy,
                      int simulationsPerIteration,
                      int ms,
                      PossiblePlayers name,
                      boolean treeReuse) {
        super(claimer);
        this.treePolicy = treePolicy;
        this.simulationPolicy = simulationPolicy;
        this.ms = ms;
        this.simulationsPerIteration = simulationsPerIteration;
        monteCarloTree = new MonteCarloTree<>(treePolicy, simulationPolicy, simulationsPerIteration, treeReuse);
        this.name = name;
    }

    @Override
    public void finishMove(Move move) {


        HexBoardState state = new HexBoardState(move.getBoard().clone(false), move.getPlayer());

            HexBoardAction action = monteCarloTree.search(state, ms);
        //System.out.println("Number of nodes = " + monteCarloTree.numberNodes);
        move.setRow(action.getX());
        move.setColumn(action.getY());
    }

    @Override
    public void finishPieMove(PieMove move) {
        finishMove(move);
    }

    @Override
    public PossiblePlayers getTypeOfPlayer() {
        return name;
    }
}
