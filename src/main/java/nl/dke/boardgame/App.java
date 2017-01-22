package nl.dke.boardgame;

import nl.dke.boardgame.display.MainFrame;
import nl.dke.boardgame.display.game.GameFrame;
import nl.dke.boardgame.game.HexGame;
import nl.dke.boardgame.game.Table;
import nl.dke.boardgame.players.PossiblePlayers;

/**
 * Launch the HexGame
 */
public class App
{

    public static void main(String[] args)
    {
        HexGame.DELAY_BETWEEN_TURNS = 10;
        new MainFrame();
    }
}
