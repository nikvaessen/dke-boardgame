package test_Graphics;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MainPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private DrawPanel draw;
	
	public MainPanel() {
		draw = new DrawPanel();
		this.setPreferredSize(new Dimension(700,520));
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw.draw(g);
	}
	
	//addNotifiers
	//{
	
}
