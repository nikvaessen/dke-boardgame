package nl.dke.boardgame.mcts;

/**
 * This interface defines an Action in a markov decision process. An action can be applied on a State s to change it
 * into a new State s`
 *
 * @author nik on 31/12/16.
 */
public interface Action<S extends State>
{
    /**
     * Apply the action to a given state, resulting in a new state
     *
     * @param before the state the action needs to be apllied on
     * @return the new state resulting for the action applied on the given state
     * @throws IllegalArgumentException when the action cannot be legally completed on the given state
     */
    S apply(S before) throws IllegalArgumentException;

}
