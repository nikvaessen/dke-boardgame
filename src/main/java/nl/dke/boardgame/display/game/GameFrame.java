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
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create panel which draws the board
        hexBoardPanel = new HexBoardPanel(game.getGameState());

        getContentPane().add(hexBoardPanel, BorderLayout.CENTER);

        //if there is a InputProcessor, we have human players
        if(inputProcessor != null)
        {
            inputPanel = new InputPanel(inputProcessor);
            getContentPane().add(inputPanel, BorderLayout.EAST);
        }

        //create the panel with information about the game
        infoPanel = new InfoPanel(game.getGameState());
        getContentPane().add(infoPanel, BorderLayout.WEST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        //and start game
        game.start();
    }

}
