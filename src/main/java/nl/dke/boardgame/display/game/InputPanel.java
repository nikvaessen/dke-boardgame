package nl.dke.boardgame.display.game;

import nl.dke.boardgame.exceptions.NotAcceptingInputException;
import nl.dke.boardgame.game.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPanel extends JPanel
{

    private static final long serialVersionUID = 1L;
    private JLabel label;
    private JTextField input;
    private int playerGo = 1;
    private InputProcessor inputProcessor;

    public InputPanel(InputProcessor inputProcessor)
    {
        this.setPreferredSize(new Dimension(150, 100));
        this.inputProcessor = inputProcessor;
        label = new JLabel("Please enter move:");
        input = new JTextField(7);
        JButton button = new JButton("Enter");
        button.addActionListener(new buttonInput());
        this.add(label); this.add(input);; this.add(button);
    }

    private void clearTextField()
    {
        input.setText("");
    }

    private class buttonInput implements ActionListener
    {
		public void actionPerformed(ActionEvent arg0) {
			//would start the move function based on user input
            try
            {
                if (inputProcessor.accepting())
                {
                    inputProcessor.in(input.getText());
                    clearTextField();
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
                        "the rows are integers! Tile could also already havea:11 " +
                        "been claimed");
                e.printStackTrace();
            }
		}
    }
}
