package nl.dke.boardgame.treeStructure;

import nl.dke.boardgame.exceptions.AlreadyClaimedException;
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
    protected Move move;
    protected Branch parent;
    protected List<Branch> branches;
    protected double score;

    public Branch(Move move){
        this.move = move;
        score = evaluate(move.getBoard());
        checked = false;
    }

    public Branch(Move move, Branch parent){
        this.move = move;
        this.parent = parent;
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

    public double getScore() {
        return score;
    }

    public Move getMove() {
        return move;
    }

    public abstract double evaluate(Board board);

    public void expand() throws AlreadyClaimedException {
        branches = new ArrayList<>();
    }

}
