package nl.dke.boardgame.display.game;

import nl.dke.boardgame.game.GameState;
import nl.dke.boardgame.game.board.TileState;
import nl.dke.boardgame.util.Watcher;

import javax.swing.*;
import java.awt.*;

/**
 * This panel displays information about the current HexGame
 */
public class InfoPanel extends JPanel implements Watcher
{

    private final static String PLAYER_1 = "player 1";
    private final static String PLAYER_1_COLOR_STRING = "RED";
    private final static Color  PLAYER_1_COLOR = Color.RED;

    private final static String PLAYER_2 = "player 2";
    private final static String PLAYER_2_COLOR_STRING = "BLUE";
    private final static Color  PLAYER_2_COLOR = Color.BLUE;

    /**
     * object holding all information about the current game
     */
    private GameState gameState;

    /**
     * All JLabels displaying information about the current turn
     */
    private JLabel currentTurnInfo;
    private JLabel currentTurnPlayer;
    private JLabel currentTurnColor;

    /**
     * All JLabels displaying information about player 1
     */
    private JLabel player1Info;
    private JLabel player1Type;
    private JLabel player1Color;
    private JLabel player1TurnCounter;

    /**
     * All JLabels displaying information about player 2
     */
    private JLabel player2Info;
    private JLabel player2Type;
    private JLabel player2Color;
    private JLabel player2TurnCounter;

    /**
     * All JLabels displaying information about the total amount of turns
     */
    private JLabel totalTurnInfo;
    private JLabel totalTurnCounter;

    /**
     * Create a InfoPanel displaying information about a HexGame
     * @param gameState the GameState belonging to the HexGame
     */
    public InfoPanel(GameState gameState)
    {
        this.gameState = gameState;
        gameState.attachWatcher(this);

        //set general settings of this panel
        this.setPreferredSize(new Dimension(200, 125));
        this.setLayout(new GridBagLayout());

        //all objects related to displaying who's turn it is
        currentTurnInfo = new JLabel("Currently playing:");
        currentTurnPlayer = new JLabel();
        setPlayerString(currentTurnPlayer, gameState.currentTurn());
        currentTurnColor = new JLabel();
        setColorString(currentTurnColor, gameState.currentTurn());

        //all objects related to displaying information about player 1
        player1Info = new JLabel();
        setPlayerString(player1Info, TileState.PLAYER1);
        player1Type = new JLabel(gameState.player1is().toString());
        player1Color = new JLabel();
        setColorString(player1Color, TileState.PLAYER1);
        player1TurnCounter = new JLabel("0");

        //all objects related to displaying information about player 2
        player2Info = new JLabel();
        setPlayerString(player2Info, TileState.PLAYER2);
        player2Type = new JLabel(gameState.player2is().toString());
        player2Color = new JLabel();
        setColorString(player2Color, TileState.PLAYER2);
        player2TurnCounter = new JLabel("0");

        //all objects related to displaying the amount of turns
        totalTurnInfo = new JLabel("total turns:");
        totalTurnCounter = new JLabel("0");

        //place the objects in the panel
        GridBagConstraints c = new GridBagConstraints();

        //add the current player panels
        c.gridx = 0;
        c.gridy = 0;
        add(currentTurnInfo, c);

        c.gridx = 1;
        c.gridy++;
        add(currentTurnPlayer, c);

        c.gridx = 1;
        c.gridy++;
        add(currentTurnColor, c);

        //add the player1 panels
        c.gridx = 0;
        c.gridy++;
        add(player1Info, c);

        c.gridx = 1;
        c.gridy++;
        add(player1Type, c);

        c.gridx = 1;
        c.gridy++;
        add(player1Color, c);

        c.gridx = 1;
        c.gridy++;
        add(player1TurnCounter, c);

        //add the player2 panels
        c.gridx = 0;
        c.gridy++;
        add(player2Info, c);

        c.gridx = 1;
        c.gridy++;
        add(player2Type, c);

        c.gridx = 1;
        c.gridy++;
        add(player2Color, c);

        c.gridx = 1;
        c.gridy++;
        add(player2TurnCounter, c);

        //add panels about total turns
        c.gridx = 0;
        c.gridy++;
        add(totalTurnInfo, c);

        c.gridx = 1;
        c.gridy++;
        add(totalTurnCounter, c);
    }

    @Override
    public void update()
    {
        if(gameState.hasWinner())
        {
            setWinner(gameState.getWinner());
            if(gameState.getWinner() == TileState.PLAYER1)
            {
                incrementPlayer1Turn();
            }
            else
            {
                incrementPlayer2Turn();
            }
        }
        else
        {
            updateCurrentTurn(gameState.currentTurn());
        }
    }

    private void updateCurrentTurn(TileState currentPlayer)
    {
        setPlayerString(currentTurnPlayer, currentPlayer);
        setColorString(currentTurnColor, currentPlayer);
        if(currentPlayer == TileState.PLAYER1)
        {
            incrementPlayer2Turn();
        }
        else
        {
            incrementPlayer1Turn();
        }
    }

    private void incrementPlayer1Turn()
    {
        player1TurnCounter.setText(Integer.toString(gameState.getPlayer1Turns()));
        totalTurnCounter.setText(Integer.toString(gameState.getTotalTurns()));
    }

    private void incrementPlayer2Turn()
    {
        player2TurnCounter.setText(Integer.toString(gameState.getPlayer1Turns()));
        totalTurnCounter.setText(Integer.toString(gameState.getTotalTurns()));
    }

    private void setWinner(TileState player)
    {
        currentTurnInfo.setText("The winner is:");
        setPlayerString(currentTurnPlayer, player);
        setColorString(currentTurnColor, player);
    }

    private void setPlayerString(JLabel label, TileState player)
    {
        if(player == TileState.PLAYER1)
        {
            label.setText(PLAYER_1);
        }
        else
        {
            label.setText(PLAYER_2);
        }
    }

    private void setColorString(JLabel label, TileState player)
    {
        if(player == TileState.PLAYER1)
        {
            label.setText(PLAYER_1_COLOR_STRING);
            label.setForeground(PLAYER_1_COLOR);
        }
        else
        {
            label.setText(PLAYER_2_COLOR_STRING);
            label.setForeground(PLAYER_2_COLOR);
        }
    }
}
