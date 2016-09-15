package nl.dke.boardgame.display;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame
{

    private static final long serialVersionUID = 1L;
    private MainPanel mainPanel;

    public MainFrame()
    {
        mainPanel = new MainPanel();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
