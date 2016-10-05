package nl.dke.boardgame.game;

import nl.dke.boardgame.display.game.InputProcessor;
import nl.dke.boardgame.display.game.HumanHexPlayer;
import nl.dke.boardgame.game.board.BoardWatcher;
import nl.dke.boardgame.players.PossiblePlayers;
import nl.dke.boardgame.players.RandomHexPlayer;
import nl.dke.boardgame.game.board.TileState;

/**
 * The table encapsulates a HexGame and it's player. There should be able to
 * accept either human players or AI players.
 */
//// TODO: 21/09/16 implement this class
public class Table
{
    private HexGame game;

    private HexPlayer player1;

    private HexPlayer player2;

    private int currentHeight = HexGame.DEFAULT_BOARD_DIMENSION;

    private int currentWidth = HexGame.DEFAULT_BOARD_DIMENSION;


    public HexGame createNewGame()
    {
        return new HexGame(currentWidth, currentHeight, player1, player2);
    }

    public void setPlayer1(PossiblePlayers type)
    {
        player1 = createPlayer(type, TileState.PLAYER1);
    }

    public void setPlayer2(PossiblePlayers type)
    {
        player2 = createPlayer(type, TileState.PLAYER2);
    }

    public void setPlayer1(InputProcessor processor)
    {
        player1 = new HumanHexPlayer(TileState.PLAYER1, processor);
    }

    public void setPlayer2(InputProcessor processor)
    {
        player2 = new HumanHexPlayer(TileState.PLAYER2, processor);
    }

    private HexPlayer createPlayer(PossiblePlayers type, TileState player)
    {
        switch (type)
        {
            case random:
                return new RandomHexPlayer(player);
            default:
                throw new IllegalArgumentException("couldn't create a" +
                        "HexPlayer, given argument" + type +" is not " +
                        "implemented");
        }
    }

    public void setBoardDimensions(int width, int height)
        throws IllegalArgumentException
    {
        if(width < HexGame.MINIMUM_BOARD_DIMENSION ||
                width > HexGame.MAXIMUM_BOARD_DIMENSION ||
                height < HexGame.MINIMUM_BOARD_DIMENSION ||
                height > HexGame.MAXIMUM_BOARD_DIMENSION)
        {
            throw new IllegalArgumentException();
        }
        this.currentHeight = height;
        this.currentWidth = width;
    }


}
