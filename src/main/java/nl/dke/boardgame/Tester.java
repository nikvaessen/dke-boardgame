package nl.dke.boardgame;

import nl.dke.boardgame.display.game.GameFrame;
import nl.dke.boardgame.game.Table;
import nl.dke.boardgame.players.PossiblePlayers;
import nl.dke.boardgame.util.Watcher;

public class Tester implements Watcher{
	
	//possible things to change
	private final int NO_OF_TESTS = 2;
	private PossiblePlayers player1 = PossiblePlayers.alphabeta;
	private PossiblePlayers player2 = PossiblePlayers.alphabeta;
	private int depthLimit = 2;
	private int timeLimit= 10;
	private int dim = 11;
	
	
	private int currentTests = 0;
	
	public Tester() {
		test();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		test();
	}
	
	public void test(){
		if (currentTests < NO_OF_TESTS) {
			currentTests++;
			Table table = new Table();
			table.setPlayer1(player1, depthLimit);
			table.setPlayer2(player2, depthLimit);
			table.setBoardDimensions(dim,dim);
			table.setPieRuleEnabled(false);
			new GameFrame(table.createNewGame(), this);
		}
	}

}
