package test_Graphics;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private MainPanel mainPanel;
	
	public MainFrame() {
		mainPanel = new MainPanel();
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
	}

	public static void main(String[] args) {
		MainFrame test = new MainFrame();
		
	}

}
