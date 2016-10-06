package nl.dke.boardgame.players;

import nl.dke.boardgame.display.game.InputProcessor;
import nl.dke.boardgame.exceptions.NotAcceptingInputException;
import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.board.TileState;

/**
 *
 */
//// TODO: 21/09/16 make it so it can receive inputProcessor from the swing panel
////    This class should be able to receive what move to make from the
////    another class, such as a JPanel or mouse/keyboard listener
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
            while(!move.isSet())
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

}
