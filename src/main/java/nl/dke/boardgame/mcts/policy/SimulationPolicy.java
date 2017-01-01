package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.State;

/**
 * Created by nik on 31/12/16.
 */
public interface SimulationPolicy<S extends State>
{
    int simulate(S state);
}
