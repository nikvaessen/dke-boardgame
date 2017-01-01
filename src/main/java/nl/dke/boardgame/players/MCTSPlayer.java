package nl.dke.boardgame.players;

import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.PieMove;
import nl.dke.boardgame.game.board.TileState;

/**
 * Created by nik on 24/12/16.
 */
public class MCTSPlayer extends HexPlayer
{
    public MCTSPlayer(TileState claimer) throws IllegalArgumentException
    {
        super(claimer);

    }

    @Override
    public void finishMove(Move move)
    {

    }

    @Override
    public void finishPieMove(PieMove move)
    {

    }

    @Override
    public PossiblePlayers getTypeOfPlayer()
    {
        return PossiblePlayers.MonteCarloTreeSearch;
    }
}
