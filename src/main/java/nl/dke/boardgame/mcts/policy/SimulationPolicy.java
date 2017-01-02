package nl.dke.boardgame.mcts.policy;

import nl.dke.boardgame.mcts.State;

/**
 * A SimulationPolicy is used to do MonteCarlo simulation on a given State. A simulation results in a reward,
 * which will be returned after the simulation.
 *
 * @author nik on 31/12/16.
 */
public interface SimulationPolicy<S extends State>
{
    /**
     * apply simulation on a given state do determine the reward
     * @param state the state to use simulation on
     * @return the reward of the simulation
     */
    int simulate(S state);

}
