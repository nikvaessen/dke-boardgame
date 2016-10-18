package nl.dke.boardgame.players.ai.trees;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.board.TileState;

import java.util.ArrayList;

/**
 * Created by josevelasquez on 10/17/16.
 */
public class GameBranch {

    /**
     * Reference to the past state of the game
     */
    private GameBranch parent;

    /**
     * To be used to get the best move
     */
    private float score;

    /**
     * Board representing the physical state of the game
     */
    private Board board;

    /**
     * To reference possible future moves
     */
    private ArrayList<GameBranch> branches;


    /**
     * @param parent to reference pass state
     * @param row row where new move will take place
     * @param column column where new move will take place
     * @param tileState player who will make the new move
     * @throws AlreadyClaimedException To check if (row, column) combination is possible
     */
    public GameBranch(GameBranch parent, int row, int column, TileState tileState) throws AlreadyClaimedException {
        this.parent = parent;
        board = parent.getBoard().clone();
        board.claim(row,column,tileState);
        branches = new ArrayList<>();
    }

    /**
     * to get the score of the branch
     * @return the score
     */
    public float getScore(){
        return score;
    }

    /**
     * to get physical representation of board
     * @return the board
     */
    public Board getBoard() {
        return board;
    }
}
