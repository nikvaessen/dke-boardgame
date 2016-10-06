package nl.dke.boardgame.display;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class MainFrame extends JFrame
{
    private JPanel selectPanel;

    public MainFrame()
    {
        //create game elements and set up panels
        selectPanel = new SelectPanel(this);

        //set main gui settings
        setLayout(new BorderLayout());
        getContentPane().add(selectPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
