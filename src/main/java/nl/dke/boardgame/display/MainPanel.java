package nl.dke.boardgame.display;

import javax.swing.*;
import java.awt.*;

//// TODO: 21/09/16 This class should probably watch a Board class and update
// itself when it changes. It could maybe also implement a Table?

public class MainPanel extends JPanel
{

    private static final long serialVersionUID = 1L;
    private DrawPanel draw;

    public MainPanel()
    {
        draw = new DrawPanel();
        this.setPreferredSize(new Dimension(700, 520));
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw.draw(g);
    }

    //addNotifiers
    //{

}
