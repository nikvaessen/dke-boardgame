package nl.dke.boardgame.treeStructure;

import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.game.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * this class is made to be the super class for branches in different algorithms
 * Every algorithm will have a different expand and evaluation functions
 *
 * @author josevelasquez
 */
public abstract class Branch {

    public boolean checked;
    private Branch parent;
    private List<Branch> branches;
    private int score;

    public Branch(Move move){
        score = evaluate(move.getBoard());
        checked = false;
    }

    public Branch getParent() {
        return parent;
    }

    public void setParent(Branch parent) {
        this.parent = parent;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public abstract int evaluate(Board board);

    public void expand(){
        branches = new ArrayList<>();
        // here
    }

}
