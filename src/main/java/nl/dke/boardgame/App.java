package nl.dke.boardgame;

import nl.dke.boardgame.display.MainFrame;
import nl.dke.boardgame.display.game.GameFrame;
import nl.dke.boardgame.game.HexGame;

/**
 * Launch the HexGame
 */
public class App
{
    public static void main(String[] args)
    {
        HexGame.DELAY_BETWEEN_TURNS = 1;
        new MainFrame();
    }
}
