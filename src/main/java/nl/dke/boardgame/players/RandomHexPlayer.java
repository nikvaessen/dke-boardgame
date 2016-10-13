package nl.dke.boardgame.players;

import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.PieMove;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;

import java.util.Random;

/**
 * A HexPlayer players which will randomly claim tiles on the board
 */
public class RandomHexPlayer extends HexPlayer
{
    /**
     * Random number generator
     */
    private Random rng = new Random(System.currentTimeMillis());

    /**
     * Creates a players which randomly claims tiles
     * @param state as which player is will play
     */
    public RandomHexPlayer(TileState state)
    {
        super(state);
    }

    /**
     * randomly claims a tile
     * @param move the move the player needs to make
     */
    @Override
    public void finishMove(Move move)
    {
        Board board = move.getBoard();
        while(!move.verify())
        {
            try
            {
                move.setRow(rng.nextInt(board.getHeight()));
                move.setColumn(rng.nextInt(board.getWidth()));
            }
            catch(IllegalArgumentException e)
            {
                continue;
            }
        }
    }

    @Override
    public void finishPieMove(PieMove move)
    {
        finishMove(move);
    }

    @Override
    public PossiblePlayers getTypeOfPlayer()
    {
        return PossiblePlayers.random;
    }
}
