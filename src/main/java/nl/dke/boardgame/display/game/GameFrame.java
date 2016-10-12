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

    private InputPanel inputPanel;

    private InfoPanel infoPanel;

    public GameFrame(HexGame game)
    {
        this(game, null);
    }

    public GameFrame(HexGame game, InputProcessor inputProcessor)
    {
        this.game = game;

        //set main gui settings
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagConstraints c = new GridBagConstraints();

        //create panel which draws the board
        hexBoardPanel = new HexBoardPanel(game.getGameState(), inputProcessor);
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        getContentPane().add(hexBoardPanel, c);

        //create the panel with information about the game
        infoPanel = new InfoPanel(game.getGameState());
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        getContentPane().add(infoPanel, c);

        //if there is a InputProcessor, we have human players
        if(inputProcessor != null)
        {
            inputPanel = new InputPanel(game.getGameState(), inputProcessor);
            c.gridx = 1;
            c.gridy = 1;
            getContentPane().add(inputPanel, c);
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        //and start game
        game.start();
    }

}
