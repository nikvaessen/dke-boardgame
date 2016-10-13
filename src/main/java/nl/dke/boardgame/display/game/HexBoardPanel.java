package nl.dke.boardgame.display.game;

import javax.swing.*;

import nl.dke.boardgame.exceptions.NotAcceptingInputException;
import nl.dke.boardgame.game.GameState;
import nl.dke.boardgame.game.HexPlayer;
import nl.dke.boardgame.util.Watcher;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class HexBoardPanel extends JPanel implements Watcher
{

    private static final long serialVersionUID = 1L;
    private DrawPanel draw;
    private GameState gameState;

    public HexBoardPanel(GameState gameState, InputProcessor processor)
    {
        this.gameState = gameState;
        gameState.attachWatcher(this);

        draw = new DrawPanel();

        //calculate the size of the board
        int rows = gameState.getCurrentBoard().length;
        int columns = gameState.getCurrentBoard()[0].length;

        //as implemented in DrawPanel
        int hexagonWidth = DrawPanel.LENGTH * 2;
        int hexagonHeight = DrawPanel.LENGTH * 3;

        int panelWidth = DrawPanel.OFFSET_X + hexagonWidth * columns;
        //add the amount of shifting
        panelWidth += columns * DrawPanel.LENGTH;

        int panelHeight = DrawPanel.OFFSET_Y + (hexagonWidth * rows - 1);
        //add the amount lost because the hexagons in a next row
        //fall into the hexagon of the previous row
        panelHeight += hexagonHeight;

        setPreferredSize(new Dimension(panelWidth, panelHeight));

        //if it's not null we have a human player :)
        if(processor != null)
        {
            this.addMouseListener(new MouseClickListener(processor));
        }
    }


    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw.draw(g, gameState.getCurrentBoard());
    }

    @Override
    public void update()
    {
        repaint();
    }

    private class MouseClickListener implements MouseListener
    {
        private InputProcessor processor;

        private MouseClickListener(InputProcessor processor)
        {
            this.processor = processor;
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent)
        {
            Hexagon hex = draw.getHexagon(mouseEvent.getPoint());
            System.out.println("Pressed on: " + hex);
            try
            {
                if(processor.accepting())
                {
                    processor.in(hex.getRow(), hex.getColumn());
                }
            }
            catch (NotAcceptingInputException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent)
        {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent)
        {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent)
        {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent)
        {

        }
    }
}
