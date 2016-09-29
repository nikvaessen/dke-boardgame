package nl.dke.boardgame.display;

import javax.swing.*;

import nl.dke.boardgame.game.board.Board;

import java.awt.*;

//// TODO: 21/09/16 This class should probably watch a Board class and update
// itself when it changes. It could maybe also implement a Table?

//// TODO: 21/09/16 There should also be a panel to choose what kind of player
// player 1 and player 2 is, and with a button to start the game

public class MainPanel extends JPanel
{

    private static final long serialVersionUID = 1L;
    private DrawPanel draw;
    private Board board;

    public MainPanel(Board board)
    {
    	this.board = board;
        draw = new DrawPanel();
        this.setPreferredSize(new Dimension(700, 520));
    }
    
    public void setBoard(Board board) 
    {
    	this.board = board;
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw.draw(g, board);
    }

    //addNotifiers
    //{

}
