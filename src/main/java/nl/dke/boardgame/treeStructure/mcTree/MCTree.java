package nl.dke.boardgame.treeStructure.mcTree;

import nl.dke.boardgame.game.GameState;
import nl.dke.boardgame.game.Move;
import nl.dke.boardgame.treeStructure.Branch;
import nl.dke.boardgame.treeStructure.Tree;

/**
 * Created by josevelasquez on 12/1/16.
 */
public class MCTree extends Tree {

    private final int MAX_DEPTH = 5;
    private Move move;
    private GameState state;

    public MCTree(MCBranch root, GameState state){
        super(root);
        move = root.getMove();
        this.state = state;
        expand();
        makeMove();
    }

    /**
     * expands the tree to it's limits
     */
    public void expand(){
        for (int i = 0; i<branches.size(); i++){
            expandBranch(i);
        }
    }


    /**
     *
     * @param index expands the branch in corresponding index and
     *              unpdates the branches list
     */
    @Override
    public void expandBranch(int index){
        if(branches.size() < MAX_DEPTH * MCBranch.MAX_BRANCHING)
            super.expandBranch(index);
        else
            makeMove();
    }

    /**
     * final method to be called, it makes the move after searching
     */
    private void makeMove(){
        Branch b = getBestBranch();
        //use b to make the move
    }


    private Branch getBestBranch(){

        Branch max = branches.get(0);

        for(Branch b : branches){
            if (b.getScore() > max.getScore())
                max = b;
        }

        boolean moved = false;

        while (!moved){
            if(max.getParent() == branches.get(0)){
                moved = true;
            }
            else
                max = max.getParent();

        }

        return max;
    }



}
