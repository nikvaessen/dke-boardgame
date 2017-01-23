package nl.dke.boardgame;

import nl.dke.boardgame.display.game.GameFrame;
import nl.dke.boardgame.game.Table;
import nl.dke.boardgame.players.PossiblePlayers;
import nl.dke.boardgame.util.Watcher;

public class Tester implements Watcher{
	
	//possible things to change
	private final int NO_OF_TESTS = 10;
	//private PossiblePlayers player1 = PossiblePlayers.alphabeta;
	private PossiblePlayers player2 = PossiblePlayers.MCTS;
	private int depthLimit = 2;
	private int timeLimit= 20000;
	private int dim = 11;
	private int[][] limits = {{1,1},{1,2},{1,3},
							  {2,1},{2,2},{2,3},
							  {3,1},{3,2},{3,3}};
	
	
	
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
//			Table table = new Table();
//			table.setPlayer1(player1, depthLimit);
//			table.setPlayer2(player2, timeLimit);
//			table.setBoardDimensions(dim,dim);
//			table.setPieRuleEnabled(false);
//			new GameFrame(table.createNewGame(), this);
//			currentTests++;
		}
	}

}
