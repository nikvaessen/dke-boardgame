package nl.dke.boardgame.mcts;

/**
 * The root node of the MonteCarlo Tree. It differs from normal nodes by storing the a State (The initial state)
 *
 * @author nik on 31/12/16.
 */
public class MonteCarloRootNode<S extends State, A extends Action<S> >
        extends MonteCarloNode<S, A>
{
    /**
     * The initial state to begin MonteCarlo Tree Search on
     */
    private S initialState;

    /**
     * Creates a root node for MonteCarlo Tree Search, with a initial State. The root node does not have a parent,
     * and will stop recursive method calls of the MonteCarloNode class
     * @param initialState
     */
    public MonteCarloRootNode(S initialState)
    {
        super(null, null);
        this.initialState = initialState;
    }

    /**
     * Returns the initial state of the monte carlo tree search
     * @return the initial state
     */
    @Override
    public S getState()
    {
        return initialState;
    }

}
