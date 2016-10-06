package nl.dke.boardgame.display.game;

import javax.swing.*;

import nl.dke.boardgame.game.board.BoardWatcher;

import java.awt.*;

//// TODO: 21/09/16 This class should probably watch a Board class and update
// itself when it changes. It could maybe also implement a Table?

//// TODO: 21/09/16 There should also be a panel to choose what kind of player
// player 1 and player 2 is, and with a button to start the game

public class HexBoardPanel extends JPanel
{

    private static final long serialVersionUID = 1L;
    private DrawPanel draw;
    private BoardWatcher watcher;
    private Thread updater;

    public HexBoardPanel(final BoardWatcher watcher)
    {
    	this.watcher = watcher;
        draw = new DrawPanel();
        this.setPreferredSize(new Dimension(700, 520));

        updater = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    if(watcher.changed())
                    {
                        repaint();
                    }
                }
            }
        });
        updater.start();
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw.draw(g, watcher.getBoard());
    }



}
