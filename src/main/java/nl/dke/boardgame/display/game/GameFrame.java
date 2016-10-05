package nl.dke.boardgame.display.game;

import javax.swing.*;

import nl.dke.boardgame.game.HexGame;

import java.awt.*;

//// TODO: 21/09/16 The whole display package needs to be documented
public class GameFrame extends JFrame
{

    private static final long serialVersionUID = 1L;

    private HexGame game;

    private HexBoardPanel hexBoardPanel;

    private InputPanel infoPanel;

    public GameFrame(HexGame game)
    {
        this(game, null);
    }

    public GameFrame(HexGame game, InputProcessor inputProcessor)
    {
        this.game = game;
        //create game elements and set up panels
        hexBoardPanel = new HexBoardPanel(game.getBoardWatcher());

        //if there is a InputProcessor, we have human players
        if(inputProcessor != null)
        {
            infoPanel = new InputPanel(inputProcessor);
            getContentPane().add(infoPanel, BorderLayout.EAST);
        }
        //set main gui settings
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().add(hexBoardPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
