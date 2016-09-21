package nl.dke.boardgame;

import nl.dke.boardgame.game.board.Board;
import org.junit.Test;

/**
 *
 */
public class BoardTest
{
    @Test
    public void testBoardCreation(){
        Board hexBoard = new Board(11, 11);
        System.out.println(hexBoard);
    }
}
