package nl.dke.boardgame.display;

import nl.dke.boardgame.display.game.GameFrame;
import nl.dke.boardgame.display.game.InputProcessor;
import nl.dke.boardgame.game.HexGame;
import nl.dke.boardgame.game.Table;
import nl.dke.boardgame.players.PossiblePlayers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 *
 */
public class SelectPanel extends JPanel
{
    private JFrame selectFrame;

    private Table table;

    private JButton startButton;

    private JLabel textFieldLabel;

    private JTextField boardDimensionBox;

    private JLabel player1Label;

    private JComboBox<PossiblePlayers> player1;

    private JLabel player2Label;

    private JComboBox<PossiblePlayers> player2;

    private JLabel errorLabel;

    private JCheckBox pieRuleCheckBox;

    public SelectPanel(JFrame frame)
    {
        this.selectFrame = frame;

        //create necessary elements to create a HexGame
        table = new Table();

        //create UI elements
        startButton = new JButton("Create HexGame!");
        startButton.addActionListener(new StartButton());

        boardDimensionBox = new JTextField(3);
        boardDimensionBox.setText((Integer.toString(HexGame.DEFAULT_BOARD_DIMENSION)));
        textFieldLabel = new JLabel("Enter board dimension(" +
                Integer.toString(HexGame.MINIMUM_BOARD_DIMENSION) +
                "-" + Integer.toString(HexGame.MAXIMUM_BOARD_DIMENSION) + ")");

        player1 = new JComboBox<PossiblePlayers>(PossiblePlayers.values());
        player1Label = new JLabel("Player 1:");
        player1.setSelectedIndex(1);

        player2 = new JComboBox<PossiblePlayers>(PossiblePlayers.values());
        player2Label = new JLabel("Player 2:");

        pieRuleCheckBox = new JCheckBox("Enable pie rule");

        errorLabel = new JLabel();
        errorLabel.setText("Dimension should be between " +
                Integer.toString(HexGame.MINIMUM_BOARD_DIMENSION) +
                " and " +
                Integer.toString(HexGame.MAXIMUM_BOARD_DIMENSION) );
        errorLabel.setVisible(false);
        errorLabel.setForeground(Color.RED);

        //create the panel
        this.setPreferredSize(new Dimension(370, 125));
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        this.add(textFieldLabel, c);
        c.gridx = 1;
        this.add(boardDimensionBox, c);

        c.gridx = 0;
        c.gridy++;
        this.add(player1Label, c);
        c.gridx = 1;
        this.add(player1, c);

        c.gridx = 0;
        c.gridy++;
        this.add(player2Label, c);
        c.gridx = 1;
        this.add(player2, c);

        c.gridx = 0;
        c.gridy++;
        this.add(pieRuleCheckBox, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        this.add(startButton, c);

        c.gridx = 0;
        c.gridy = 4;
        this.add(errorLabel, c);
    }

    private class StartButton implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            int dim = getDimension();
            if(dim == -1)
            {
                errorLabel.setVisible(true);
            }
            else
            {
                InputProcessor processor = null;
                PossiblePlayers pType =(PossiblePlayers) player1.getSelectedItem();
                if(pType == PossiblePlayers.human)
                {
                    processor = new InputProcessor();
                    table.setPlayer1(processor);
                    System.out.println("set Player 1 as " + pType);
                }
                else
                {
                    table.setPlayer1((PossiblePlayers) player1.getSelectedItem());
                    System.out.println("set Player 1 as " + pType);
                }

                pType = (PossiblePlayers) player2.getSelectedItem();
                if(pType == PossiblePlayers.human)
                {
                    if(processor == null)
                    {
                        processor = new InputProcessor();
                    }
                    table.setPlayer2(processor);
                    System.out.println("set Player 2 as " + pType);
                }
                else
                {
                    table.setPlayer2(pType);
                    System.out.println("set Player 2 as " + pType);
                }
                table.setBoardDimensions(dim, dim);
                System.out.printf("set board dim as: %d %d\n" ,dim, dim);

                table.setPieRuleEnabled(pieRuleCheckBox.isSelected());
                System.out.println("Set pie rule enabled as: " + pieRuleCheckBox.isSelected());

                if(processor == null)
                {
                    new GameFrame(table.createNewGame());
                }
                else
                {
                    new GameFrame(table.createNewGame(), processor);
                }
                selectFrame.dispose();
            }
        }

        private int getDimension()
        {
            try
            {
                int dim = Integer.parseInt(boardDimensionBox.getText());
                if(dim >= HexGame.MINIMUM_BOARD_DIMENSION ||
                        dim > HexGame.MAXIMUM_BOARD_DIMENSION)
                {
                    return dim;
                }
                else
                {
                    return -1;
                }
            }
            catch (NumberFormatException e)
            {
                return -1;
            }
        }
    }


}
