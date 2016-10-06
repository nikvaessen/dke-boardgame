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
        label = new JLabel("Please enter move");
        input = new JTextField(7);
        JButton button = new JButton("Enter");
        button.addActionListener(new buttonInput());
        this.add(label); this.add(input);; this.add(button);
    }
    
    public void updateLabel(String newInput) {
    	label.setText(newInput);
    }
    
    public void updateLabel() {
    	label.setText("Player " + playerGo + " go");
    	if (playerGo == 1) playerGo = 2; else playerGo = 1;
    }
    
    private class buttonInput implements ActionListener
    {
		public void actionPerformed(ActionEvent arg0) {
			//would start the move function based on user input
            try
            {
                inputProcessor.in(input.getText());
                updateLabel();
            }
            catch (NotAcceptingInputException e)
            {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("Invalid input: Try <row>:<column>. " +
                        "Note that the rows are integers and the columns " +
                        "letters!");
                e.printStackTrace();
            }
		}
    }
}
