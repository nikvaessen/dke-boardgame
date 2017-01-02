package nl.dke.boardgame.mcts;

/**
 * Created by nik on 31/12/16.
 */
public interface Action<S extends State>
{
    /**
     * Apply the action to a given state, resulting in a new state
     * @param before the state the action needs to be apllied on
     * @return the new state resulting for the action applied on the given state
     * @throws IllegalArgumentException when the action cannot be legally completed on the given state
     */
    S apply(S before) throws IllegalArgumentException;

}
