package nl.dke.boardgame.display.game;

import nl.dke.boardgame.exceptions.NotAcceptingInputException;
import nl.dke.boardgame.game.GameState;
import nl.dke.boardgame.util.Watcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPanel extends JPanel implements Watcher
{

    private static final long serialVersionUID = 1L;
    private JLabel textFieldLabel;
    private JTextField input;
    private JButton switchButton;
    private GameState gameState;
    private InputProcessor inputProcessor;

    public InputPanel(GameState gameState, InputProcessor inputProcessor)
    {
        this.gameState = gameState;
        gameState.attachWatcher(this);

        this.inputProcessor = inputProcessor;

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //label above the textfield
        textFieldLabel = new JLabel("Please enter move:");

        //text field to type the hextile to claim in
        input = new JTextField(7);
        input.addActionListener(new UserInputCompleteListener());

        //button to enter the tile typed into the text field
        JButton button = new JButton("Enter");
        button.addActionListener(new UserInputCompleteListener());

        switchButton = new JButton("Switch");
        switchButton.addActionListener(new SwitchButtonListener());

        c.gridy = 0; this.add(textFieldLabel, c);
        c.gridy = 1; this.add(input, c);
        c.gridy = 2; this.add(button, c);
    }

    public void update()
    {
        if(gameState.getTotalTurns() == 1 && gameState.isPieRuleEnabled())
        {
            switchButton.setVisible(true);
        }
        else if(switchButton.isVisible())
        {
            switchButton.setVisible(false);
        }
    }

    private void clearTextField()
    {
        input.setText("");
    }

    private class UserInputCompleteListener implements ActionListener
    {
		public void actionPerformed(ActionEvent arg0) {
			//would start the move function based on user input
            try
            {
                if (inputProcessor.accepting())
                {
                    inputProcessor.in(input.getText());
                }
            }
            catch (NotAcceptingInputException e)
            {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("Invalid input: Try <column>:<row>. A " +
                        "syntactically correct input would be A:5. " +
                        "Note that the columns are letters and " +
                        "the rows are integers! Tile could also already have " +
                        "been claimed");
                e.printStackTrace();
             }
            finally
            {
                clearTextField();
            }
        }
    }

    private class SwitchButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            System.out.println("switch button not implemented");
        }
    }

}
