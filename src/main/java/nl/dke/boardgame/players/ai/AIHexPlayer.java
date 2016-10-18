package nl.dke.boardgame.players.ai;

import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.PieMove;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.players.PossiblePlayers;

/**
 * Created by josevelasquez on 10/17/16.
 */
public class AIHexPlayer extends HexPlayer{
    /**
     * Create the HexPlayer
     *
     * @param claimer if the player is player1 or player2
     * @throws IllegalArgumentException when neutral is given as argument
     */
    public AIHexPlayer(TileState claimer) throws IllegalArgumentException {
        super(claimer);
    }

    @Override
    public void finishMove(Move move) {

    }

    @Override
    public void finishPieMove(PieMove move) {

    }

    @Override
    public PossiblePlayers getTypeOfPlayer() {
        return PossiblePlayers.ai;
    }
}
