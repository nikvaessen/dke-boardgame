package nl.dke.boardgame.mcts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nik on 31/12/16.
 */
public interface State
{
    /**
     * Get the state resulting from the current state with the given action applied to it
     * @param action The action to apply to this state
     * @return the state resulting from the given action applied to the current state
     * @throws IllegalArgumentException when the given action cannot be applied to the current state, either because
     * it does not correspond to the given state or the action cannot legally be applied to that state
     */
     <S extends State> S next(Action<? extends State> action) throws IllegalArgumentException;

    /**
     * List all the actions possible from the current state
     * @return a list of all possible actions which can be legally applied to this state
     */
     List<? extends Action> possibleActions();

    /**
     * Build the State given the previous actions
     * @param sequences the array of previous actions
     * @param <S> the State
     * @param <A> the Action
     * @return the State resulting from applying all previous actions to a initial state
     */
     //<S extends State, A extends Action<? extends State>> S buildState(ArrayList<A> sequences);
}
