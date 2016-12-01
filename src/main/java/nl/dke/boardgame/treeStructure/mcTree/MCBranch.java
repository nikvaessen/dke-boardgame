package nl.dke.boardgame.treeStructure.mcTree;

import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.game.board.Board;
import nl.dke.boardgame.treeStructure.Branch;

/**
 * @author josevelasquez
 */
public class MCBranch extends Branch{

    public MCBranch(Move move){
        super(move);
    }

    public MCBranch(Move move, MCBranch parent){
        super(move, parent);
    }

    @Override
    public int evaluate(Board board) {
        return 0;
    }

    @Override
    public  void expand(){
        super.expand();

    }


    public void selection(){
        //TODO:
    }

    public void expansion(){
        //TODO:
    }

    public void simulation(){

    }

    public void backpropagation(){

    }


}
