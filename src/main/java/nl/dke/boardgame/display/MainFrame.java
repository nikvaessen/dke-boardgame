package nl.dke.boardgame.display;

import javax.swing.*;

import nl.dke.boardgame.game.board.Board;

import java.awt.*;

//// TODO: 21/09/16 The whole display package needs to be documented
public class MainFrame extends JFrame
{

    private static final long serialVersionUID = 1L;
    private MainPanel mainPanel;
    Board board;

    public MainFrame(Board board)
    {
    	this.board = board;
        mainPanel = new MainPanel(board);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
