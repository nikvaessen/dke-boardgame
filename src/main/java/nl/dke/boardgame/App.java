package nl.dke.boardgame;

import nl.dke.boardgame.display.MainFrame;
import nl.dke.boardgame.game.board.Board;

/**
 * Hello world!
 */
public class App
{
    public static void main(String[] args)
    {
    	Board board = new Board(11,11);
        new MainFrame(board);
    }
}
