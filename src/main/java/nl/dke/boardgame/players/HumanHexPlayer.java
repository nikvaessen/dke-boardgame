package nl.dke.boardgame.players;

import nl.dke.boardgame.display.game.InputProcessor;
import nl.dke.boardgame.exceptions.NotAcceptingInputException;
import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.PieMove;
import nl.dke.boardgame.game.board.TileState;

/**
 * This class relies on the Display package to retrieve input from the user
 * as to what move to make
 */
public class HumanHexPlayer extends HexPlayer
{

    /**
     * Retrieves user inputProcessor from a swing panel
     */
    private InputProcessor inputProcessor;

    /**
     * Creates the HumanHexPlayer class
     * @param state the kind of player
     * @param input the object retrieving the inputProcessor from the user
     */
    public HumanHexPlayer(TileState state, InputProcessor input)
    {
        super(state);
        this.inputProcessor = input;
    }

    @Override
    public void finishMove(Move move)
    {
        try
        {
            inputProcessor.give(move);
            //block thread until a correct input is given
            while(!move.isSet() || !move.verify())
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (NotAcceptingInputException e)
        {
            e.printStackTrace();
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
        return PossiblePlayers.human;
    }
}
